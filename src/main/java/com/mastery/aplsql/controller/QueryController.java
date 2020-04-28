package com.mastery.aplsql.controller;

import com.mastery.aplsql.Datastorage.Storage;
import com.mastery.aplsql.exceptions.DuplicateEntryException;
import com.mastery.aplsql.model.Query;
import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.TableProperties;
import com.mastery.aplsql.service.CreateQueryStringParser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class QueryController {

    private Storage storage = new Storage();

    @PostMapping("/create")
    public String createTable(@RequestBody Query query) {
        try {
            Table table = storage.insertTable(new TableProperties(CreateQueryStringParser.parseTableName(query.getQueryString())));
            table.insertColumns(CreateQueryStringParser.getColumnSpecs(query.getQueryString()));
        } catch (DuplicateEntryException e) {
            e.printStackTrace();
        }
        return "A table was created from this" + query.getQueryString() + "String";

    }

    @GetMapping("/select")
    public List<List<String>> getResultOfQuery(@RequestParam Query query) {
        System.out.println(query.getQueryString());
        return null;
    }

    @PostMapping("/insert")
    public List<String> addRecord(@RequestParam Query query) {
        System.out.println(query.getQueryString());
        return null;
    }

    @PutMapping("/update")
    public List<String> updateRecord(@RequestParam Query query) {
        System.out.println(query.getQueryString());
        return null;
    }
}
