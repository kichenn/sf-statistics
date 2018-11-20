package type.immutable;

/*
 * Copyright (c) 2017 by Emotibot Corporation. All rights reserved.
 * EMOTIBOT CORPORATION CONFIDENTIAL AND TRADE SECRET
 *
 * Primary Owner: zhipenghao@emotibot.com
 * Secondary Owner:
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class CU {
    @Expose
    @SerializedName("Text0")
    private String text0;
    @Expose
    @SerializedName("Text1")
    private String text1;
    @Expose
    @SerializedName("Text1_Old")
    private String text1Old;
    @Expose
    @SerializedName("UserID")
    private String userId;
    @Expose
    @SerializedName("UniqueID")
    private String uniqueId;
    @Expose
    @SerializedName("robot")
    private String robot;

    @Expose
    @SerializedName("AfterRewriteInfo")
    private AfterRewriteInfo afterRewriteInfo;

    @Expose
    @SerializedName("intent_zoo")
    private CUIntent intentZoo;
    
    @Expose
    @SerializedName("CustomCU")
    protected List<CustomCU> customCU = new ArrayList<>();

    public CU() {

    }

    public CU(String text0, String text1, String text1Old, String userId, String uniqueId, String robot, AfterRewriteInfo afterRewriteInfo, List<CustomCU> customCU) {
        this.text0 = text0;
        this.text1 = text1;
        this.text1Old = text1Old;
        this.userId = userId;
        this.uniqueId = uniqueId;
        this.robot = robot;
        this.afterRewriteInfo = afterRewriteInfo;
        this.customCU = customCU;
    }

    public String getText0() {
        return text0;
    }

    public void setText0(String text0) {
        this.text0 = text0;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText1Old() {
        return text1Old;
    }

    public void setText1Old(String text1Old) {
        this.text1Old = text1Old;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getRobot() {
        return robot;
    }

    public void setRobot(String robot) {
        this.robot = robot;
    }

    public AfterRewriteInfo getAfterRewriteInfo() {
        return afterRewriteInfo;
    }

    public void setAfterRewriteInfo(AfterRewriteInfo afterRewriteInfo) {
        this.afterRewriteInfo = afterRewriteInfo;
    }

    public void setCustomCU(List<CustomCU> customCU) {
        this.customCU = customCU;
    }

    public List<CustomCU> getCustomCU() {
        return customCU;
    }
    public void setIntentZoo(CUIntent intentZoo) {
		this.intentZoo = intentZoo;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CU)) return false;

        CU cu = (CU) o;

        return new EqualsBuilder()
                .append(text0, cu.text0)
                .append(text1, cu.text1)
                .append(text1Old, cu.text1Old)
                .append(userId, cu.userId)
                .append(uniqueId, cu.uniqueId)
                .append(robot, cu.robot)
                .append(afterRewriteInfo, cu.afterRewriteInfo)
                .append(customCU, cu.customCU)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(text0)
                .append(text1)
                .append(text1Old)
                .append(userId)
                .append(uniqueId)
                .append(robot)
                .append(afterRewriteInfo)
                .append(customCU)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("text0", text0)
                .append("text1", text1)
                .append("text1Old", text1Old)
                .append("userId", userId)
                .append("uniqueId", uniqueId)
                .append("robot", robot)
                .append("afterRewriteInfo", afterRewriteInfo)
                .append("customCU", customCU)
                .toString();
    }
}
