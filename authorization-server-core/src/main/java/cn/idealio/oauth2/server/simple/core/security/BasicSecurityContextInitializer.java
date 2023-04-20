package cn.idealio.oauth2.server.simple.core.security;

import cn.idealio.oauth2.server.simple.core.exception.UnauthorizedException;
import cn.idealio.oauth2.server.simple.core.password.PasswordMatcher;
import cn.idealio.oauth2.server.simple.core.user.OAuthUser;
import cn.idealio.oauth2.server.simple.core.user.OAuthUserRepository;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author 宋志宗 on 2023/4/20
 */
@Slf4j
public class BasicSecurityContextInitializer implements SecurityContextInitializer {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String TOKEN_PREFIX = "Basic ";
    private static final int TOKEN_PREFIX_LENGTH = TOKEN_PREFIX.length();
    private final PasswordMatcher passwordMatcher;
    private final OAuthUserRepository oauthUserRepository;

    public BasicSecurityContextInitializer(@Nonnull PasswordMatcher passwordMatcher,
                                           @Nonnull OAuthUserRepository oauthUserRepository) {
        this.passwordMatcher = passwordMatcher;
        this.oauthUserRepository = oauthUserRepository;
    }

    @Nullable
    @Override
    public SecurityContext initialize(@Nonnull String queryString,
                                      @Nonnull Map<String, List<String>> headers) {
        String authorization;
        List<String> authorizations = headers.get(AUTHORIZATION_HEADER_NAME);
        if (authorizations == null || authorizations.isEmpty()) {
            authorization = getAuthorizationFromQueryString(queryString);
        } else {
            authorization = authorizations.get(0);
        }
        if (authorization == null || authorization.isBlank()) {
            log.info("请求头中Authorization信息为空");
            return null;
        }
        if (!authorization.startsWith(TOKEN_PREFIX)) {
            log.info("Authorization信息格式错误: {}", authorization);
            throw new UnauthorizedException("Authorization信息格式错误");
        }
        String token = authorization.substring(TOKEN_PREFIX_LENGTH);
        int index = token.indexOf(":");
        if (index < 1) {
            log.info("Authorization信息格式错误: {}", authorization);
            throw new UnauthorizedException("Authorization信息格式错误");
        }
        String username = token.substring(0, index);
        String password = token.substring(index + 1);
        if (username.isBlank() || password.isBlank()) {
            log.info("Authorization信息格式错误: {}", authorization);
            throw new UnauthorizedException("Authorization信息格式错误");
        }
        OAuthUser user = oauthUserRepository.findByUsername(username);
        if (user == null) {
            log.info("用户不存在: {}", username);
            throw new UnauthorizedException("用户名或密码错误");
        }
        String userPassword = user.password();
        if (userPassword == null) {
            log.info("用户密码为空: {}", username);
            throw new UnauthorizedException("用户名或密码错误");
        }
        if (!passwordMatcher.matches(password, userPassword)) {
            log.info("密码错误: {} {}", username, password);
            throw new UnauthorizedException("用户名或密码错误");
        }
        String userId = user.userId();
        return SecurityContext.of(userId, null);
    }

    @Nullable
    static String getAuthorizationFromQueryString(@Nullable String queryString) {
        if (queryString == null || queryString.isBlank()) {
            return null;
        }
        int index = queryString.indexOf(AUTHORIZATION_HEADER_NAME);
        if (index < 0) {
            return null;
        }
        String substring = queryString.substring(index, queryString.length() - 1);
        String[] split = substring.split("&");
        String kv = split[0].trim();
        String[] split1 = kv.split("=", 2);
        if (split1.length != 2) {
            return null;
        }
        return split1[1].trim();
    }
}
