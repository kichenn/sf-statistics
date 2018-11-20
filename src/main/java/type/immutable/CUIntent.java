package type.immutable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import type.sentence.IMutableSentence;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2017 by Emotibot Corporation. All rights reserved.
 * EMOTIBOT CORPORATION CONFIDENTIAL AND TRADE SECRET
 *
 * Primary Owner: zhipenghao@emotibot.com
 * Secondary Owner:
 */

public class CUIntent {
	
	private final static Gson GSON = new Gson();
    @Expose
    @SerializedName("ver")
    private String version;
    @Expose
    @SerializedName("res")
    private List<IntentRes> res = new ArrayList<>();
    @Expose
    @SerializedName("status")
    private String status;

    public CUIntent() {

    }

    public static String makeDummyCU(final IMutableSentence sentence, Intent intent) {
        String res;
        final String userQ = sentence.getProcessedSentence();
        final String robot = sentence.getRobotId();
        final String userId = sentence.getUserId();
        final String uniqueId = sentence.getUniqueId();

        final CU dummyCU = new CU();
        dummyCU.setText0(userQ);
        dummyCU.setText1(userQ);
        dummyCU.setText1Old(userQ);
        dummyCU.setUserId(userId);
        dummyCU.setUniqueId(uniqueId);
        dummyCU.setRobot(robot);

        Intent customCUIntent = new Intent();
        customCUIntent.setStatus("OK");
        customCUIntent.setVersion("1.0.5");
        CustomCU customCU = new CustomCU("userDefine", customCUIntent);

        AfterRewriteInfo afterRewriteInfo = new AfterRewriteInfo();
        afterRewriteInfo.setIntentZoo(intent.getCUintent());
        List<CustomCU> customCUList = new ArrayList<>();
        customCUList.add(customCU);
        afterRewriteInfo.setCustomCU(customCUList);
        dummyCU.setAfterRewriteInfo(afterRewriteInfo);
        dummyCU.setIntentZoo(intent.getCUintent());
       
        res = GSON.toJson(dummyCU);
        return res;
    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<IntentRes> getRes() {
        return res;
    }

    public void setRes(List<IntentRes> res) {
        this.res = res;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CUIntent)) return false;

        CUIntent intent = (CUIntent) o;

        return new EqualsBuilder()
                .append(version, intent.version)
                .append(res, intent.res)
                .append(status, intent.status)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(version)
                .append(res)
                .append(status)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("version", version)
                .append("res", res)
                .append("status", status)
                .toString();
    }
}