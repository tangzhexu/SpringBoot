package com.tangzx.springboot.repository;

import com.tangzx.springboot.domain.RedPacket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther: Tangzx
 * @Date: 2018/11/22 14:25
 * @Description:
 */
@Mapper
public interface RedPacketDao {
    /*
     * @param: [id] 红包id
     * @return: com.tangzx.springboot.entity.RedPacket
     * @author: Tangzx
     * @date: 2018/11/22 14:27
     * @Description: 查询红包具体信息
     */
    public RedPacket getRedPacket(Long id);

    /*
     * @param: [id] 红包id
     * @return: int
     * @author: Tangzx
     * @date: 2018/11/22 14:27
     * @Description: 更新红包信息
     */
    public int decreaseRedPacket(Long id);

    /*
     * @param: [id] 红包id
     * @return: com.tangzx.springboot.entity.RedPacket 红包具体信息
     * @author: Tangzx
     * @date: 2018/11/22 17:31
     * @Description: 获取红包信息. 悲观锁的实现方式
     */
    public RedPacket getRedPacketForUpdate(Long id);
    /*
     * @param: [id, version] 红包id,本标记
     * @return: int 更新记录条数
     * @author: Tangzx
     * @date: 2018/11/23 9:35
     * @Description: 扣减抢红包数. 乐观锁的实现方式
     */
    public int decreaseRedPacketForVersion(@Param("id") Long id, @Param("version") Integer version);
}
