package com.tangzx.springboot.web;

import com.tangzx.springboot.service.UserRedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Tangzx
 * @Date: 2018/11/22 15:17
 * @Description:
 */
@Controller
@RequestMapping("/userRedPacket")
public class UserRedPacketController {
    @Autowired
    private UserRedPacketService userRedPacketService;

    @RequestMapping(value = "/grabRedPacket")
    @ResponseBody
    public Map<String, Object> grabRedPacket(Long redPacketId, Long userId) {
        // 抢红包
        //boolean result = userRedPacketService.grabRedPacket(redPacketId, userId);
        //boolean result = userRedPacketService.grabRedPacketForVersionAndTime(redPacketId, userId);
        // boolean result = userRedPacketService.grabRedPacketForVersionAndNumber(redPacketId, userId);
        Long result = userRedPacketService.grabRedPacketByRedis(redPacketId, userId);
        boolean flag = result > 0;
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("success", result);
        retMap.put("message", flag ? "抢红包成功" : "抢红包失败");
        return retMap;
    }

    @RequestMapping(value = "/gotoGrabRedPacket")
    public String gotoGrabRedPacket() {
        return "hello";
    }
}
