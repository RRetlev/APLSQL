package com.mastery.aplsql.controller;

import com.mastery.aplsql.Datastorage.Storage;
import com.mastery.aplsql.exceptionhandling.DuplicateEntryException;
import com.mastery.aplsql.exceptionhandling.EntityNotFoundException;
import com.mastery.aplsql.exceptionhandling.MalformedQueryException;
import com.mastery.aplsql.exceptionhandling.TypeMismatchException;
import com.mastery.aplsql.model.Query;
import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.TableProperties;
import com.mastery.aplsql.service.DataBaseService;
import com.mastery.aplsql.service.QueryStringParser;
import com.mastery.aplsql.service.TableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@Slf4j
public class QueryController {

    @Autowired
    TableService tableService;

    @Autowired
    DataBaseService dataBaseService;

    Storage storage = new Storage();
    Storage temporaryStorage;

    @GetMapping("/isworking")
    public Boolean isWorking() {
        return true;
    }

    @ModelAttribute
    public void createStorageSnapshot() {
        temporaryStorage = new Storage(storage);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTable(@RequestBody Query query) throws DuplicateEntryException, MalformedQueryException {
        String tableName = QueryStringParser.parseTableName(query.getQueryString());
        HashMap<String, String> columnSpecs = QueryStringParser.getColumnSpecs(query.getQueryString());
        tableService.insertColumns(dataBaseService.insertTable(storage, new TableProperties(tableName)),columnSpecs);
        return new ResponseEntity<>("Table '" + tableName + "' has been created with columns " + columnSpecs.keySet(), HttpStatus.OK);
    }

    @PostMapping("/select")
    public List<List<String>> getResultOfQuery(@RequestBody Query query) throws EntityNotFoundException, MalformedQueryException {
        log.info(query.getQueryString());
        String tableName = QueryStringParser.parseTableName(query.getQueryString());
        List<String> columnNames = QueryStringParser.parseColumnNames(query.getQueryString());
        return tableService.selectRecords(dataBaseService.getTableByName(storage, tableName), columnNames, QueryStringParser.parseWhereCondition(query.getQueryString()));
    }

    @PostMapping("/insert")
    public ResponseEntity<String> addRecord(@RequestBody Query query) throws EntityNotFoundException, TypeMismatchException, MalformedQueryException {
        log.info(query.getQueryString());
        String tableName = QueryStringParser.parseTableName(query.getQueryString());
        Map<String, String> insertValues = QueryStringParser.parseInsertValues(query.getQueryString());
        Table table = dataBaseService.getTableByName(storage, tableName);
        tableService.insertRecords(table, insertValues);
        return new ResponseEntity<>(insertValues.values() + " values have been inserted to columns " +
                insertValues.keySet() + " of table '" + tableName + "'.", HttpStatus.OK);
    }

    @PutMapping("/update")
    public List<List<String>> updateRecord(@RequestBody Query query) throws EntityNotFoundException, MalformedQueryException, TypeMismatchException {
        log.info(query.getQueryString());
        return tableService.updateRecord(
                dataBaseService.getTableByName(storage, query.getQueryString()),
                    QueryStringParser.getUpdateParameters(query.getQueryString()),
                    QueryStringParser.parseWhereCondition(query.getQueryString()
                )
        );
    }

    @DeleteMapping("/drop-table")
    public ResponseEntity<String> dropTable(@RequestBody Query query) throws EntityNotFoundException, MalformedQueryException {
        log.info(query.getQueryString());
        String tableName = QueryStringParser.parseTableName(query.getQueryString());
        dataBaseService.dropTable(storage,tableName);
        return new ResponseEntity<>("The table '" + tableName + "' has been dropped.", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteRecord(@RequestBody Query query) throws EntityNotFoundException, MalformedQueryException {
        log.info(query.getQueryString());
        String tablename = QueryStringParser.parseTableName(query.getQueryString());
        Table table = dataBaseService.getTableByName(storage,tablename);
        tableService.deleteRecords(table, QueryStringParser.parseWhereCondition(query.getQueryString()));
        return new ResponseEntity<>("delete", HttpStatus.OK);
    }
}
