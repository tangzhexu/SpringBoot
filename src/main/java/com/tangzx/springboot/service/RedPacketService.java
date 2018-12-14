package com.tangzx.springboot.service;

import com.tangzx.springboot.domain.RedPacket;

/**
 * @Auther: Tangzx
 * @Date: 2018/11/22 14:51
 * @Description:
 */
public interface RedPacketService {
    /**
     * @param: [id] 红包编号
     * @return: com.tangzx.springboot.entity.RedPacket 红包信息
     * @author: Tangzx
     * @date: 2018/11/22 14:52
     * @Description: 获取红包
     */
    public RedPacket getRedPacket(Long id);

    /**
     * @param: [id]
     * @return: int
     * @author: Tangzx
     * @date: 2018/11/22 14:52
     * @Description: 扣减红包
     */
    public boolean decreaseRedPacket(Long id);
}
