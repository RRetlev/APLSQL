package com.mastery.aplsql.controller;

import com.mastery.aplsql.Datastorage.Storage;
import com.mastery.aplsql.exceptionhandling.DuplicateEntryException;
import com.mastery.aplsql.exceptionhandling.EntityNotFoundException;
import com.mastery.aplsql.exceptionhandling.TypeMismatchException;
import com.mastery.aplsql.model.Query;
import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.TableProperties;
import com.mastery.aplsql.service.CreateQueryStringParser;
import com.mastery.aplsql.service.InsertQueryStringParser;
import com.mastery.aplsql.service.SelectQueryStringParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
public class QueryController {

    private Storage storage = new Storage();

    @GetMapping("/isworking")
    public Boolean isWorking() {
        return true;
    }

    @PostMapping("/create")
    public String createTable(@RequestBody Query query) throws DuplicateEntryException {
        Table table = storage.insertTable(new TableProperties(CreateQueryStringParser.parseTableName(query.getQueryString())));
        table.insertColumns(CreateQueryStringParser.getColumnSpecs(query.getQueryString()));
        return "A table was created from this " + query.getQueryString() + " String";
    }

    @PostMapping("/select")
    public List<List<String>> getResultOfQuery(@RequestBody Query query) throws EntityNotFoundException {
        log.info(query.getQueryString());
        String tableName = SelectQueryStringParser.parseTableName(query.getQueryString());
        List<String> columnNames = SelectQueryStringParser.parseColumnNames(query.getQueryString());
        log.info("table name: " + tableName + "; column names: " + columnNames.toString());
        return storage.getTableByName(tableName).selectRecords(columnNames);
        // TODO : Create response entity.
    }

    @PostMapping("/insert")
    public List<String> addRecord(@RequestBody Query query) throws EntityNotFoundException, TypeMismatchException {
        log.info(query.getQueryString());
        Table table = storage.getTableByName(InsertQueryStringParser.parseTableName(query.getQueryString()));
        table.insertRecords(InsertQueryStringParser.parseInsertValues(query.getQueryString()));
        return null;
    }

    @PutMapping("/update")
    public List<String> updateRecord(@RequestBody Query query) {
        System.out.println(query.getQueryString());
        return null;
    }
}
