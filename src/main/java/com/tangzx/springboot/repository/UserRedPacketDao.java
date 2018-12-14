package com.tangzx.springboot.repository;

import com.tangzx.springboot.domain.UserRedPacket;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Auther: Tangzx
 * @Date: 2018/11/22 14:44
 * @Description:
 */
@Mapper
public interface UserRedPacketDao {
    /*
     * @param: [userRedPacket] 抢红包信息
     * @return: int 影响记录数
     * @author: Tangzx
     * @date: 2018/11/22 14:48
     * @Description: 插入抢红包信息.
     */
    public int grabRedPacket(UserRedPacket userRedPacket);
}
