package com.tangzx.springboot.service.impl;

import com.tangzx.springboot.domain.RedPacket;
import com.tangzx.springboot.repository.RedPacketDao;
import com.tangzx.springboot.service.RedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: Tangzx
 * @Date: 2018/11/22 14:56
 * @Description:
 */
@Service
public class RedPacketServiceImpl implements RedPacketService {
    @Autowired
    private RedPacketDao redPacketDao;

    @Override
    public RedPacket getRedPacket(Long id) {
        return redPacketDao.getRedPacket(id);
    }

    @Override
    public boolean decreaseRedPacket(Long id) {
        int i = redPacketDao.decreaseRedPacket(id);
        if (i > 0) {
            return true;
        }
        return false;
    }
}
