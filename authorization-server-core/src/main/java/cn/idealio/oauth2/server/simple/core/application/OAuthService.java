package cn.idealio.oauth2.server.simple.core.application;

import cn.idealio.oauth2.server.simple.core.cache.CodeInfo;
import cn.idealio.oauth2.server.simple.core.cache.OAuthCache;
import cn.idealio.oauth2.server.simple.core.client.RegisteredClient;
import cn.idealio.oauth2.server.simple.core.client.RegisteredClientRepository;
import cn.idealio.oauth2.server.simple.core.constant.OAuthScope;
import cn.idealio.oauth2.server.simple.core.exception.ClientNotFoundException;
import cn.idealio.oauth2.server.simple.core.exception.ForbiddenException;
import cn.idealio.oauth2.server.simple.core.exception.InvalidCodeException;
import cn.idealio.oauth2.server.simple.core.exception.UserNotFoundException;
import cn.idealio.oauth2.server.simple.core.token.JWTHelper;
import cn.idealio.oauth2.server.simple.core.token.OAuthToken;
import cn.idealio.oauth2.server.simple.core.token.TokenInfo;
import cn.idealio.oauth2.server.simple.core.user.OAuthUser;
import cn.idealio.oauth2.server.simple.core.user.OAuthUserDetail;
import cn.idealio.oauth2.server.simple.core.user.OAuthUserRepository;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author 宋志宗 on 2023/4/20
 */
@Slf4j
public class OAuthService {
    private static final String REDIRECT_URI_MISMATCH_STR
        = "error=redirect_uri_mismatch&error_description=The+redirect_uri+MUST+match+the+registered+callback+URL+for+this+application.";
    private final OAuthCache oauthCache;
    private final OAuthUserRepository userRepository;
    private final RegisteredClientRepository registeredClientRepository;

    public OAuthService(@Nonnull OAuthCache oauthCache,
                        @Nonnull OAuthUserRepository userRepository,
                        @Nonnull RegisteredClientRepository registeredClientRepository) {
        this.oauthCache = oauthCache;
        this.userRepository = userRepository;
        this.registeredClientRepository = registeredClientRepository;
    }


    @Nonnull
    public String genRedirectUrl(@Nonnull String userId,
                                 @Nullable String tenantId,
                                 @Nullable String state,
                                 @Nonnull String clientId,
                                 @Nullable String redirectUri) {
        RegisteredClient client = registeredClientRepository.findByClientId(clientId);
        if (client == null) {
            log.info("未找到clientId为[{}]的客户端", clientId);
            throw new ClientNotFoundException(clientId);
        }
        String simpleRedirectUri = client.loadSimpleRedirectUri();
        if (redirectUri != null && !redirectUri.isBlank() && !redirectUri.startsWith(simpleRedirectUri)) {
            String configuredRedirectUri = client.getRedirectUri();
            return formatRedirectUrl(configuredRedirectUri, REDIRECT_URI_MISMATCH_STR, state);
        }
        CodeInfo codeInfo = CodeInfo.of(userId, tenantId);
        oauthCache.saveCode(codeInfo);
        String code = codeInfo.getCode();
        if (redirectUri == null || redirectUri.isBlank()) {
            redirectUri = client.getRedirectUri();
        }
        return formatRedirectUrl(redirectUri, "code=" + code, state);
    }

    @Nonnull
    public OAuthToken generateToken(@Nonnull String code,
                                    @Nonnull String clientId,
                                    @Nonnull String clientSecret) {
        CodeInfo codeInfo = oauthCache.loadCode(code);
        if (codeInfo == null) {
            log.info("未找到code为[{}]的codeInfo", code);
            throw new InvalidCodeException();
        }
        RegisteredClient client = registeredClientRepository.findByClientId(clientId);
        if (client == null) {
            log.info("未找到clientId为[{}]的客户端", clientId);
            throw new ClientNotFoundException(clientId);
        }
        client.authenticate(clientSecret);

        String userId = codeInfo.getUserId();
        String tenantId = codeInfo.getTenantId();
        return JWTHelper.generateToken(clientId, userId, tenantId, client.genScopeString());
    }

    /**
     * 通过Authorization请求头获取用户信息
     *
     * @param authorization Authorization请求头
     * @return 用户信息
     */
    @Nonnull
    public OAuthUserDetail getUserDetail(@Nonnull String authorization) {
        TokenInfo tokenInfo = JWTHelper.parseAuthorization(authorization);
        Set<String> scopes = tokenInfo.getScopes();
        String clientId = tokenInfo.getClientId();
        if (!scopes.contains(OAuthScope.PROFILE)) {
            log.info("客户端没有profile权限, clientId: [{}]", clientId);
            throw new ForbiddenException(OAuthScope.PROFILE);
        }
        String userId = tokenInfo.getUserId();
        String tenantId = tokenInfo.getTenantId();
        OAuthUser user = userRepository.findByUserId(userId);
        if (user == null) {
            log.info("未找到userId为[{}]的用户", userId);
            throw new UserNotFoundException(userId);
        }
        String nickname = user.nickname();
        String email = user.email();
        String phone = user.phone();
        if (!scopes.contains(OAuthScope.EMAIL)) {
            log.info("客户端没有email权限, clientId: [{}]", clientId);
            email = null;
        }
        if (!scopes.contains(OAuthScope.PHONE)) {
            log.info("客户端没有phone权限, clientId: [{}]", clientId);
            phone = null;
        }
        return new OAuthUserDetail(userId, nickname, phone, email, tenantId);
    }

    @Nonnull
    public static String formatRedirectUrl(@Nonnull String redirectUri,
                                           @Nonnull String appendQueryString,
                                           @Nullable String state) {
        int index = redirectUri.indexOf("?");
        String url;
        if (index < 0) {
            url = redirectUri + "?" + appendQueryString;
        } else {
            url = redirectUri + "&" + appendQueryString;
        }
        if (state != null && !state.isBlank()) {
            url += "&state=" + state;
        }
        return url;
    }
}
