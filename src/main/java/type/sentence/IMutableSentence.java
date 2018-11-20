/*
 * Copyright (c) 2016 by Emotibot Corporation. All rights reserved.
 * EMOTIBOT CORPORATION CONFIDENTIAL AND TRADE SECRET
 *
 * Primary Owner: xiongyaohua@emotibot.com.cn
 * Secondary Owner:
 */
package type.sentence;

import java.util.List;

/**
 * mutable user input sentence.
 */
public interface IMutableSentence extends Sentence {
    /**
     * 渠道id
     * @return
     */
    public String getChannelId();

    /**
     * set raw sentence
     *
     * @param sentence
     */
    public void setRawSentence(String sentence);


    /**
     * set unique id
     *
     * @param uniqueId
     */
    public void setUniqueId(String uniqueId);

    /**
     * set user id
     *
     * @param userId
     */
    public void setUserId(String userId);

    /**
     * set robot id
     *
     * @param robotId
     */
    public void setRobotId(String robotId);

    /**
     * set enterprise id
     *
     * @param enterpriseId
     */
    public void setEnterpriseId(String enterpriseId);

    /**
     * set section id
     *
     * @param sectionId
     */
    public void setSessionId(String sessionId);
    /**
     * set platform
     *
     * @param platform
     */
    public void setDimensionTags(List<String> dimensionTags);

    /**
     * set customInfo
     *
     * @param customInfo
     */
    public void setCustomInfo(Object customInfo);
    /**
     * add sentence semantic feature
     *
     * @param type
     * @param feature
     */
    public void addSentenceFeature(SentenceFeature type, Object feature);

    public void addResponseFeature(ResponseFeature type, Object feature);

    public void setCutShortSignal(boolean signal);

    public void setTotalTime(int totalTime);
    
    public void setMessage(String message);
    
    public void setLastFeature(String feature);

    public void setLastAnswer(String answer);
    
    public String getMessage();

    public void addNote(String note);
	
}
