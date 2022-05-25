package com.wavecom.nowcoder.constant;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File NowCoderConstant
 * @Date 2022/5/23 3:51 下午
 **/
public class NowCoderConstant {
    /** 待激活 */
    public static final int ACTIVATION_NOT = -1;

    /** 激活成功 */
    public static final int ACTIVATION_SUCCESS = 0;

    /** 重复激活 */
    public static final int ACTIVATION_REPEAT = 1;

    /** 激活失败 */
    public static final int ACTIVATION_FAILED = 2;

    /**
     * 登录凭证有效
     */
    public static final int LOGIN_TICKET_VALID = 0;

    /**
     * 登录凭证失效
     */
    public static final int LOGIN_TICKET_INVALID = 1;

    /** 默认状态的登录凭证的超时时间 12小时 */
    public static final int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /** 记住状态下的登录凭证超时时间 100天 */
    public static final int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    /** 实体类型：帖子 */
    public static final int ENTITY_TYPE_POST = 1;

    /** 实体类型：评论 */
    public static final int ENTITY_TYPE_COMMENT = 2;

    /** 实体类型：用户 */
    public static final int ENTITY_TYPE_USER = 3;

    /** 主题：评论 */
    public static final String TOPIC_COMMENT = "comment";

    /** 主题：点赞 */
    public static final String TOPIC_LIKE = "like";

    /** 主题：关注 */
    public static final String TOPIC_FOLLOW = "follow";

    /** 主题：发帖 */
    public static final String TOPIC_PUBLISH = "publish";

    /** 主题：分享 */
    public static final String TOPIC_SHARE = "share";

    /** 删除 */
    public static final String TOPIC_DELETE = "delete";

    /**
     * 普通用户
     */
    public static final int USER_TYPE_NORMAL = 1;

    /** 系统用户id */
    public static final int SYSTEM_USER_ID = 1;

    /** 权限：普通用户 */
    public static final String AUTHORITY_USER = "user";

    /** 权限：管理员 */
    public static final String AUTHORITY_ADMIN = "admin";

    /** 权限：版主 */
    public static final String AUTHORITY_MODERATOR = "moderator";
}
