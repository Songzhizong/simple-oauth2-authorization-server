package cn.idealio.oauth2.server.simple.core.password;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2023/4/20
 */
public class BCryptPasswordMatcher implements PasswordMatcher {

    @Override
    public boolean matches(@Nonnull CharSequence rawPassword,
                           @Nonnull String encodedPassword) {
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }
}
