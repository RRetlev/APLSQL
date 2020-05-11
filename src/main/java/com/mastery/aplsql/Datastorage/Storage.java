package com.mastery.aplsql.Datastorage;

import com.mastery.aplsql.exceptionhandling.DuplicateEntryException;
import com.mastery.aplsql.exceptionhandling.EntityNotFoundException;
import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.TableProperties;
import com.mastery.aplsql.service.Util;
import lombok.Data;

import java.util.*;

@Data
public class Storage {
    private HashMap<TableProperties, Table> DB = new HashMap<>();
    private Set<String> tableNames = new HashSet<>();

    public Table insertTable(TableProperties tableProperties) throws DuplicateEntryException {
        if (!Util.containsName(tableNames, tableProperties.getName())) {
            Table table = new Table();
            tableNames.add(tableProperties.getName());
            DB.put(tableProperties, table);
            return table;
        }
        throw new DuplicateEntryException();
    }


    public Table getTableByName(String name) throws EntityNotFoundException {
        return DB.entrySet()
                .stream()
                .filter(tablePropertiesTableEntry -> tablePropertiesTableEntry.getKey().getName().equals(name))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new).getValue();
    }


    public TableProperties getTablePropertiesByName(String name) throws EntityNotFoundException {
        return DB.keySet()
                .stream()
                .filter(tableProperties -> tableProperties.getName().equals(name))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
    }
}
