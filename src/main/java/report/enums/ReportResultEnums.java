package report.enums;

public enum  ReportResultEnums {
    SUCCESS("0000","success"),
    DATE_PARSE_EXCEPTION("1000","date format error"),
    DATE_SPAN_TOO_LONG("1001","date span too long"),
    REQUEST_TOO_FREQUENT("1002","don't request too frequent")

    ;


    private String code;
    private String msg;
    ReportResultEnums(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
