package com.mastery.aplsql.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class QueryController {

    @PostMapping("/create")
    public String createTable(@RequestParam String query){
        System.out.println(query);
        return null;
    }

    @GetMapping("/select")
    public List<List<String>> getResultOfQuery(@RequestParam String query){
        System.out.println(query);
        return null;
    }

    @PostMapping("/insert")
    public List<String> addRecord(@RequestParam String query){
        System.out.println(query);
        return null;
    }

    @PutMapping("/update")
    public List<String> updateRecord(@RequestParam String query){
        System.out.println(query);
        return null;
    }
}
