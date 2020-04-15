package com.mastery.aplsql;

import com.mastery.aplsql.Datastorage.Storage;
import com.mastery.aplsql.model.Column;
import com.mastery.aplsql.model.ColumnProperties;
import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.TableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class AplsqlApplication {

    @Autowired
    private Storage storage;

    public static void main(String[] args) {
        SpringApplication.run(AplsqlApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(){
        return args -> {
            String record = "alma";
            ColumnProperties clp = new ColumnProperties("col",true);
            TableProperties tp = new TableProperties("table","pk");
            Column column = new Column();
            Table table = new Table();
            storage.getDB().put(tp, table);
            table.getColumns().put(clp,column);
            column.getData().add(record);
            System.out.println(storage.getDB().get(tp).getColumns().get(clp).getData().get(0));
        };
    }



}
