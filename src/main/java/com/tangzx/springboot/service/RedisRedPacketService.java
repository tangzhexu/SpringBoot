package com.tangzx.springboot.service;

/**
 * @Auther: Tangzx
 * @Date: 2018/11/23 14:23
 * @Description:
 */
public interface RedisRedPacketService {
    /*
     * @param: [redPacketId, unitAmount] 抢红包编号,红包金额
     * @return: void
     * @author: Tangzx
     * @date: 2018/11/23 14:24
     * @Description: 保存redis抢红包列表
     */
    public void saveUserRedPacketByRedis(Long redPacketId, Double unitAmount);
}
