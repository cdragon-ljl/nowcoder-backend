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

import java.io.ByteArrayInputStream;

@SpringBootTest
class NowcoderBackendApplicationTests {

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private SensitiveUtil sensitiveUtil;

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
}
