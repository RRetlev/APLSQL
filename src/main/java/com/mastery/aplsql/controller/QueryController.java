package com.mastery.aplsql.controller;

import com.mastery.aplsql.Datastorage.Storage;
import com.mastery.aplsql.exceptionhandling.DuplicateEntryException;
import com.mastery.aplsql.exceptionhandling.EntityNotFoundException;
import com.mastery.aplsql.exceptionhandling.MalformedQueryException;
import com.mastery.aplsql.exceptionhandling.TypeMismatchException;
import com.mastery.aplsql.model.Query;
import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.TableProperties;
import com.mastery.aplsql.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@Slf4j
public class QueryController {

    Storage storage = new Storage();
    Storage temporaryStorage;

    @GetMapping("/isworking")
    public Boolean isWorking() {
        return true;
    }

    @ModelAttribute
    public void createStorageSnapshot(){
        temporaryStorage = new Storage(storage);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTable(@RequestBody Query query) throws DuplicateEntryException, MalformedQueryException {
        String tableName = CreateQueryStringParser.parseTableName(query.getQueryString());
        HashMap<String, String> columnSpecs = CreateQueryStringParser.getColumnSpecs(query.getQueryString());
        if (columnSpecs == null) throw new MalformedQueryException();
        storage.insertTable(new TableProperties(tableName)).insertColumns(columnSpecs);
        return new ResponseEntity<>("Table '" + tableName + "' has been created with columns " + columnSpecs.keySet(), HttpStatus.OK);
    }

    @PostMapping("/select")
    public List<List<String>> getResultOfQuery(@RequestBody Query query) throws EntityNotFoundException {
        log.info(query.getQueryString());
        String tableName = SelectQueryStringParser.parseTableName(query.getQueryString());
        List<String> columnNames = SelectQueryStringParser.parseColumnNames(query.getQueryString());
        if (columnNames == null) throw new EntityNotFoundException();
        return storage.getTableByName(tableName).selectRecords(columnNames,QueryStringParser.parseWhereCondition(query.getQueryString()));
    }

    @PostMapping("/insert")
    public ResponseEntity<String> addRecord(@RequestBody Query query) throws EntityNotFoundException, TypeMismatchException, MalformedQueryException {
        log.info(query.getQueryString());
        String tableName = InsertQueryStringParser.parseTableName(query.getQueryString());
        Map<String, String> insertValues = InsertQueryStringParser.parseInsertValues(query.getQueryString());
        Table table = storage.getTableByName(tableName);
        table.insertRecords(insertValues);
        return new ResponseEntity<>(insertValues.values() + " values have been inserted to columns " +
                insertValues.keySet() + " of table '" + tableName + "'.", HttpStatus.OK);
    }

    @PutMapping("/update")
    public List<List<String>> updateRecord(@RequestBody Query query) throws EntityNotFoundException, MalformedQueryException {
        log.info(query.getQueryString());
        return storage.getTableByName(query.getQueryString())
                .updateRecord(UpdateQueryStringParser.getUpdateParameters(
                        query.getQueryString()),
                        QueryStringParser.parseWhereCondition(query.getQueryString()));
    }

    @DeleteMapping("/drop-table")
    public ResponseEntity<String> dropTable(@RequestBody Query query) throws EntityNotFoundException {
        log.info(query.getQueryString());
        String tableName = QueryStringParser.parseTableName(query.getQueryString());
        storage.dropTable(tableName);
        return new ResponseEntity<>("The table '" + tableName + "' has been dropped.", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteRecord(@RequestBody Query query) throws EntityNotFoundException {
        log.info(query.getQueryString());
        Stack stack = new Stack();
        String tablename = QueryStringParser.parseTableName(query.getQueryString());
        Table table = storage.getTableByName(tablename);
        table.deleteRecords(QueryStringParser.parseWhereCondition(query.getQueryString()));
        return new ResponseEntity<>("delete",HttpStatus.OK);
    }
}
