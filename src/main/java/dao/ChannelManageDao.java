package dao;

import channel.bean.ChannelPo;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

/**
 * @Author: zujikang
 * @Date: 2020-03-06 15:03
 */
@MapperScan
public interface ChannelManageDao {

    Long addChannel(ChannelPo channelPo);

    List<ChannelPo> listAllChannel();

    List<ChannelPo> listActiveChannel();

    Long updateChannel(ChannelPo channelPo);
}
