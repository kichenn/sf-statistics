package utils;

import channel.bean.ChannelPo;
import com.alibaba.fastjson.JSONArray;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import config.ConfigManagedService;
import config.ConfigManager;
import config.Constants;

import java.util.List;

/**
 * @Author: zujikang
 * @Date: 2020-03-15 13:10
 */
public class ConsulClientUtils {

    private static String consulUrl = "172.16.0.1";
    private static Integer consulPort = 8500;
    private static String channelKey = "idc/setting/teInfo";
    private static ConsulClient consulClient = new ConsulClient(consulUrl,consulPort);

    static{
        ConfigManager configManager = ConfigManagedService.INSTANCE.getConfig();
        consulUrl = configManager.getStr(Constants.CONSUL_HOST);
        consulPort = configManager.getInteger(Constants.CONSUL_PORT);
        channelKey = configManager.getStr(Constants.CONSUL_CHANNEL_KEY);
        consulClient = new ConsulClient(consulUrl,consulPort);
    }

    public static boolean addChannel(ChannelPo channelPo) {
        String value = consulClient.getKVValue(channelKey).getValue().getDecodedValue();
        List<ChannelPo> channelPos = JSONArray.parseArray(value, ChannelPo.class);
        channelPo.setStatus(null);
        channelPos.add(channelPo);
        Response<Boolean> booleanResponse = consulClient.setKVValue(channelKey, JSONArray.toJSONString(channelPos));
        return booleanResponse.getValue();
    }

    public static boolean updateChannel(ChannelPo channelPo) {
        String value = consulClient.getKVValue(channelKey).getValue().getDecodedValue();
        List<ChannelPo> channelPos = JSONArray.parseArray(value, ChannelPo.class);
        channelPos.forEach(e -> {
            if (e.getChannelId().equals(channelPo.getChannelId())) {
                e.setChannelName(channelPo.getChannelName());
            }});
        Response<Boolean> booleanResponse = consulClient.setKVValue(channelKey, JSONArray.toJSONString(channelPos));
        return booleanResponse.getValue();
    }

}