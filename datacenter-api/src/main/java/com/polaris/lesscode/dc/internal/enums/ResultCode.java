package com.polaris.lesscode.dc.internal.enums;

import com.polaris.lesscode.vo.AbstractResultCode;

public enum ResultCode implements AbstractResultCode{
    OK(0,"OK"),
    TOKEN_ERROR(401,"token错误"),
    FORBIDDEN_ACCESS(403,     "无权访问"),
    PATH_NOT_FOUND(404,   "请求地址不存在"),
    PARAM_ERROR(501,"请求参数错误"),
    INTERNAL_SERVER_ERROR(500, "服务器异常"),
    SYS_ERROR_MSG(996,  "系统异常, %s "),
    FAILURE(997,  "业务失败"),
    SYS_ERROR(998,    "系统异常"),
    UNKNOWN_ERROR(999,  "未知错误"),
    SOCKET_TIMEOUT_ERROR(1000,"网络超时请稍后再试"),
    UPDATA_SUCCESS(200,   "更新成功"),
    UPDATA_FAIL(100010,"更新失败"),
    INTERNAL_SERVICE_ERROR(150000,  "内部服务异常"),

    //Data Center
    NO_AVAILABLE_DATASOURCE(700001,"没有可用的数据源"),
    NO_AVAILABLE_DATABASE(700002, "没有可用的数据库"),
    CURRENT_DATASOURCE_DISABLE(700003,   "当前数据源不可用，请联系客服"),
    CURRENT_DATABASE_DISABLE(700004, "当前数据库不可用，请联系客服"),
    STAGE_NOT_EXIST(700005,    "阶段不存在"),
    DATA_QUERY_ERROR(700006,     "数据查询异常"),
    QUERY_NOT_FROM(700007,     "查询没有指定表名"),
    ALTER_NOT_TABLE(700008,     "变动没有指定表"),
    ALTER_NOT_ACTION(700009,     "变动没有指定动作"),
    ALTER_NOT_RESOURCE(700010,     "变动没有指定资源对象"),

    // dsl
    INVALID_EXECUTOR_TYPE(700101,     "无效的执行器类型"),
    INVALID_EXECUTOR_SETS_IS_EMPTY(700102,     "更新条件为空"),
    INVALID_EXECUTOR_VALUES_IS_EMPTY(700103,     "插入数据为空"),
    JSON_PATH_DEPTH_NOT_SUPPORT(700104,     "Json嵌套深度暂无法支持"),

    ;


    private ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResultCode parse(int code) {
        for (ResultCode rc : values()) {
            if (rc.getCode() == code) {
                return rc;
            }
        }
        return SYS_ERROR;
    }

    public boolean equals(Integer code) {
        return Integer.valueOf(this.getCode()).equals(code);
    }


}
