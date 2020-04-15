package com.mastery.aplsql.Datastorage;

import com.mastery.aplsql.model.Column;
import com.mastery.aplsql.model.ColumnProperties;
import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.TableProperties;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Data
public class Storage {
    private HashMap<TableProperties, Table> DB = new HashMap<>();

    public void insertTable(String tableName,String tablePrimaryKey){
        TableProperties tableProperties = new TableProperties(tableName,tablePrimaryKey);
        Table table = new Table();
        DB.put(tableProperties,table);
    }

    public Table getTableByName(String name){
        for (Map.Entry<TableProperties, Table> entry :
                DB.entrySet())
            if ((entry.getKey().getName().toString()).equals(name)) {
                return entry.getValue();
            }
        return null;
    }


}
