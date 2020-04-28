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

    public Table insertTable(TableProperties tableProperties) throws DuplicateEntryException {
        if (!Util.containsName(tableNames, tableProperties.getName())) {
            Table table = new Table();
            tableNames.add(tableProperties.getName());
            DB.put(tableProperties, table);
            return table;
        }
        throw new DuplicateEntryException("Table name:"+ tableProperties.getName()+"is already present in the Database");
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
