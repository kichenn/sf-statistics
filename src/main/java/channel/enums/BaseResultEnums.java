package channel.enums;

public enum BaseResultEnums {
    SUCCESS("10000","success"),
    BAD_REQUEST("10001","bad request"),
    SERVER_ERROR("10003","server error"),
    UNIQUE_ID("10004","channelId exist or server error"),
    EXIST_ID("10005","channelId not exist or server error"),
    HANDLE_CONSUL_ERROR("10006","handle consul error")
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
