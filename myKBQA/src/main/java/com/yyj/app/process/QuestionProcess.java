package com.yyj.app.process;


import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
//import com.sun.deploy.net.MessageHeader;
import com.yyj.app.dao.InsectsRepository;
import com.yyj.app.entity.Insects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.testng.annotations.Test;

import java.io.*;
import java.util.*;

public class QuestionProcess {

    //对用户问题进行处理分类，并提取实体、关系等信息
    public String process(String question) throws IOException {

        String best_label = "";
        double max_cos = 0.0;
        QuestionProcess qp = new QuestionProcess();
        String lib_path = "src/main/resources/questionLib";
        File lib_dir = new File(lib_path);
        File[] file_list = lib_dir.listFiles();
        List<String> topic_list = new ArrayList<>();

        assert file_list != null;
        for (File f:file_list){
            String name = f.getName();
            topic_list.add(name.replace(".txt",""));
        }
        System.out.println(topic_list);

        HashMap<String,Double> tfidf = qp.getTFIDF(question,topic_list);

        for(String topic : topic_list){
            double tmp_cos = qp.getCosSimilarity(question,topic,tfidf);
            if(max_cos < tmp_cos){
                max_cos = tmp_cos;
                best_label = topic;
            }
        }
        System.out.println("------------------------------------------------");
        System.out.println("best_label:" + best_label);
        return best_label;
    }
    //连同问题在内，计算其与问句库中的TFIDF值
    public HashMap<String,Double> getTFIDF(String question,List<String> topic_list) throws IOException {
        NLPTokenizer.ANALYZER.enableCustomDictionary(true);
        int line_num = 1;
        int word_num = 0;
        List<String> str_list = new ArrayList<>();//问句列表
        List<List<String>> s_str_list = new ArrayList<>();//分词列表
        HashMap<String, Integer> word_map = new HashMap<String, Integer>();//存词频
        HashMap<String, Double> word_TF = new HashMap<String, Double>();//存词频
        HashMap<String, Double> word_TFIDF = new HashMap<String, Double>();//存词频
        HashMap<String, Double> word_IDF = new HashMap<String, Double>();//逆文档频率

        for(String topic:topic_list){
            File f = new File("src/main/resources/questionLib/"+topic+".txt");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String tmp = "";
            while ((tmp = br.readLine()) != null){
                str_list.add(tmp);
                line_num++;
            }
        }

        str_list.add(question);
//        System.out.println(str_list);
        for (String s : str_list) {
            List<Term> termList = NLPTokenizer.segment(s);//分词
            List<String> tmp_list = new ArrayList<>();
            for(Term term: termList){

//                System.out.println(term.nature);
                if(term.nature.toString().equals("ni")){//提取并替换信息词
                    term.word = "ni";
                }
                tmp_list.add(term.word);
                if(!word_map.containsKey(term.word)){
                    word_map.put(term.word,1);
                }
                else {
                    word_map.put(term.word,word_map.get(term.word) + 1);
                }
            }
            s_str_list.add(tmp_list);
//            System.out.println(word_map);
        }

        for(int val:word_map.values()){
            word_num += val;
        }
        for(String key:word_map.keySet()){
            int tmp_num = 0;
            int doc_num = 0;
            for(List<String> senList : s_str_list){
                if (senList.contains(key)){
                    doc_num += senList.size();
                    tmp_num++;
                }
            }
            word_TF.put(key, ((double)word_map.get(key)/(double)doc_num));//TF除以含有此词的文档的词数而非全部词数
//            System.out.println(key + ":" + tmp_num);
            word_IDF.put(key,Math.log((double)line_num/((double) (tmp_num+1))));
            word_TFIDF.put(key,word_TF.get(key) * word_IDF.get(key));
        }
//        System.out.println(word_TF);
//        System.out.println(word_IDF);
//        System.out.println(word_TFIDF);
        return word_TFIDF;
    }


    public double getCosSimilarity(String qustion, String topic, HashMap<String,Double> tfidf) throws IOException {
        List<String> str_list = new ArrayList<>();
        File f = new File("src/main/resources/questionLib/"+topic+".txt");
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String tmp = "";
        while ((tmp = br.readLine()) != null){
            str_list.add(tmp);
        }
        double max_cos = 0.0;
        for(String s:str_list){
//            HashMap<String,Double>tfidf = getTFIDF(qustion,topic);
//        String s1,String s2 , HashMap<String,Double> tfidf
            NLPTokenizer.ANALYZER.enableCustomDictionary(true);
            List<Term> termList1 = NLPTokenizer.segment(s);//分词
            List<Term> termList2 = NLPTokenizer.segment(qustion);//分词
            List<Double> vector1 = new ArrayList<>();
            List<Double> vector2 = new ArrayList<>();

            List<String> s1list = new ArrayList<>();
            List<String> s2list = new ArrayList<>();

            for (Term t:termList1){
                s1list.add(t.word);
            }
            for (Term t:termList2){
                s2list.add(t.word);
            }


            for(String key:tfidf.keySet()){
                if (s1list.contains(key)){
                    vector1.add(tfidf.get(key));
                }
                else {
                    vector1.add(0.0);
                }
                if (s2list.contains(key)){
                    vector2.add(tfidf.get(key));
                }
                else {
                    vector2.add(0.0);
                }
            }

//            System.out.println(vector1);
//            System.out.println(vector2);
            double top = 0.0;
            double bot1 = 0.0;
            double bot2 = 0.0;
            for (int i = 0;i < vector1.size();i++){
                top += (vector1.get(i) * vector2.get(i));
                bot1 += Math.pow(vector1.get(i),2);
                bot2 += Math.pow(vector2.get(i),2);
            }
            double cos = top/Math.pow((bot1*bot2),0.5);
//            System.out.println(cos);
            max_cos = (Math.max(max_cos, cos));
        }
        System.out.println(topic+":"+max_cos);
        return max_cos;
    }

    @Test
    public void testGet() throws IOException {
        String question = "梨小食心虫危害的植物是什么";
        QuestionProcess qp = new QuestionProcess();
        qp.process(question);
    }

    @Test
    public void testHanLP(){
        String lineStr = "梨小食心虫的天敌是谁";
        try{
            Segment segment = HanLP.newSegment();
            segment.enableCustomDictionary(true);
            /**
             * 自定义分词+词性
             */

            List<Term> seg = segment.seg(lineStr);
            for (Term term : seg) {
                System.out.println(term.toString());
            }
        }catch(Exception ex){
            System.out.println(ex.getClass()+","+ex.getMessage());
        }
    }

    @Test
    public void testHanLP2(){
        String lineStr = "梨小食心虫的天敌是谁";

        NLPTokenizer.ANALYZER.enableCustomDictionary(true);
        List<Term> termList = NLPTokenizer.segment(lineStr);//分词
        System.out.println(termList);

        try{
            Segment segment = HanLP.newSegment();
            segment.enableCustomDictionary(true);
            List<Term> seg = segment.seg(lineStr);
            System.out.println(seg);
        }catch(Exception ex){
            System.out.println(ex.getClass()+","+ex.getMessage());
        }
    }

//    public static void main(String[] args) throws IOException {
//        String question = "梨小食心虫有什么别名";
//        QuestionProcess qp = new QuestionProcess();
//        System.out.println(qp.process(question));
//    }
}
