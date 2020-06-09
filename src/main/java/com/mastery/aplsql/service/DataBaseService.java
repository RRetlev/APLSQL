package com.mastery.aplsql.service;

import com.mastery.aplsql.Datastorage.Storage;
import com.mastery.aplsql.exceptionhandling.DuplicateEntryException;
import com.mastery.aplsql.exceptionhandling.EntityNotFoundException;
import com.mastery.aplsql.model.ColumnProperties;
import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.TableProperties;
import com.mastery.aplsql.model.Types;
import org.springframework.stereotype.Service;

@Service
public class DataBaseService {


    TableService tableService = new TableService();

    public Table insertTable(Storage storage,TableProperties tableProperties) throws DuplicateEntryException {
        if (!Util.containsName(storage.getTableNames(), tableProperties.getName())) {
            Table table = new Table();
            storage.getTableNames().add(tableProperties.getName());
            storage.getDB().put(tableProperties, table);
            tableService.insertColumn(table,new ColumnProperties("id", Types.INTEGER));
            return table;
        }
        throw new DuplicateEntryException();
    }

    public void dropTable(Storage storage,String name) throws EntityNotFoundException {
        storage.getDB().remove(getTablePropertiesByName(storage,name));
        storage.getTableNames().remove(name);
    }

    public Table getTableByName(Storage storage,String name) throws EntityNotFoundException {
        return storage.getDB().entrySet()
                .stream()
                .filter(tablePropertiesTableEntry -> tablePropertiesTableEntry.getKey().getName().equals(name))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new).getValue();
    }


    public TableProperties getTablePropertiesByName(Storage storage,String name) throws EntityNotFoundException {
        return storage.getDB().keySet()
                .stream()
                .filter(tableProperties -> tableProperties.getName().equals(name))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
    }

}
