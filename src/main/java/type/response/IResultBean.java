package type.response;

import type.immutable.Answer;
import type.immutable.FAQQuestion;
import type.response.BaseResultBean.StatusType;

import java.util.ArrayList;

public class IResultBean {
	public void setStatus(StatusType status) {}
	    
	public void setMessage(String message) {}
	   
	public void setTspan(int tspan) {}
	    
	public void addAllData(ArrayList<Answer> answers) {}
	    
	public void addData(Answer answer) {}
	   
	public void setInformation(Info information) {}
	public Info getInformation() {return null;}
	    
	public void setCustomInfo(Object customInfo) {}
	    
	public void setExtendData(Object extendData) {}
	
	public void setRelatedQuestions(ArrayList<FAQQuestion> relatedQuestions) {}

	public String getMessage() {
		return null;}
}
