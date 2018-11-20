/*
 * Copyright (c) 2016 by Emotibot Corporation. All rights reserved.
 * 
 * EMOTIBOT CORPORATION CONFIDENTIAL AND TRADE SECRET
 *
 * Primary Owner: xiongyaohua@emotibot.com.cn
 * 
 * Secondary Owner:
 */
package type.sentence;

import com.google.gson.Gson;
import type.immutable.Answer;
import type.response.BaseResultBean.StatusType;
import type.response.IResultBean;
import type.response.Info;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * base implementation of sentence for concurrency.
 *
 * @ThreadSafe
 */
public class BaseSentence implements IMutableSentence {

    protected static final Gson GSON = new Gson();

    /*
     * the created time stamp
     */
    protected long timestamp;

    /* raw sentence */
    protected String rawSentence;

    /* user id */
    protected String userId = null;

    /* unique id */
    protected String uniqueId = null;

    /* robot id */
    protected String robotId = null;

    /* enterprise id */
    protected String enterpriseId = null;
    
    /* section id */
    protected String sessionId = null;

    /* totalTime */
    protected int totalTime = 0;

    /* message */
    protected String message = null;

    /* cutShortSignal */
    protected boolean cutShortSignal = false;

    /* platform */
    protected List<String> dimensionTags = null;

    /* customInfo */
    protected Object customInfo = null;

    protected Map<String, Object> extendDataMap = new HashMap<String, Object>();

    /* 
    * nBest is a spec for audio ai server
    * NLU tde module need more accuracy audio to text from ASR module
    * so ASR give every possbile result with score for tde to use.
    */
    protected SortedSet<NBest> nBest = null;

    /* sentence features */
    protected final ConcurrentMap<SentenceFeature, Object> sentenceFeatures = new ConcurrentHashMap<SentenceFeature, Object>();

    /* sentence features */
    protected final ConcurrentMap<ResponseFeature, Object> responseFeatures = new ConcurrentHashMap<ResponseFeature, Object>();

    private String lastAnswer = null;

    private String lastFeature = null;

    private HashSet<String> finishModule = new HashSet<String>();

    protected StringBuilder note = new StringBuilder();

    protected String channelId;
    
    public BaseSentence(String sentence) {
        super();
        this.rawSentence = sentence;
        this.timestamp = System.currentTimeMillis();
    }

    public void setDimensionTags(List<String> dimensionTags) {
        this.dimensionTags = dimensionTags;
    }

    public List<String> getDimensionTags() {
        return this.dimensionTags;
    }

    public synchronized String getRawSentence() {
        return rawSentence;
    }

