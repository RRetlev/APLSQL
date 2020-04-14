package com.mastery.aplsql.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Controller
public class QueryController {

    @PostMapping("/create")
    public String createTable(){
        return null;
    }

    @GetMapping("/select")
    public List<List<String>> getResultOfQuery(){
        return null;
    }

    @PostMapping("/insert")
    public List<String> addRecord(){
        return null;
    }

    @PutMapping("/update")
    public List<String> updateRecord(){
        return null;
    }
}
