package type.response;
/*
 * Copyright (c) 2017 by Emotibot Corporation. All rights reserved.
 * EMOTIBOT CORPORATION CONFIDENTIAL AND TRADE SECRET
 *
 * Primary Owner: zhipenghao@emotibot.com
 * Secondary Owner:
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShunfengResultBean extends IResultBean{

	@Expose
	@SerializedName("code")
	private int code = 0;

	@Expose
	@SerializedName("errormsg")
	private String errormsg;

	@Expose
	@SerializedName("type")
	private int type=1;
	@Expose
	@SerializedName("message")
	private String message;
	@Expose
	@SerializedName("relatedQuestions")
	private List<String> relatedQuestions;
	@Expose
	@SerializedName("context")
	private String context;

	@Expose
	@SerializedName("isNeedEvaluate")
	private boolean isNeedEvaluate = false;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getRelatedQuestions() {
		return relatedQuestions;
	}

	public void setRelatedQuestions(List<String> relatedQuestions) {
		this.relatedQuestions = relatedQuestions;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public boolean isNeedEvaluate() {
		return isNeedEvaluate;
	}

	public void setNeedEvaluate(boolean needEvaluate) {
		isNeedEvaluate = needEvaluate;
	}

	public enum StatusType {
		SUCCESS(200), 
		FAIL(400); 
		
		public int value;
	    private StatusType(int value) {
	        this.value = value;
	    }
	}
}