    public synchronized void setRawSentence(String question) {
        this.rawSentence = question;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUniqueId() {
        return this.uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getRobotId() {
        return this.robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public String getEnterpriseId() {
        return this.enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setCutShortSignal(boolean signal) {
        this.cutShortSignal = signal;
    }

    public boolean getCutShortSignal() {
        return cutShortSignal;
    }

    public void setCustomInfo(Object customInfo) {
        this.customInfo = customInfo;
    }

    public Object getCustomInfo() {
        return customInfo;
    }

    public void setNBest(Collection<NBest> results) {
        this.nBest = new TreeSet<>(results);
    }

    public SortedSet<NBest> getNBest() {
        return this.nBest;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setLastAnswer(String lastAnswer) {
        this.lastAnswer = lastAnswer;
    }

    public void setLastFeature(String lastFeature) {
        this.lastFeature = lastFeature;
    }

    public String getLastAnswer() {
        return lastAnswer;
    }

    public String getLastFeature() {
        return lastFeature;
    }

    public Object getExtendData(String module) {
    		return extendDataMap.get(module);
    }
    
    public void setExtendData(String module, Object data) {
    		extendDataMap.put(module, data);
    }
   
	public boolean getFinishModule(String module) {
		return finishModule.contains(module);
	}
	public void setFinishModule(String module) {
		finishModule.add(module);
	}
	
	public String getNote() {
		return this.note.toString();
	}
	public void addNote(String note) {
		this.note.append(note).append(",");
	}
	
	
    public String getProcessedSentence() {
        if (containsSentenceFeature(SentenceFeature.SYNONYM_SENTENCE)) {
            String processedSentence = getSentenceFeature(SentenceFeature.SYNONYM_SENTENCE, String.class);
            if (processedSentence != null && !processedSentence.isEmpty()) {
                return processedSentence;
            }
        }
        return getRawSentence();
    }

    public void setResponse(IResultBean resultBean) {

        // set message & status
        if (message == null || message.isEmpty()) {
            resultBean.setMessage("success");
            resultBean.setStatus(StatusType.SUCCESS);
        } else {
            resultBean.setMessage(message);
            resultBean.setStatus(StatusType.FAIL);
        }

        // set answer
        if (responseFeatures.containsKey(ResponseFeature.ANSWERS)) {
            ArrayList<Answer> answers = getResponseFeature(ResponseFeature.ANSWERS, ArrayList.class);
            resultBean.addAllData(answers);
        }

        Info info = new Info();

        // set module
        if (responseFeatures.containsKey(ResponseFeature.MODULE)) {
            info.setModule(getResponseFeature(ResponseFeature.MODULE, String.class));
        }

        // set emotion
        if (responseFeatures.containsKey(ResponseFeature.EMOTION)) {
            info.setEmotion(getResponseFeature(ResponseFeature.EMOTION, String.class));
        }

        // set emotion score
        if (responseFeatures.containsKey(ResponseFeature.EMOTION_SCORE)) {
            info.setEmotionScore(getResponseFeature(ResponseFeature.EMOTION_SCORE, Double.class).intValue());
        }

        // set score
        if (responseFeatures.containsKey(ResponseFeature.SCORE)) {
            info.setTextScore(getResponseFeature(ResponseFeature.SCORE, Double.class).intValue());
        }

        // set tokens
        if (responseFeatures.containsKey(ResponseFeature.TOKENS)) {
            info.setTokens((ArrayList<String>) getResponseFeature(ResponseFeature.TOKENS, List.class));
        }

        // set intent
        if (responseFeatures.containsKey(ResponseFeature.INTENT)) {
            info.setIntent(getResponseFeature(ResponseFeature.INTENT, String.class));
        }

        // set intent score
        if (responseFeatures.containsKey(ResponseFeature.INTENT_SCORE)) {
            info.setIntentScore(getResponseFeature(ResponseFeature.INTENT_SCORE, Double.class).intValue());
        }

        // set info
        resultBean.setInformation(info);
        
        // set customInfo
        if(customInfo != null) {
        		resultBean.setCustomInfo(customInfo);
        }
        
        // set matchQuestion
 		if (responseFeatures.containsKey(ResponseFeature.MATCHED_QUESTION)) {
 			String question = (String) getResponseFeature(
 					ResponseFeature.MATCHED_QUESTION, String.class);
 			info.setMatchQuestion(question);
 		}
        
        // set processing time
        resultBean.setTspan(totalTime);

    }


    @Override
    public <T> T getSentenceFeature(SentenceFeature type, Class<? extends T> returnType) {
        Object valueObject = sentenceFeatures.get(type);
        return getFeature(valueObject, returnType);
    }

    @Override
    public void addSentenceFeature(SentenceFeature type, Object feature) {
        sentenceFeatures.put(type, feature);
    }

    @Override
    public <T> T getResponseFeature(ResponseFeature type, Class<? extends T> returnType) {
        Object valueObject = responseFeatures.get(type);
        return getFeature(valueObject, returnType);
    }

    @Override
    public void addResponseFeature(ResponseFeature type, Object feature) {
        responseFeatures.put(type, feature);
    }

    private <T> T getFeature(Object valueObject, Class<? extends T> returnType) {
        // whether is object array
        if (valueObject instanceof Object[]) {
            Object[] valueArray = (Object[]) valueObject;
            T copy = ((Object) returnType == (Object) Object[].class) ? (T) new Object[valueArray.length] : (T) Array.newInstance(returnType.getComponentType(), valueArray.length);
            System.arraycopy(valueArray, 0, copy, 0, Math.min(valueArray.length, valueArray.length));
            return copy;
        } else {
            return (T) valueObject;
        }
    }

    public boolean containsSentenceFeature(SentenceFeature key) {
        return sentenceFeatures.containsKey(key);
    }


	public boolean containsResponseFeature(ResponseFeature key) {
		return responseFeatures.containsKey(key);
	}

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
