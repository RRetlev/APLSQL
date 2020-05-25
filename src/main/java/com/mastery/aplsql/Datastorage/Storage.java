package com.mastery.aplsql.Datastorage;

import com.mastery.aplsql.exceptionhandling.DuplicateEntryException;
import com.mastery.aplsql.exceptionhandling.EntityNotFoundException;
import com.mastery.aplsql.model.ColumnProperties;
import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.TableProperties;
import com.mastery.aplsql.model.Types;
import com.mastery.aplsql.service.Util;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class Storage {
    private HashMap<TableProperties, Table> DB = new HashMap<>();
    private List<String> tableNames = new ArrayList<>();

    public Table insertTable(TableProperties tableProperties) throws DuplicateEntryException {
        if (!Util.containsName(tableNames, tableProperties.getName())) {
            Table table = new Table();
            tableNames.add(tableProperties.getName());
            DB.put(tableProperties, table);
            table.insertColumn(new ColumnProperties("id", Types.INTEGER));
            return table;
        }
        throw new DuplicateEntryException();
    }

    public void dropTable(String name) throws EntityNotFoundException {
        DB.remove(getTablePropertiesByName(name));
        tableNames.remove(name);
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
