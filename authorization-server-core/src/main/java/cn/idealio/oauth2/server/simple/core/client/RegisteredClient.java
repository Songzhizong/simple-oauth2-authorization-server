package cn.idealio.oauth2.server.simple.core.client;

import cn.idealio.oauth2.server.simple.core.exception.InvalidClientSecretException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * OAuth2授权客户端
 *
 * @author 宋志宗 on 2023/4/20
 */
@Getter
@Setter
@Accessors(chain = true)
public class RegisteredClient {

    /** 客户端ID */
    @Nonnull
    private String clientId = "";

    /** 客户端密钥 */
    @Nonnull
    private Set<String> clientSecrets = new HashSet<>();

    /** 登录页连接 */
    @Nullable
    private String loginUrl = null;

    /** 重定向地址 */
    @Nonnull
    private String redirectUri = "";

    /** 授权空间 */
    @Nonnull
    private Set<String> scopes = new HashSet<>();

    /** 过期时间戳 */
    @Nullable
    private Long expirationTimestamp = null;

    @Nonnull
    public String loadSimpleRedirectUri() {
        String redirectUri = getRedirectUri();
        int index = redirectUri.indexOf("?");
        if (index < 0) {
            return redirectUri;
        }
        return redirectUri.substring(0, index);
    }

    public void authenticate(@Nonnull String clientSecret) {
        if (!clientSecrets.contains(clientSecret)) {
            throw new InvalidClientSecretException();
        }
    }

    @Nonnull
    public String genScopeString() {
        Set<String> scopes = getScopes();
        if (scopes.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String scope : scopes) {
            sb.append(scope).append(',');
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
