报表接口说明  




 /v1/sf/ coreReport
例如：http://localhost:8080/v1/sf/coreReport?beginDate=2018-11-11&endDate=2018-11-15

1关键指标：(chat_record)
总会话量：每个渠道不同session_id数
有效会话量（有效业务会话量）：去掉user_q=‘welcome_tag’欢迎语，模块module等于faq的 不同session_id数
转人工量：answer字段里包含ACS的数据里 不同session_id数
独立服务量：总会话量 - 转人工量
 
有效独立服务量：有效会话量 - （有效未转人工量）
                                                      有效转人工量=> 去掉user_q=‘welcome_tag’ module=‘faq’ answer 包含 ‘ACS’
交互轮数:去掉user_q = ‘welcome_tag’ 后，每个渠道对应 交互次数 除以 不同session_id数
平均会话时长：去掉user_q = ‘welcome_tag’ 后，每个session_id 的create_time 最大值减去最小值 为会话时长，然后每个渠道对应的会话时长总和除以 session_id的数量，为每个会话平均时长
总转人工率：转人工量 / 总会话量
有效转人工率：转人工量 / 有效会话量
独立服务率 ： 独立服务量 / 总会话量
有效独立服务率：有效独立服务量 / 有效会话量
机器人分留率： 1- 总转人工率


 /v1/sf/ satisfyReport

2.知识点
知识点
标准问题
访问量:   根据  渠道，标准问，知识点，统计其记录量为访问量
评价有用量：根据  渠道，标准问，知识点，统计qa_solved为1的量
评价无用量：根据  渠道，标准问，知识点，统计qa_solved为0的量
无用原因：根据  渠道，标准问，知识点，原因，统计每个原因的访问量，然后将每个渠道，标准问，知识点下的原因整合


/v1/sf/roundNumReport

3.交互轮数
去掉欢迎语，module是否为ACS，根据渠道，acs类型，session_id 来 统计次数，次数小于10，单独为轮数，次数大于10 为 >10轮
 然后再根据 渠道，acs类型，轮数，来统计不同session_id个数即会话量
是否转人工类型：
总会话量：
1轮会话量：
2轮等等



/v1/sf/dialogueTimeReport

4.对话时长
去掉欢迎语，根据 渠道，acs类型，session_id 来统计，create_time最大值减去最小值 即对话时长,小于60秒以5秒为间隔，大于60s为 >60s一类
再根据渠道，acs类型，时长，计算不同session_id个数作为会话量，再根据渠道 和 acs类型 统计不同时长 里 session数总和

渠道
类型是否转人工 module是否为ACS
总会话量
0-5秒会话量
5-10秒会话量等待


/v1/sf/hourReport

5.时段分布
去掉欢迎语，获取create_time的小时数据，根据渠道，类型，小时，统计不同session_id数
 再根据渠道，类型，统计小时数为0，1，2等 的session_id数总和

类型
总会话量
0点-1点
1点-2点
等等



/v1/sf/faqindexReport

6.问答指标

渠道
总问答交互轮数：去掉欢迎语每个渠道对话次数
总FAQ交互轮数：直接回答 + 推荐回答+ 为命中
直接回答数：module为faq且score>faqthredhold0,表示直接回答
推荐回答数：module为faq且score 在faqthehold0 和faqthrehold2之间会出推荐答案
未命中数：module为backfill的
直接回答率：直接回答数 /  总FAQ交互轮数
推荐回答率： 推荐回答数 / 总FAQ交互轮数
未命中率： 未命中数 / 总FAQ交互轮数
