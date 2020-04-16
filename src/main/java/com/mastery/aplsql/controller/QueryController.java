package com.mastery.aplsql.controller;

import com.mastery.aplsql.model.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Queue;

@RestController
@CrossOrigin
public class QueryController {

    @PostMapping("/create")
    public String createTable(@RequestBody Query query){
        System.out.println(query.getQueryString());
        return null;
    }

    @GetMapping("/select")
    public List<List<String>> getResultOfQuery(@RequestParam Query query){
        System.out.println(query.getQueryString());
        return null;
    }

    @PostMapping("/insert")
    public List<String> addRecord(@RequestParam Query query){
        System.out.println(query.getQueryString());
        return null;
    }

    @PutMapping("/update")
    public List<String> updateRecord(@RequestParam Query query){
        System.out.println(query.getQueryString());
        return null;
    }
}
