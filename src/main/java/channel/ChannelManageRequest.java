package channel;

import channel.bean.BaseResult;
import channel.bean.ChannelPo;
import channel.enums.BaseResultEnums;
import config.ConfigManagedService;
import config.Constants;
import dao.ChannelManageDao;
import dao.IChatRecordDao;
import log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import utils.ConsulClientUtils;
import utils.MySQLHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: zujikang
 * @Date: 2020-03-06 15:03
 */
public class ChannelManageRequest {
    public static final String addChannelPoint = "/channelManage/add";
    public static String listAllChannelPoint = "/channelManage/list";
    public static String listActiveChannelPoint = "/channelManage/listActive";
    public static String updateChannelPoint = "/channelManage/update";
    private String channelId;
    private String channelName;
    private String status;

    public void initParams(HttpServletRequest request) {
        this.channelId = request.getParameter("channelId");
        this.channelName = request.getParameter("channelName");
        this.status = request.getParameter("status");
    }

    @Transactional
    public BaseResult addChannel(HttpServletRequest request) {
        initParams(request);
        if (!isValidChannelRequest()) {
            return new BaseResult(BaseResultEnums.BAD_REQUEST);
        }
        long startTime = System.currentTimeMillis();
        ChannelPo channelPo = new ChannelPo();
        channelPo.setChannelId(channelId);
        channelPo.setChannelName(channelName);
        channelPo.setStatus(Integer.parseInt(status));
        Long line = 0L;
        line = MySQLHelper.getInstance().getChannelManageDao().addChannel(channelPo);

        if (line > 0) {
            if(ConsulClientUtils.addChannel(channelPo)){
                LoggerFactory.getLogger().info(String.format("[%s] output: '%s'", this.getClass().getSimpleName(), "time consume:" + (System.currentTimeMillis() - startTime)));
                return new BaseResult(BaseResultEnums.SUCCESS);
            }else {
                return new BaseResult(BaseResultEnums.HANDLE_CONSUL_ERROR);
            }
        } else {
            return new BaseResult(BaseResultEnums.UNIQUE_ID);
        }
    }

    public BaseResult listAllChannel(HttpServletRequest request) {
        List<ChannelPo> channelList = MySQLHelper.INSTANCE.getChannelManageDao().listAllChannel();
        if (!CollectionUtils.isEmpty(channelList)) {
            BaseResult baseResult = new BaseResult(BaseResultEnums.SUCCESS);
            baseResult.setData(channelList);
            return baseResult;
        } else {
            return new BaseResult(BaseResultEnums.SERVER_ERROR);
        }



    }

    /**
     * list所有已启用渠道
     * @param request
     * @return
     */
    public BaseResult listActiveChannel(HttpServletRequest request) {
        List<ChannelPo> channelList = MySQLHelper.INSTANCE.getChannelManageDao().listActiveChannel();
        if (!CollectionUtils.isEmpty(channelList)) {
            BaseResult baseResult = new BaseResult(BaseResultEnums.SUCCESS);
            baseResult.setData(channelList);
            return baseResult;
        } else {
            return new BaseResult(BaseResultEnums.SERVER_ERROR);
        }
    }

    /**
     * 注：channalId数据库唯一，不可更新
     * @param request
     * @return
     */
    @Transactional
    public BaseResult updateChannel(HttpServletRequest request) {
        initParams(request);
        if (!isValidChannelRequest()) {
            return new BaseResult(BaseResultEnums.BAD_REQUEST);
        }
        long startTime = System.currentTimeMillis();
        ChannelPo channelPo = new ChannelPo();
        channelPo.setChannelId(this.channelId);
        channelPo.setChannelName(this.channelName);
        channelPo.setStatus(Integer.parseInt(this.status));
        Long line = MySQLHelper.INSTANCE.getChannelManageDao().updateChannel(channelPo);

        if (line > 0) {
            if(ConsulClientUtils.updateChannel(channelPo)){
                LoggerFactory.getLogger().info(String.format("[%s] output: '%s'", this.getClass().getSimpleName(), "time consume:" + (System.currentTimeMillis() - startTime)));
                return new BaseResult(BaseResultEnums.SUCCESS);
            }else {
                return new BaseResult(BaseResultEnums.HANDLE_CONSUL_ERROR);
            }
        } else {
            return new BaseResult(BaseResultEnums.EXIST_ID);
        }
    }



    public boolean isValidChannelRequest() {
        if (StringUtils.isBlank(this.channelId)) {
            return false;
        }
        if (StringUtils.isBlank(this.channelName)) {
            return false;
        }
        if (StringUtils.isBlank(this.status) || (!this.status.equals("0") && !this.status.equals("1"))) {
            return false;
        }
        return true;
    }

}
