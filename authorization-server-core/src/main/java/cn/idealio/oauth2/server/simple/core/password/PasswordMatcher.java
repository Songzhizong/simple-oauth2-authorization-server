package cn.idealio.oauth2.server.simple.core.password;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2023/4/20
 */
public interface PasswordMatcher {


    /**
     * 验证密码是否正确
     *
     * @param rawPassword     明文密码
     * @param encodedPassword 编码后的密码
     * @return 密码是否正确
     */
    boolean matches(@Nonnull CharSequence rawPassword,
                    @Nonnull String encodedPassword);
}
