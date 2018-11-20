package type.immutable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;


public class DalSQ {
	public String errno;
	public ArrayList<ActualResult> actualResults;
	
	public class ActualResult {	
		//用不到先注解
		//public String userRecordId;
		//public String appid;
		public String content;
		//public String space;
		//public long timestamp;
		//public long modifiedMs;
		//public int recordId;
		public String labels;
	}
	@Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("errno", errno)
                .append("actualResults", actualResults.toString())
                .toString();
    }
}
