package channel.enums;

public enum BaseResultEnums {
    SUCCESS("10000","success"),
    BAD_REQUEST("10001","bad request"),
    SERVER_ERROR("10003","server error"),
    UNIQUE_ID("10004","channelId exist")
    ;


    private String code;
    private String msg;
    BaseResultEnums(String code, String msg){
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
