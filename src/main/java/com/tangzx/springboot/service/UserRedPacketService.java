package com.tangzx.springboot.service;

/**
 * @Auther: Tangzx
 * @Date: 2018/11/22 14:54
 * @Description:
 */
public interface UserRedPacketService {
    /**
     * @param: [redPacketId, userId] 红包编号，抢红包用户编号
     * @return: int
     * @author: Tangzx
     * @date: 2018/11/22 14:55
     * @Description: 保存抢红包的信息, 悲观锁的方式
     */
    public boolean grabRedPacket(Long redPacketId, Long userId);

    /**
     * @param: [redPacketId, userId] 红包编号，抢红包用户编号
     * @return: int
     * @author: Tangzx
     * @date: 2018/11/23 9:43
     * @Description: 保存抢红包信息. 乐观锁的方式，按时间戳重入
     */
    public boolean grabRedPacketForVersionAndTime(Long redPacketId, Long userId);


    /**
     * @param: [redPacketId, userId] 红包编号，抢红包用户编号
     * @return: int
     * @author: Tangzx
     * @date: 2018/11/23 9:43
     * @Description: 保存抢红包信息. 乐观锁的方式，按次数重入
     */
    public boolean grabRedPacketForVersionAndNumber(Long redPacketId, Long userId);

    /**
     * @param: [redPacketId, userId] 红包编号，抢红包用户编号
     * @return: java.lang.Long 0-没有库存 失败, 1--成功 且不是最后一个红包 ,2--成功，且是最后一个红包
     * @author: Tangzx
     * @date: 2018/11/23 15:45
     * @Description: 通过Redis实现抢红包
     */
    public Long grabRedPacketByRedis(Long redPacketId, Long userId);
}
