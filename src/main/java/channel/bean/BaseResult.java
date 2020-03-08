package channel.bean;


import channel.enums.BaseResultEnums;

/**
 * @Author: zujikang
 * @Date: 2020-03-06 15:03
 */
public class BaseResult {
    private String code;
    private String msg;
    Object data;



    public BaseResult(BaseResultEnums enums){
        code = enums.getCode();
        msg = enums.getMsg();
    }

    public BaseResult(){

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
