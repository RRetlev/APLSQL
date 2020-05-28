package com.mastery.aplsql.Datastorage;

import com.mastery.aplsql.exceptionhandling.DuplicateEntryException;
import com.mastery.aplsql.exceptionhandling.EntityNotFoundException;
import com.mastery.aplsql.model.ColumnProperties;
import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.TableProperties;
import com.mastery.aplsql.model.Types;
import com.mastery.aplsql.service.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Storage {
    private HashMap<TableProperties, Table> DB = new HashMap<>();
    private List<String> tableNames = new ArrayList<>();

    public Storage(Storage that){
        this.DB = that.copyOfDB(that.getDB());
        this.tableNames = that.copyOfTableNames(that.getTableNames());
//        this(that.copyOfDB(that.getDB()),that.copyOfTableNames(that.getTableNames()));
    }

    private HashMap<TableProperties, Table> copyOfDB(HashMap<TableProperties, Table> DB){
        HashMap<TableProperties, Table> copiedDB = new HashMap<>();
        DB.forEach(copiedDB::put);
        return copiedDB;
    }

    private List<String> copyOfTableNames(List<String> tableNames){
        return new ArrayList<>(tableNames);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Storage storage = (Storage) o;
        return Objects.equals(DB, storage.DB) &&
                Objects.equals(tableNames, storage.tableNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(DB, tableNames);
    }

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
