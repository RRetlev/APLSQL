package com.mastery.aplsql.Datastorage;

import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.TableProperties;
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
        this.DB = this.copyOfDB(that.getDB());
        this.tableNames = that.getTableNames();
        }


    private HashMap<TableProperties, Table> copyOfDB(HashMap<TableProperties, Table> DB){
        HashMap<TableProperties, Table> copiedDB = new HashMap<>();
        DB.forEach((key, value) -> copiedDB.put(new TableProperties(key), new Table(value)));
        return copiedDB;
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

}
