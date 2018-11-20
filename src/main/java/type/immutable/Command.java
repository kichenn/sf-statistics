package type.immutable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import log.LoggerFactory;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Date;


public class Command {
	public String name;
	public int target; //0：标准问, 1: 出话内容
	public ArrayList<Answer> answers = new ArrayList<Answer>(); // json 参考Answer.java
	public int responseType; // 0：取代, 1：附加至前, 2：附加至后
	public Date beginTime;
	public Date endTime;
	
	public ArrayList<String> keywords = new ArrayList<String>();
	public ArrayList<String> regexes = new ArrayList<String>();
	
	public Command(String name, int target, String rule, String answerContent, int responseType, Date beginTime, Date endTime){
		this.name = name;
		this.target = target;
		
		Gson gson = new Gson();
		
		//parser rule
		try {
			JsonArray jsonArray = new JsonParser().parse(rule).getAsJsonArray();
			for(JsonElement jsonElement: jsonArray) {
				RuleStatement ruleStatement = gson.fromJson(jsonElement, RuleStatement.class);
				if(ruleStatement.value == null || ruleStatement.value.isEmpty()) continue;
				if("keyword".equals(ruleStatement.type)) {
					keywords.addAll(ruleStatement.value);
				} else if("regex".equals(ruleStatement.type)){
					regexes.addAll(ruleStatement.value);
				}
			}
		} catch (Exception e) {
			LoggerFactory.getLogger().warn("\""+rule+"\"解析失败");
		}
		
		//parser answerContent
		try {
			JsonArray jsonArray = new JsonParser().parse(answerContent).getAsJsonArray();
			for(JsonElement jsonElement: jsonArray) {
				answers.add(gson.fromJson(jsonElement, Answer.class));	
			}
		} catch (Exception e) {
			answers.add(new Answer(answerContent));
		}
		
		//
		this.responseType = responseType;
		this.beginTime = beginTime;
		this.endTime = endTime;
	}
	
	public boolean isEffectiveTime() {
		if(beginTime == null || endTime == null) return true; //永久
		long now = new Date().getTime();
		return now - beginTime.getTime() >= 0 && endTime.getTime() - now >= 0;
	}
	
	public boolean isMatch (String question, ArrayList<Answer> answers) {
		if(!isEffectiveTime()) return false;
		//触发对象
		ArrayList<String> targetStrings = new ArrayList<String>();
		//for question
		if(target == TargetType.QUESTION.value) {
			targetStrings.add(question);
		} else {
		//for answer 
			for(Answer answer: answers) {
				targetStrings.add(answer.value);
			}
		}
		
		//触发条件：keyword, 每个关键字要全部包含才算match, 多句target时，只要有一笔命中就算
		boolean isMatchKeyword = true;
		for(String keyword: keywords) {
			boolean match = false;
			for(String str: targetStrings) {
				if(str.contains(keyword)) {
					match = true;
					break;
				}
			}
			isMatchKeyword &= match;
		}
		if(keywords.size() > 0 && isMatchKeyword) return true;
		
		//触发条件：正则, 每个正则要全不符合才算match, 多句target时，只要有一笔符合就算
		boolean isMatchRegular = true;
		for(String regex: regexes) {
			boolean match = false;
			for(String str: targetStrings) {
				if(str.matches(regex)) {
					match = true;
					break;
				}
			}
			isMatchRegular &= match;
		}
		if(regexes.size() > 0 && isMatchRegular) return true;
		return false;
	}
	
	//若已经符合该rule, 产生答案
	public ArrayList<Answer> getRuleAnswer(ArrayList<Answer> originAnswers) {
		if(answers.size() <= 0 || answers.get(0).value.isEmpty()) {
			return originAnswers;
		}
		//取代答案
		if(responseType == ResponseType.REPLACE.value) {
			originAnswers.clear();
			originAnswers.addAll(answers);
			return originAnswers;
		} else if(responseType == ResponseType.FIRST.value) {
			//加至最前
			originAnswers.addAll(0, answers);
		} else if(responseType == ResponseType.LAST.value) {
			//加至最后
			originAnswers.addAll(answers);
		}
		return originAnswers;
	}
	
	
	class RuleStatement {
		String type;
		ArrayList<String> value;
		@Override
	    public String toString() {
	        return new ToStringBuilder(this)
	                .append("type", type)
	                .append("value", value)  
	                .toString();
	    }
	}
	
	public enum TargetType {
		QUESTION(0), 
		ANSWER(1); 
		public int value;
	    private TargetType(int value) {
	        this.value = value;
	    }
	}
	
	public enum ResponseType {
		REPLACE(0), 	
		FIRST(1),
		LAST(2);
		public int value;
	    private ResponseType(int value) {
	        this.value = value;
	    }
	}
}
