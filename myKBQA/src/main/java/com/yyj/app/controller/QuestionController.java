package com.yyj.app.controller;

import com.yyj.app.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/query")
    public HashMap<String,List<String>> query(String question) throws Exception {
        return questionService.answer(question);
    }


}
