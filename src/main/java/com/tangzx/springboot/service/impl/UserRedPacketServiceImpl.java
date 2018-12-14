package com.tangzx.springboot.service.impl;

import com.tangzx.springboot.domain.RedPacket;
import com.tangzx.springboot.domain.UserRedPacket;
import com.tangzx.springboot.repository.RedPacketDao;
import com.tangzx.springboot.repository.UserRedPacketDao;
import com.tangzx.springboot.service.RedisRedPacketService;
import com.tangzx.springboot.service.UserRedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

/**
 * @Auther: Tangzx
 * @Date: 2018/11/22 15:03
 * @Description:
 */
@Service
public class UserRedPacketServiceImpl implements UserRedPacketService {
    //private Logger logger = LoggerFactory.getLogger(UserRedPacketServiceImpl.class);
    @Autowired
    private UserRedPacketDao userRedPacketDao;

    @Autowired
    private RedPacketDao redPacketDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisRedPacketService redisRedPacketService;
    // 失败
    private static final boolean FAILED = false;
    //成功
    private static final boolean SUCCEED = false;
    // Lua脚本
    String script = "local listKey = 'red_packet_list_'..KEYS[1] \n"
            + "local redPacket = 'red_packet_'..KEYS[1] \n"
            + "local stock = tonumber(redis.call('hget', redPacket, 'stock')) \n"
            + "if stock <= 0 then return 0 end \n"
            + "stock = stock -1 \n"
            + "redis.call('hset', redPacket, 'stock', tostring(stock)) \n"
            + "redis.call('rpush', listKey, ARGV[1]) \n"
            + "if stock == 0 then return 2 end \n"
            + "return 1 \n";

    // 在缓存LUA脚本后，使用该变量保存Redis返回的32位的SHA1编码，使用它去执行缓存的LUA脚本[加入这句话]
    String sha1 = null;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean grabRedPacket(Long redPacketId, Long userId) {
        //获取红包信息
        RedPacket redPacket = redPacketDao.getRedPacketForUpdate(redPacketId);
        //红包剩余库存
        int stock = redPacket.getStock();
        //logger.info("剩余Stock数量:{}", stock);
        if (stock > 0) {
            redPacketDao.decreaseRedPacket(redPacketId);
            // 生成抢红包信息
            UserRedPacket userRedPacket = new UserRedPacket();
            userRedPacket.setRedPacketId(redPacketId);
            userRedPacket.setUserId(userId);
            userRedPacket.setAmount(redPacket.getUnitAmount());
            userRedPacket.setNote("redPacket- " + redPacketId);
            //插入抢红包信息
            int result = userRedPacketDao.grabRedPacket(userRedPacket);
            if (result > 0) {
                return SUCCEED;
            }
        }
        //logger.info("没有红包啦!!!");
        // 失败返回
        return FAILED;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean grabRedPacketForVersionAndTime(Long redPacketId, Long userId) {
        //记录开始时间
        long start = System.currentTimeMillis();
        // 无限循环，等待成功或者时间满100毫秒退出
        while (true) {
            // 获取循环当前时间
            long end = System.currentTimeMillis();
            // 当前时间已经超过100毫秒，返回失败
            if (end - start > 100) {
                return FAILED;
            }
            //获取红包信息,注意version值
            RedPacket redPacket = redPacketDao.getRedPacket(redPacketId);
            //红包剩余库存>0
            if (redPacket.getStock() > 0) {
                // 再次传入线程保存的version旧值给SQL判断，是否有其他线程修改过数据
                int update = redPacketDao.decreaseRedPacketForVersion(redPacketId, redPacket.getVersion());
                // 如果没有数据更新，则说明其他线程已经修改过数据，则重新抢夺
                if (update == 0) {
                    continue;
                }
                // 生成抢红包信息
                UserRedPacket userRedPacket = new UserRedPacket();
                userRedPacket.setRedPacketId(redPacketId);
                userRedPacket.setUserId(userId);
                userRedPacket.setAmount(redPacket.getUnitAmount());
                userRedPacket.setNote("redPacket- " + redPacketId);
                //插入抢红包信息
                int result = userRedPacketDao.grabRedPacket(userRedPacket);
                if (result > 0) {
                    return SUCCEED;
                }
            } else {
                // 红包剩余库存<0,返回失败
                return FAILED;
            }
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    @Override
    public boolean grabRedPacketForVersionAndNumber(Long redPacketId, Long userId) {
        for (int i = 0; i < 3; i++) {
            //获取红包信息,注意version值
            RedPacket redPacket = redPacketDao.getRedPacket(redPacketId);
            //红包剩余库存>0
            if (redPacket.getStock() > 0) {
                // 再次传入线程保存的version旧值给SQL判断，是否有其他线程修改过数据
                int update = redPacketDao.decreaseRedPacketForVersion(redPacketId, redPacket.getVersion());
                // 如果没有数据更新，则说明其他线程已经修改过数据，则重新抢夺
                if (update == 0) {
                    continue;
                }
                // 生成抢红包信息
                UserRedPacket userRedPacket = new UserRedPacket();
                userRedPacket.setRedPacketId(redPacketId);
                userRedPacket.setUserId(userId);
                userRedPacket.setAmount(redPacket.getUnitAmount());
                userRedPacket.setNote("redPacket- " + redPacketId);
                //插入抢红包信息
                int result = userRedPacketDao.grabRedPacket(userRedPacket);
                if (result > 0) {
                    return SUCCEED;
                }
            } else {
                // 红包剩余库存<0,返回失败
                return FAILED;
            }
        }
        // 抢红包失败
        return FAILED;
    }

    @Override
    public Long grabRedPacketByRedis(Long redPacketId, Long userId) {
        //当前抢红包用户和日期信息
        String args = userId + "-" + System.currentTimeMillis();
        Long result = null;
        //获取底层Redis操作对象
        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        try {
            // 如果脚本没有加载过，那么进行加载，这样就会返回一个sha1编码
            if (sha1 == null) {
                sha1 = jedis.scriptLoad(script);
            }
            // 执行脚本，返回结果
            Object res = jedis.evalsha(sha1, 1, redPacketId + "", args);
            result = (Long) res;
            //返回结果为2时，为最后一个红包，即此时将抢红包的信息通过异步保存到数据库中
            if (result == 2) {
                //获取单个小红包金额
                String unitAmountStr = jedis.hget("red_packet_" + redPacketId, "unit_amount");
                // 触发保存数据库操作
                Double unitAmount = Double.parseDouble(unitAmountStr);
                redisRedPacketService.saveUserRedPacketByRedis(redPacketId, unitAmount);
            }
        } finally {
            // 确保jedis顺利关闭
            if (jedis != null && jedis.isConnected()) {
                jedis.close();
            }
        }
        return result;
    }
}
