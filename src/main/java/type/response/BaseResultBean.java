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
import org.apache.commons.lang.builder.ToStringBuilder;
import type.immutable.Answer;

import java.util.ArrayList;

public class BaseResultBean extends IResultBean{
	
	@Expose
	@SerializedName("status")
	protected int status = 0;
	
	@Expose
    @SerializedName("message")
	protected String message = "";
	
	@Expose
    @SerializedName("tspan")
	protected int tspan = 0; //耗时(毫秒)
	
	@Expose
	@SerializedName("data")
	protected ArrayList<Answer> datas = new ArrayList<Answer>();
	
	@Expose
	@SerializedName("info")
	protected Info information = null;
	
	@Expose
	@SerializedName("customInfo")
	protected Object customInfo = null;
	
	@Expose
	@SerializedName("extendData")
	protected Object extendData = null;
	
	
    public BaseResultBean() { }


    public void setStatus(StatusType status) {
        this.status = status.value;
    }
    
    public void setMessage(String message) {
		this.message = message;
	}
   
    public void setTspan(int tspan) {
		this.tspan = tspan;
	}
    
    public void addAllData(ArrayList<Answer> answers) {
		datas.addAll(answers);
	}
    
    public void addData(Answer answer) {
		datas.add(answer);
	}
    
 
	public void setInformation(Info information) {
		this.information = information;
    }
    public Info getInformation() {
		return information;
	}
    
    public void setCustomInfo(Object customInfo) {
		this.customInfo = customInfo;
	}
    
    public void setExtendData(String extendData) {
		this.extendData = extendData;
	}
    
    public String getMessage() {
		return message;
	}
    
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
