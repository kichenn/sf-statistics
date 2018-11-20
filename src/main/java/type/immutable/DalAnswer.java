package type.immutable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;


public class DalAnswer {
	public String errno;
	public ArrayList<DalAnswerActualResult> actualResults;
	
	//注解无用 减少parse时间
	public class DalAnswerActualResult {
		//public String userRecordId;
		public String appid;
		//public boolean istemplate;
		public int parentRecordId;
		public String content;
		//public String space;
		//public long timestamp;
		//public long modifiedMs;
		public int recordId;
		public String endtime;
		public String starttime;
		public ArrayList<DalAnswerDimension> dimension = new ArrayList<DalAnswerDimension>();
		public String labels;
		public ArrayList<Integer> relatedQuestions = new ArrayList<Integer>();
		@Override
	    public String toString() {
	        return new ToStringBuilder(this)
	               // .append("userRecordId", userRecordId)
	                .append("appid", appid)
	               // .append("istemplate", istemplate)
	               // .append("parentRecordId", parentRecordId)
	                .append("content", content)
	               // .append("space", space)
	               // .append("timestamp", timestamp)
	               // .append("modifiedMs", modifiedMs)
	               // .append("recordId", recordId)
	                .append("endtime", endtime)
	                .append("starttime", starttime)
	                .append("dimension", dimension.toString())
	                .toString();
	    }
	}
	
	public class DalAnswerDimension {
		public int dimensionid;
		public ArrayList<Integer> data = new ArrayList<Integer>();
		public String dimensionname;
		@Override
	    public String toString() {
	        return new ToStringBuilder(this)
	                .append("dimensionid", dimensionid)
	                .append("data", data.toString())
	                .append("dimensionname", dimensionname)
	                .toString();
	    }
	}
	@Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("errno", errno)
                .append("actualResults", actualResults.toString())
                .toString();
    }
}
