package type.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShunfengEvaluateResultBean extends IResultBean{

	@Expose
	@SerializedName("code")
	private int code = 0;

	@Expose
	@SerializedName("errormsg")
	private String errormsg;

	@Expose
	@SerializedName("reason")
	private List<String> reason;

	@Expose
	@SerializedName("message")
	private String message;

	@Expose
	@SerializedName("type")
	private int type = 0;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public List<String> getReason() { return reason; }

	public void setReason(List<String> reason) {
		this.reason = reason;
	}

	public String getMessage() { return message; }

	public void setMessage(String message) { this.message = message; }

	public int getType() { return type; }

	public void setType(int type) { this.type = type; }

	public enum StatusType {
		SUCCESS(200), 
		FAIL(400); 
		
		public int value;
	    private StatusType(int value) {
	        this.value = value;
	    }
	}
}
