package com.mastery.aplsql.Datastorage;

import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.TableProperties;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
@Data
public class Storage {
    private HashMap<TableProperties, Table> DB = new HashMap<>();


}
