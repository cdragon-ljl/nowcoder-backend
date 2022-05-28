package com.wavecom.nowcoder.utils;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File RedisUtil
 * @Date 2022/5/27 3:57 PM
 **/
public class RedisUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_TOKEN = "token";
    private static final String PREFIX_USER = "user";

    /**
     * 实体的赞
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getEntityLikeKey(int entityType, int entityId) {

        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 用户的赞
     * @param uid
     * @return
     */
    public static String getUserLikeKey(int uid) {
        return PREFIX_USER_LIKE + SPLIT + uid;
    }

    /**
     * 用户关注的实体
     * @param uid
     * @param entityType
     * @return
     */
    public static String getFolloweeKey(int uid, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + uid + SPLIT + entityType;
    }

    /**
     * 实体的粉丝
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 登录验证码
     * @param owner
     * @return
     */
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    /**
     * 登录凭证
     * @param ticket
     * @return
     */
    public static String getTicketKey(String ticket) {
        return PREFIX_TOKEN + SPLIT + ticket;
    }

    /**
     *
     * @param uid
     * @return
     */
    public static String getUserKey(int uid) {
        return PREFIX_USER + SPLIT + uid;
    }
}
