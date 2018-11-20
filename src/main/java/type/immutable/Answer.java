package type.immutable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Answer {
	@Expose
	@SerializedName("type")
	public String type = "";
	@Expose
	@SerializedName("subType")
	public String subType = "";
	@Expose
	@SerializedName("value")
	public String value = "";
	@Expose
	@SerializedName("data")
	public ArrayList<Object> datas = new ArrayList<>();
	@Expose
	@SerializedName("extendData")
	public String extendData = "";
	
	public Answer(Answer ans) {
		this.type = ans.type;
		this.subType = ans.subType;
		this.value = ans.value;
		this.datas = (ArrayList<Object>) ans.datas.clone();
		this.extendData = ans.extendData;
	}
	
	public Answer(String value) {
		this.type = "text";
		this.subType = "text";
		this.value = value;
	}
	
	public Answer(String type, String subType, String value) {
		this.type = type;
		this.subType = subType;
		this.value = value;
	}
	
	public Answer(String type, String subType, String value, List<String> datas) {
		this.type = type;
		this.subType = subType;
		this.value = value;
		this.datas.addAll(datas);
	}
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
