package com.mastery.aplsql.Datastorage;

import com.mastery.aplsql.exceptions.DuplicateEntryException;
import com.mastery.aplsql.exceptions.EntityNotFoundException;
import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.TableProperties;
import com.mastery.aplsql.service.Util;
import lombok.Data;

import java.util.*;

@Data
public class Storage {
    private HashMap<TableProperties, Table> DB = new HashMap<>();
    private Set<String> tableNames = new HashSet<>();

    public Table insertTable(String tableName, String tablePrimaryKey) throws DuplicateEntryException {
        if (!Util.containsName(tableNames, tableName)) {
            TableProperties tableProperties = new TableProperties(tableName, tablePrimaryKey);
            Table table = new Table();
            tableNames.add(tableName);
            DB.put(tableProperties, table);
            return table;
        }
        throw new DuplicateEntryException();
    }

    public Table insertTable(String tableName) throws DuplicateEntryException {
        if (!Util.containsName(tableNames, tableName)) {
            TableProperties tableProperties = new TableProperties(tableName);
            Table table = new Table();
            tableNames.add(tableName);
            DB.put(tableProperties, table);
            return table;
        }
        throw new DuplicateEntryException();
    }

    public Table getTableByName(String name) throws EntityNotFoundException {
        for (Map.Entry<TableProperties, Table> entry :
                DB.entrySet())
            if ((entry.getKey().getName()).equals(name)) {
                return entry.getValue();
            }
        throw new EntityNotFoundException();
    }


    public TableProperties getTablePropertiesByName(String name) throws EntityNotFoundException {
        for (TableProperties tp : DB.keySet()) {
            if (tp.getName().equals(name)) {
                return tp;
            }
        }
        throw new EntityNotFoundException();
    }
}
