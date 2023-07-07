package com.polaris.lesscode.dc.internal.dsl;

import com.polaris.lesscode.dc.internal.enums.ResultCode;
import com.polaris.lesscode.exception.BusinessException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Alter {

    private Table table;

    private List<AlterAction> actions;

    public static Alter table(Table table){
        Alter alter = new Alter();
        alter.setTable(table);
        return alter;
    }

    public Alter actions(List<AlterAction> actions){
        if (this.actions == null){
            this.actions = new ArrayList<>();
        }
        if (! CollectionUtils.isEmpty(actions)){
            this.actions.addAll(actions);
        }
        return this;
    }

    public Alter actions(AlterAction... actions){
        if (this.actions == null){
            this.actions = new ArrayList<>();
        }
        this.actions.addAll(Arrays.asList(actions));
        return this;
    }

    private void assertConfig(){
        if (table == null){
            throw new BusinessException(ResultCode.ALTER_NOT_TABLE);
        }
        if (CollectionUtils.isEmpty(actions)){
            throw new BusinessException(ResultCode.ALTER_NOT_ACTION);
        }
    }

    public Sql toSql() {
        assertConfig();
        List<Object> args = new ArrayList<>();
        String builder = "ALTER TABLE " +
                table.toSql(args) +
                " " +
                StringUtils.join(actions.stream().map(AlterAction::toSql).toArray(), ", ") +
                ";";
        return new Sql(builder, args);
    }
}
