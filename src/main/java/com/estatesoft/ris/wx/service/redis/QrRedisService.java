package com.estatesoft.ris.wx.service.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by wk on 2017/9/28.
 */
@Service
public class QrRedisService extends RedisService {

    /***/
    private final String RIS_DEVICE_TICKET_CACHE = "RIS_DEVICE_TICKET_CACHE:";

    //    private final String MAGIC_MIRROR_CACHE = "MAGIC_MIRROR_CACHE";
//
//    private final String DEVICE_ID_ = "DEVICE_ID_";
//
    private final String LINK_TOKEN = "LINK_TOKEN:";
//
//    private final String DEVICE_ID_QRCODE_URL_ = "DEVICE_ID_QRCODE_URL_";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    //private final String URINE_MACID_QRCODE = "URINE_MACID_QRCODE:";


//    /**
//     * redis缓存1分钟
//     *
//     * @param deviceId
//     * @param ticket
//     */
//    public void putDeviceIdTicketCache(String deviceId, String ticket) {
//        String key = deviceId + "_" + ticket;
//        redisTemplate.opsForValue().set(MAGIC_MIRROR_DEVICE_TICKET_CACHE + key, ticket, 1, TimeUnit.MINUTES);
//        //redisson.getMapCache(MAGIC_MIRROR_DEVICE_TICKET_CACHE).put(key, ticket, 1, TimeUnit.MINUTES);
//    }


//    /**
//     * 魔镜设备二维码的ticket
//     *
//     * @param key
//     * @return
//     */
//    public String getTicketByDeviceIdCache(String key) {
//        ValueOperations<String, String> ops = this.redisTemplate.opsForValue();
//        return ops.get(MAGIC_MIRROR_DEVICE_TICKET_CACHE + key);
//
////        RMapCache<String, String> mapCache = redisson.getMapCache(MAGIC_MIRROR_DEVICE_TICKET_CACHE);
////        return mapCache.get(key);
//    }


    /**
     * 获取设备TICKET
     *
     * @param deviceId
     * @return
     */
    public String getDeviceIdTicketFromCache(String deviceId) {
        //String key = DEVICE_ID_ + deviceId ;

        ValueOperations<String, String> ops = this.redisTemplate.opsForValue();
        return ops.get(RIS_DEVICE_TICKET_CACHE + deviceId);

//        RMapCache<String, String> mapCache = redisson.getMapCache(MAGIC_MIRROR_CACHE);
//        return mapCache.get(key);
    }

    /**
     * redis缓存1分钟
     *
     * @param deviceId
     * @param ticket
     */
    public void putDeviceIdTicketToCache(String deviceId, String ticket) {
        ValueOperations<String, String> ops = this.redisTemplate.opsForValue();
        ops.set(RIS_DEVICE_TICKET_CACHE + deviceId, ticket, 10, TimeUnit.MINUTES);
    }
//    public void putQrCodePictureUrlToCache(String deviceId, String qrCodePictureUrl) {
//        String key = DEVICE_ID_QRCODE_URL_ + deviceId ;
//        redisson.getMapCache(MAGIC_MIRROR_CACHE).put(key, qrCodePictureUrl, 1, TimeUnit.MINUTES);
//    }
//    /**
//     * 魔镜设备二维码的url
//     *
//     * @param deviceId
//     * @return
//     */
//    public String getQrCodePictureUrlByDeviceIdCache(String deviceId) {
//        String key = DEVICE_ID_QRCODE_URL_ + deviceId ;
//        RMapCache<String, String> mapCache = redisson.getMapCache(MAGIC_MIRROR_CACHE);
//        return mapCache.get(key);
//    }

    /**
     * @param deviceId
     * @return
     */
    public String getLinkTokenFromCache(String deviceId) {

        String key = LINK_TOKEN + deviceId;
        ValueOperations<String, String> ops = this.redisTemplate.opsForValue();
        return ops.get(key);
    }

    /**
     * linkToken缓存5分钟
     *
     * @param linkToken
     */
    public void putDeviceIdLinkTokenToCache(String deviceId, String linkToken) {
        String key = LINK_TOKEN + deviceId;
        ValueOperations<String, String> ops = this.redisTemplate.opsForValue();
        ops.set(key, linkToken, 5, TimeUnit.MINUTES);
    }

    /**
     * @param deviceId
     */
    public void deleteLinkTokenByDeviceId(String deviceId) {
        String key = LINK_TOKEN + deviceId;
        this.redisTemplate.delete(key);
    }
}
