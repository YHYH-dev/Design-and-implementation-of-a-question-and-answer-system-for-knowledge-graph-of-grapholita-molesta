package com.yyj.app.service.impl;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.yyj.app.dao.InsectsRepository;
import com.yyj.app.dao.PestRepository;
import com.yyj.app.dao.PlantsRepository;
import com.yyj.app.process.QuestionProcess;
import com.yyj.app.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private InsectsRepository insectsRepository;
    @Autowired
    private PlantsRepository plantsRepository;
    @Autowired
    private PestRepository pestRepository;

    @Override
    public HashMap<String,List<String>> answer(String question) throws IOException {
//        问句处理
        String best_label = "";
        String ientity = "";
        String pentity = "";

        question = question.replace(" ","");

        HashMap<String,List<String>> result_map = new HashMap<>();
        List result = new ArrayList<>();
        String enitity = "";
        String relation = "";
//        NLPTokenizer.ANALYZER.enableCustomDictionary(true);
//        List<Term> termList = NLPTokenizer.segment(question);//分词
//        System.out.println(termList);

        Segment segment = HanLP.newSegment();
        segment.enableCustomDictionary(true);
        List<Term> termList = segment.seg(question);
        System.out.println(termList);

        for (Term t :termList){
            if (t.nature.toString().equals("ni")){
                ientity = t.word;
            }
            else if (t.nature.toString().equals("np")){
                pentity = t.word.replace("子","");
            }
        }
        if (termList.size() == 1){//单一个实体
//            System.out.println(ientity);
//            System.out.println("1");
            if (!ientity.isEmpty()) {
                result = insectsRepository.findInsectsSummary(ientity);
                enitity=ientity+"::4";
            }
            else {
                result = plantsRepository.findPlantsSummary(pentity);
                enitity=pentity+"::9";
            }
            relation= "具有简介";
        }
        else {
            QuestionProcess qp = new QuestionProcess();
            best_label =  qp.process(question);

            switch (best_label){
                case "Alias":
                    if (!ientity.isEmpty()) {
                        result = insectsRepository.findInsectsAlias(ientity);
                        enitity=ientity+"::4";
                    }
                    else {
                        result = plantsRepository.findPlantsAlias(pentity);
                        enitity=pentity+"::9";
                    }
                    relation="具有别名";
                    break;
                case "Chemicals":
                    result = pestRepository.findPestChemicals(ientity+"虫害");
                    enitity=ientity+"虫害::8";
                    relation="具有药剂";
                    break;
                case "Enemy":
                    result = insectsRepository.findInsectsEnemy(ientity);
                    enitity=ientity+"::4";
                    relation="具有天敌";
                    break;
                case "Features":
                    result = insectsRepository.findInsectsFeatures(ientity);
                    enitity=ientity+"::4";
                    relation="具有形态特征";
                    break;
                case "Habit":
                    result = insectsRepository.findInsectsHabit(ientity);
                    enitity=ientity+"::4";
                    relation="具有生活习性";
                    break;
                case "LatinName":
                    if (!ientity.isEmpty()) {
                        result = insectsRepository.findInsectsLatinName(ientity);
                        enitity=ientity+"::4";
                    }
                    else {
                        result = plantsRepository.findPlantsLatinName(pentity);
                        enitity=pentity+"::9";
                    }
                    relation="具有拉丁名";
                    break;
                case "Location":
                    result = insectsRepository.findInsectsLocation(ientity);
                    enitity=ientity+"::4";
                    relation="分布于";
                    break;
                case "Parts":
                    result = pestRepository.findPestParts(ientity+"虫害");
                    enitity=ientity+"虫害::8";
                    relation="为害部位";
                    break;
                case "Plants":
                    result = pestRepository.findPestPlants(ientity+"虫害");
                    enitity=ientity+"虫害::8";
                    relation="为害植物";
                    break;
                case "Prevention":
                    result = pestRepository.findPestPrevention(ientity+"虫害");
                    enitity=ientity+"虫害::8";
                    relation="具有防治方法";
                    break;
                case "Reproduction":
                    result = insectsRepository.findInsectsReproduction(ientity);
                    enitity=ientity+"::4";
                    relation="具有繁殖方式";
                    break;
                case "Rules":
                    result = pestRepository.findPestRules(ientity+"虫害");
                    enitity=ientity+"虫害::8";
                    relation="具有为害规律";
                    break;
                case "Summary":
                    if (!ientity.isEmpty()) {
                        result = insectsRepository.findInsectsSummary(ientity);
                        enitity=ientity+"::4";
                    }
                    else {
                        result = plantsRepository.findPlantsSummary(pentity);
                        enitity=pentity+"::9";
                    }
                    relation="具有简介";
                    break;
                case "Symptom":
                    result = pestRepository.findPestSymptom(ientity+"虫害");
                    enitity=ientity+"虫害::8";
                    relation="具有症状";
                    break;
                case "Taxonomy":
                    if (!ientity.isEmpty()) {
                        result = insectsRepository.findInsectsTaxonomy(ientity);
                        enitity=ientity+"::4";
                    }
                    else {
                        result = plantsRepository.findPlantsTaxonomy(pentity);
                        enitity=pentity+"::9";
                    }
                    relation="具有分类";
                    break;

            }

        }
        if (!result.isEmpty()){
            List<String> result_list = new ArrayList<>();
            List<String> entity_list = new ArrayList<>();
            List<String> relation_list = new ArrayList<>();
            entity_list.add(enitity);
            for (Object o : result) {
                System.out.println(o.toString());
                result_list.add(o.toString());
                entity_list.add(o.toString());
            }
            for (String s:result_list){
                relation_list.add(enitity + "->" + relation + "->" + s);
            }
            result_map.put("entity", entity_list);
            result_map.put("relation", relation_list);
            result_map.put("data", result_list);
        }
        return result_map;
    }

//    @Test
//    public void testall() throws IOException{
//        String question = "梨小食心虫的天敌是谁";
//        QuestionServiceImpl qs = new QuestionServiceImpl();
//        System.out.println(qs.answer(question));
//
//    }
}
