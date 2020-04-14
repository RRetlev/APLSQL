package com.mastery.aplsql.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class QueryController {

    @GetMapping("/select")
    public List<List<String>> getResultOfQuery(){
        return null;
    }
}
