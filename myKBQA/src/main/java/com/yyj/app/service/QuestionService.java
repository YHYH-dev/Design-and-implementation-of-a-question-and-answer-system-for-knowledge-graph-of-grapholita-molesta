package com.yyj.app.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface QuestionService {

    HashMap<String,List<String>> answer(String question) throws IOException;
}
