/*
 * Copyright (c) 2016 by Emotibot Corporation. All rights reserved.
 * EMOTIBOT CORPORATION CONFIDENTIAL AND TRADE SECRET
 *
 * Primary Owner: xiongyaohua@emotibot.com.cn
 * Secondary Owner:
 */
package type.sentence;

import com.emotibot.format.Response.ResItem;
import type.immutable.*;
import type.response.IResultBean;

import java.util.List;

/**
 * User input sentence.
 */
public interface Sentence {

    /**
     * get raw sentence before processors
     *
     * @return
     */
    public String getRawSentence();

    public String getProcessedSentence();

    public void setResponse(IResultBean resultBean);

    public List<String> getDimensionTags();

    public Object getCustomInfo();

    /**
     * get exact sentence feature
     *
     * @param type
     * @return
     */
    public <T> T getSentenceFeature(SentenceFeature type, Class<? extends T> returnType);

    public <T> T getResponseFeature(ResponseFeature type, Class<? extends T> returnType);


    /**
     * get user id
     *
     * @return
     */
    public String getUserId();

    /**
     * get unique id
     *
     * @return
     */
    public String getUniqueId();


    public String getRobotId();

    /**
     * get enterprise id
     * 
     * @return
     */
    public String getEnterpriseId();

    public String getSessionId();

    public boolean getCutShortSignal();

    public String getLastFeature();

    public String getLastAnswer();

    public boolean getFinishModule(String module);
	
	public void setFinishModule(String module);
	
	public Object getExtendData(String module);
	
	public String getNote();
	
    /**
     * question semantic features
     */
    public enum SentenceFeature {
        RETRY_ANSWER(Response.class),
        ASK_TO_HUMAN(String.class),
        TO_HUMAN(String.class),
        SYNONYM_SENTENCE(String.class),
        MGC_WORDPOS(List.class),
        CHAT_RES(ResItem.class),
        FUNCTION_RES(ResItem.class),
        ML_RES(ResItem.class),
        FAQ_RES(FAQRes.class),
        FUNCTION_INTENT(Intent.class),
        INTENT(Intent.class),
        EMOTION_RES(GeneralItem.class),
        EMOTION_ANSWER(String.class),
        KEYWORD_RES(String.class),
        BACKFILL_RES(String.class),
        KNOWLEDGE_RES(KnowledgeRes.class),
        TASK_ENGINE_RES(String.class),
        TASK_ENGINE_REWIND_RES(String.class),
        ROBOT_SWITCH_RES(String.class),
        NAMED_ENTITIES(String.class),
        PERSON_NE(String.class),
        SENTENCE_WITH_CONTEXT(Sentence.class),
        LAST_QUESTION(Sentence.class),
        LAST_ANSWER(Sentence.class);

        public Class<?> returnType;

        SentenceFeature(Class<?> returnType) {
            this.returnType = returnType;
        }

        public Class<?> getReturnType() {
            return this.returnType;
        }

    }

    public enum ResponseFeature {
        ANSWERS(List.class),
        MODULE(String.class),
        RELATED_QUESTIONS(List.class), // 未在答案内
        SCORE(Double.class),
        MATCHED_QUESTION(String.class),
        EMOTION(String.class),
        EMOTION_SCORE(String.class),
        TOKENS(List.class),
        INTENT(String.class),
        INTENT_SCORE(String.class),
        ERROR(String.class),
        SOURCE(String.class),
        FAQ_RES(Object.class)
        ;

        public Class<?> returnType;

        ResponseFeature(Class<?> returnType) {
            this.returnType = returnType;
        }

        public Class<?> getReturnType() {
            return this.returnType;
        }

    }

    public boolean containsSentenceFeature(SentenceFeature key);

    public boolean containsResponseFeature(ResponseFeature key);
}


