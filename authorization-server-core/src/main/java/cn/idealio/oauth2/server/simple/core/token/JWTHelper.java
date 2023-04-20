package cn.idealio.oauth2.server.simple.core.token;

import cn.idealio.oauth2.server.simple.core.exception.OAuthException;
import cn.idealio.oauth2.server.simple.core.exception.TokenExpiredException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 宋志宗 on 2023/4/20
 */
@Slf4j
public class JWTHelper {
    /** 10分钟有效 */
    private static final int expireSeconds = 600;
    private static final String SECRET = "rVtqG@ciW!jkcXW7oUtgkK2hmeHW4W!k";
    private static final String TOKEN_PREFIX = OAuthToken.TOKEN_TYPE + " ";
    private static final int TOKEN_PREFIX_LENGTH = TOKEN_PREFIX.length();
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).build();

    @Nonnull
    public static OAuthToken generateToken(@Nonnull String clientId,
                                           @Nonnull String userId,
                                           @Nullable String tenantId,
                                           @Nullable String scopes) {
        if (tenantId == null) {
            tenantId = "";
        }
        if (scopes == null) {
            scopes = "";
        }
        long expireTime = System.currentTimeMillis() + (expireSeconds * 1000);
        String token = JWT.create()
            .withAudience(clientId, userId, tenantId, scopes, String.valueOf(expireTime))
            .sign(ALGORITHM);
        return new OAuthToken(token, scopes, expireSeconds);
    }

    @Nonnull
    public static TokenInfo parseAuthorization(@Nonnull String token) {
        try {
            DecodedJWT jwt = VERIFIER.verify(token.substring(TOKEN_PREFIX_LENGTH));
            List<String> audience = jwt.getAudience();
            String clientId = audience.get(0);
            String userId = audience.get(1);
            String tenantId = audience.get(2);
            String scopesStr = audience.get(3);
            String expireTime = audience.get(4);
            if (System.currentTimeMillis() > Long.parseLong(expireTime)) {
                log.info("Token已过期");
                throw new TokenExpiredException();
            }
            if ("".equals(tenantId)) {
                tenantId = null;
            }
            Set<String> scopes = getScopes(scopesStr);
            return new TokenInfo(clientId, userId, tenantId, scopes);
        } catch (Exception exception) {
            log.info("Token解析失败", exception);
            throw new OAuthException(exception);
        }
    }

    @Nonnull
    private static Set<String> getScopes(@Nullable String scopesStr) {
        Set<String> scopes = new HashSet<>();
        if (scopesStr != null && !scopesStr.isBlank()) {
            String[] split = scopesStr.split(",");
            for (String scope : split) {
                String trim = scope.trim();
                if (trim.length() > 1) {
                    scopes.add(trim);
                }
            }
        }
        return scopes;
    }
}
