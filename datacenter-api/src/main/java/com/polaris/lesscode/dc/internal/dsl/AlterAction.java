package com.polaris.lesscode.dc.internal.dsl;

import com.polaris.lesscode.dc.internal.enums.ResultCode;
import com.polaris.lesscode.exception.BusinessException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class AlterAction {

    private String action;

    private String resourceType;

    private String resource;

    private String more;

    public static AlterAction addColumn(String column, String more){
        AlterAction alterAction = new AlterAction();
        alterAction.action = "ADD";
        alterAction.resourceType = "COLUMN";
        alterAction.resource = column;
        alterAction.more = more;
        return alterAction;
    }

    public static AlterAction dropColumn(String column, String more){
        AlterAction alterAction = new AlterAction();
        alterAction.action = "DROP";
        alterAction.resourceType = "COLUMN";
        alterAction.resource = column;
        alterAction.more = more;
        return alterAction;
    }

    public static AlterAction alterColumn(String column, String more){
        AlterAction alterAction = new AlterAction();
        alterAction.action = "ALTER";
        alterAction.resourceType = "COLUMN";
        alterAction.resource = column;
        alterAction.more = more;
        return alterAction;
    }

    public void assertConfig(){
        if (StringUtils.isBlank(action)){
            throw new BusinessException(ResultCode.ALTER_NOT_ACTION);
        }
        if (StringUtils.isBlank(resourceType) || StringUtils.isBlank(resource)){
            throw new BusinessException(ResultCode.ALTER_NOT_RESOURCE);
        }
    }

    public String toSql() {
        assertConfig();
        StringBuilder builder = new StringBuilder();
        builder.append(action)
                .append(" ")
                .append(resourceType)
                .append(" ")
                .append(resource)
                .append(" ");
        if (StringUtils.isNotBlank(more)){
            builder.append(more);
        }
        return builder.toString();
    }

}
