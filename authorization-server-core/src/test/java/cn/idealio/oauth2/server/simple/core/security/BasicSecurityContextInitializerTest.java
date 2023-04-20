package cn.idealio.oauth2.server.simple.core.security;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author 宋志宗 on 2023/4/20
 */
public class BasicSecurityContextInitializerTest {

    @Test
    public void initialize() {
    }

    @Test
    public void getAuthorizationFromQueryString() {
        String authorization = BasicSecurityContextInitializer
            .getAuthorizationFromQueryString("test=a&Authorization=Baisc fdsafsd&name=test");
        Assert.assertEquals(authorization, "Baisc fdsafsd");
    }
}
