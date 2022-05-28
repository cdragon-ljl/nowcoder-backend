package com.wavecom.nowcoder;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyuncs.exceptions.ClientException;
import com.wavecom.nowcoder.utils.MailUtil;
import com.wavecom.nowcoder.utils.SensitiveUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class NowcoderBackendApplicationTests {

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private SensitiveUtil sensitiveUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void emailTest() {
        mailUtil.sendMail("15851933005@163.com", "邮件测试", "邮件测试成功");
    }

    @Test
    void contextLoads() {
    }

    @Test
    void sensitiveTest() {
        String result = sensitiveUtil.filter("你是傻瓜吗，什么垃圾玩意");
        System.out.println(result);
    }

    @Test
    void redisStringTest() {
        String redisKey = "test:count";
        redisTemplate.opsForValue().set(redisKey, 1);

        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    void redisHashTest() {
        String redisKey = "test:user";

        redisTemplate.opsForHash().put(redisKey, "id", 1);
        redisTemplate.opsForHash().put(redisKey, "username", "cd");

        System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey, "username"));
    }

    @Test
    void redisListTest() {
        String redisKey = "test:ids";

        redisTemplate.opsForList().leftPush(redisKey, 101);
        redisTemplate.opsForList().leftPush(redisKey, 102);
        redisTemplate.opsForList().leftPush(redisKey, 103);

        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey, 0));
        System.out.println(redisTemplate.opsForList().range(redisKey, 0, 2));

        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
    }

    @Test
    void redisSetTest() {
        String redisKey = "test:names";

        redisTemplate.opsForSet().add(redisKey, "wu", "yuan", "liu", "liu", "du");

        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    @Test
    void redisZSetTest() {
        String redisKey = "test:mates";

        redisTemplate.opsForZSet().add(redisKey, "wu", 88);
        redisTemplate.opsForZSet().add(redisKey, "yuan", 86);
        redisTemplate.opsForZSet().add(redisKey, "liu", 85);
        redisTemplate.opsForZSet().add(redisKey, "du", 87);

        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
        System.out.println(redisTemplate.opsForZSet().score(redisKey, "liu"));
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey, "liu"));
        System.out.println(redisTemplate.opsForZSet().range(redisKey, 0, 2));
    }

    @Test
    void redisKeyTest() {
        redisTemplate.delete("test:user");

        System.out.println(redisTemplate.hasKey("test:user"));

        redisTemplate.expire("test:mates", 10, TimeUnit.SECONDS);
    }

    @Test
    void redisBoundOperations() {
        String redisKey = "test:count";

        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
        operations.increment();
        operations.increment();
        operations.increment();
        System.out.println(operations.get());
    }

    @Test
    void redisTransactionTest() {
        Object execute = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:tx";
                //开始执行
                operations.multi();

                operations.opsForSet().add(redisKey, "wu");
                operations.opsForSet().add(redisKey, "yuan");
                operations.opsForSet().add(redisKey, "liu");
                operations.opsForSet().add(redisKey, "du");

                System.out.println(operations.opsForSet().members(redisKey));

                return operations.exec();
            }
        });
        System.out.println(execute);
    }
}

