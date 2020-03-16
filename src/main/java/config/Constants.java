package config;

/**
 * Created by yuao on 4/9/17.
 */
public interface Constants {
	
	/*
	 * controller setting
	 */
    String SERVICE_PORT = "servicePort";

    String SERVICE_THREADPOOL_MAX = "serviceThreadPoolMax";

    String SERVICE_THREADPOOL_MIN = "serviceThreadPoolMin";

    String SERVICE_TIMEOUT = "serviceTimeout";

    String CONTROLLER_CONFIG_PATH = "controllerConfigPath";

    String REMOTE_FLAG = "remoteFlag";
    
    String APPLICATION_CONTEXT_PATH = "applicationContextPath";
    /*
     * service url
     */
    String FAQ_URL = "faqUrl";

    String FUNCTION_URL = "functionUrl";

    String FUNCTION_INTENT_URL = "functionIntentUrl";

    String KNOWLEDGE_URL = "knowledgeUrl";

    String EMOTION_URL = "emotionUrl";
    
    String CHAT_URL = "chatServiceUrl";
    
    String INTENT_URL = "intentUrl";
    
    String HUMAN_INTENT_URL = "humanIntentUrl";

    String PINYIN_URL = "pinyinUrl";
    
    String NLU_SERVICE_URL = "nluServiceUrl";
    
    String TASK_ENGINE_URL = "taskEngineUrl";
    
    String LOGSTASH_HOST_URL = "logstashHostUrl";
    String LOGSTASH_RECORDS_PORT = "logstashRecordsPort";
    String LOGSTASH_SESSIONS_PORT = "logstashSessionsPort";

    String CONFIG_URL = "configUrl";
    String CONFIG_VERSION_URL = "configVersionUrl";
    String ANSWER_SYNC_URL = "answerSyncUrl";
   
    String STATSD_HOST_URL = "statsdHostUrl";
    String STATSD_HOST_PORT = "statsdHostPort";

    String GET_ENTERPRISE_ID_URL = "getEnterpriseIdUrl";
    

    String REDIS_HOST = "redisHost";
    String REDIS_PORT = "redisPort";
    String REDIS_CONNECTION_TIMEOUT = "redisConnectionTimeout";
    String REDIS_PASSWORD = "redisPassword";
    
    String CONSUL_HOST = "consulHost";
    String CONSUL_PORT = "consulPort";
    String CONSUL_CHANNEL_KEY = "consulChannelKey";
    String CONSUL_KEY_PREFIX = "consulKeyPrefix";
    

    /*
     *  service related setting
     */
    String INTENT_THRESHOLD = "intentThreshold";
    String EMOTION_THRESHOLD_1 = "emotionThreshold1";

    String FAQ_THRESHOLD0 = "faqThreshold0";
    String FAQ_THRESHOLD1 = "faqThreshold1";
    String FAQ_THRESHOLD2 = "faqThreshold2";

    String REPORT_TIME_INTERVAL = "reportTimeInterval";

    String REPORT_RUN_TIME = "reportRunTime";
 
    String CHAT_THRESHOLD = "chatThreshold";
    String KNOWLEDGE_THRESHOLD = "knowledgeThreshold";
    String CHAT_MODULE = "chatModule";
    
    String PREF_DEFAULT_ENABLED = "prefDefaultEnabled";

    String CACHED_QA_TIMEOUT = "cachedQATimeout";
    String ENABLE_MYSQL_LOG = "enableMysqlLog";

}
