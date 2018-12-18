#!/bin/bash

HOME_DIR=/usr/src/app
configfile="$HOME_DIR/config/Controller.properties"

#decrypt password
if [[ ${#DECRYPTION_SERVICE} > 0 ]]; then
    MYSQL_PASSWORD=$(curl -sS -X GET $DECRYPTION_SERVICE$EC_MYSQL_EMOTIBOT_PASSWORD)
    if [[ $? -ne 0 ]]; then
        exit $?
    fi
    export EC_MYSQL_EMOTIBOT_PASSWORD=$MYSQL_PASSWORD
    export EC_MYSQL_BACKEND_LOG_PASSWORD=$MYSQL_PASSWORD
    REDIS_PASSWORD=$(curl -sS -X GET $DECRYPTION_SERVICE$EC_REDIS_PASSWORD)
    export EC_REDIS_PASSWORD=$REDIS_PASSWORD
fi

cd $HOME_DIR/config
ls

#generat debug config properties

echo "controllerConfigPath=$EC_CONFIG_PATH" >> $configfile

echo "servicePort=$EC_SERVER_PORT" >> $configfile
echo "serviceThreadPoolMax=$EC_THREADPOOL_MAXTHREADS" >> $configfile
echo "serviceThreadPoolMin=$EC_THREADPOOL_MINTHREADS" >> $configfile
echo "serviceTimeout=$EC_SERVICE_TIMEOUT" >> $configfile

echo "mlServiceUrl=$EC_ML_SERVICE_URL" >> $configfile
echo "faqUrl=$EC_FAQ_URL" >> $configfile
echo "knowledgeUrl=$EC_KNOWLEDGE_URL" >> $configfile
echo "emotionUrl=$EC_EMOTION_URL" >> $configfile
echo "cuControllerUrl=$EC_CU_CONTROLLER_URL" >> $configfile
echo "humanIntentUrl=$EC_HUMAN_INTENT_URL" >> $configfile
echo "intentUrl=$EC_INTENT_URL" >> $configfile
echo "functionUrl=$EC_FUNCTION_URL" >> $configfile
echo "functionIntentUrl=$EC_FUNCTION_INTENT_URL" >> $configfile
echo "chatServiceUrl=$EC_CHAT_URL" >> $configfile
echo "taskEngineUrl=$EC_TASK_ENGINE_URL" >> $configfile
echo "nluServiceUrl=$EC_NLU_SERVCIE_URL" >> $configfile
echo "pinyinServiceUrl=$EC_PINYIN_SERVCIE_URL" >> $configfile
echo "actUrl=$EC_ACT_URL" >> $configfile

echo "consulHost=$EC_CONSUL_HOST" >> $configfile
echo "consulKeyPrefix=$EC_CONSUL_KEY_PREFIX" >> $configfile

echo "redisHost=$EC_REDIS_HOST" >> $configfile
echo "redisPort=$EC_REDIS_PORT" >> $configfile
echo "redisPassword=$EC_REDIS_PASSWORD" >> $configfile
echo "redisConnectionTimeout=$EC_REDIS_CONNECTION_TIMEOUT" >> $configfile

echo "intentThreshold=$EC_INTENT_THRESHOLD" >> $configfile

echo "emotionThreshold1=$EC_EMOTION_THRESHOLD_1" >> $configfile

echo "faqThreshold0=$EC_RANKER_FAQ_THRESHOLD0" >> $configfile
echo "faqThreshold1=$EC_RANKER_FAQ_THRESHOLD1" >> $configfile
echo "faqThreshold2=$EC_RANKER_FAQ_THRESHOLD2" >> $configfile

echo "reportTimeInterval=$EC_REPORT_TIME_INTERVAL" >> $configfile
echo "reportRunTime=$EC_REPORT_RUN_TIME" >> $configfile


echo "chatThreshold=$EC_RANKER_CHAT_THRESHOLD" >> $configfile

echo "knowledgeThreshold=$EC_RANKER_CHAT_THRESHOLD" >> $configfile

echo "chatModule=$EC_CHAT_MODULE" >> $configfile

echo "applicationContextPath=$EC_APPLICATION_CONTEXT_PATH" >> $configfile

echo "sqTagUrl=$EC_SQ_TAG_URL" >> $configfile
echo "configUrl=$EC_CONFIG_URL" >> $configfile
echo "configVersionUrl=$EC_CONFIG_VERSION_URL" >> $configfile
echo "answerSyncUrl=$EC_ANSWER_SYNC_URL" >> $configfile

echo "logstashHostUrl=$EC_LOGSTASH_HOST_URL" >> $configfile
echo "logstashRecordsPort=$EC_LOGSTASH_RECORDS_PORT" >> $configfile
echo "logstashSessionsPort=$EC_LOGSTASH_SESSIONS_PORT" >> $configfile

echo "statsdHostUrl=$EC_STATSD_HOST_URL" >> $configfile
echo "statsdHostPort=$EC_STATSD_HOST_PORT" >> $configfile

echo "getEnterpriseIdUrl=$EC_GET_ENTERPRISE_ID_URL" >> $configfile

echo "prefDefaultEnabled=$EC_DEFAULT_ENABLED_MODULE" >> $configfile


echo "cachedQATimeout=$EC_CACHED_QA_TIMEOUT" >> $configfile
echo "enableMysqlLog=$EC_ENABLE_MYSQL_LOG" >> $configfile

#end of config properties

rm $HOME_DIR/config/log4j2.properties

if [ ! -n "$MODULE_LOG_LEVEL" ]; then
  module_log_level=3
else
  module_log_level=$MODULE_LOG_LEVEL
fi

case  "$module_log_level"  in

    "0"  )
    EC_LOGGER_FILE_LOG_LEVEL=debug
    EC_LOGGER_CONSOLE_LOG_LEVEL=debug
    ;;

    "1"  )
    EC_LOGGER_FILE_LOG_LEVEL=info
    EC_LOGGER_CONSOLE_LOG_LEVEL=info
    ;;

    "2"  )
    EC_LOGGER_FILE_LOG_LEVEL=warn
    EC_LOGGER_CONSOLE_LOG_LEVEL=warn
    ;;

    "3"  )
    EC_LOGGER_FILE_LOG_LEVEL=error
    EC_LOGGER_CONSOLE_LOG_LEVEL=error
    ;;

    * )
    EC_LOGGER_FILE_LOG_LEVEL=error
    EC_LOGGER_CONSOLE_LOG_LEVEL=error
    ;;

esac


while read line
do
    eval echo "$line" >> $HOME_DIR/config/log4j2.properties
done < $HOME_DIR/docker/template/log4j2.template


rm $HOME_DIR/config/mysql.properties


rm $HOME_DIR/config/applicationContext.xml

while read line
do
    preprocess=`echo $line | sed 's/"/\\\"/g'`
    echo `eval "echo \"$preprocess\""` >> $HOME_DIR/config/applicationContext.xml
done < $HOME_DIR/docker/template/applicationContext.template



#mv /usr/src/app/target/EmotibotController*with-dependencies.jar /usr/src/app

cd /usr/src/app



# Start the service
java -Xmx5125m \
    -Dlog4j.configurationFile="$HOME_DIR/config/log4j2.properties" \
    -Duser.timezone=GMT+08 \
    -jar sf-statistics*with-dependencies.jar main.ControllerServlet
