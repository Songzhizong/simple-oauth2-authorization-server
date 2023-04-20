package cn.idealio.oauth2.server.simple.core.password;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2023/4/20
 */
public class PlainPasswordMatcher implements PasswordMatcher {

    @Override
    public boolean matches(@Nonnull CharSequence rawPassword,
                           @Nonnull String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }
}
