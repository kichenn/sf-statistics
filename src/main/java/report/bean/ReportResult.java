package report.bean;

import report.enums.ReportResultEnums;

public class ReportResult {
    private String code;
    private String msg;
    Object data;



    public ReportResult(ReportResultEnums enums){
        code = enums.getCode();
        msg = enums.getMsg();
    }

    public ReportResult(){

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
