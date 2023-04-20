package cn.idealio.oauth2.server.simple.spring.web;

import cn.idealio.oauth2.server.simple.core.application.OAuthService;
import cn.idealio.oauth2.server.simple.core.dto.OAuthResult;
import cn.idealio.oauth2.server.simple.core.dto.OAuthTokenResult;
import cn.idealio.oauth2.server.simple.core.dto.UserDetailResult;
import cn.idealio.oauth2.server.simple.core.exception.OAuthException;
import cn.idealio.oauth2.server.simple.core.exception.UnauthorizedException;
import cn.idealio.oauth2.server.simple.core.security.SecurityContext;
import cn.idealio.oauth2.server.simple.core.security.SecurityContextInitializer;
import cn.idealio.oauth2.server.simple.core.token.OAuthToken;
import cn.idealio.oauth2.server.simple.core.user.OAuthUserDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * OAuth2控制器
 *
 * @author 宋志宗 on 2023/4/20
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth2")
public class OAuth2Controller {
    private final OAuthService oauthService;
    private final ObjectMapper objectMapper;
    private final SecurityContextInitializer securityContextInitializer;

    /**
     * 授权接口
     * <pre>
     *   <b>需要权限: 无</b>
     *   <p><b>请求示例</b></p>
     *   GET {{base_url}}/iam/oauth/authorize?
     *       state=1234&
     *       response_type=code&
     *       client_id=cf28d8eb95412ae08191&
     *       redirect_uri=http://127.0.0.1:17000/callback/oauth
     *   Authorization: {{token_type}} {{access_token}}
     *
     *   <p><b>成功响应</b></p>
     *   HTTP/1.1 302 Found
     *   Location: http://127.0.0.1:17000/callback/oauth?code=8b786541f8d643459edd78f527218ae8&state=1234
     *   X-Ideal-Trace-Id: 3lz0cvhqw1z4
     *
     *   <p><b>失败响应</b></p>
     *   HTTP/1.1 404 Not Found
     *   Content-Type: application/json;charset=utf-8
     *
     *   {
     *     "error": "client_not_found,
     *     "error_description": "client not found"
     *   }
     * </pre>
     *
     * @param state        随机字符串，用于防止CSRF攻击
     * @param clientId     客户端ID
     * @param responseType 授权类型，固定为code
     * @param redirectUri  回调地址
     */
    @RequestMapping("/authorize")
    public ResponseEntity<OAuthResult> authorize(@RequestParam("state") String state,
                                                 @RequestParam("client_id") String clientId,
                                                 @RequestParam("response_type") String responseType,
                                                 @RequestParam("redirect_uri") String redirectUri,
                                                 @RequestHeader HttpHeaders headers,
                                                 @Nonnull HttpServletRequest request) {
        if (log.isDebugEnabled()) {
            requestLog(headers, request);
        }
        try {
            if (!"code".equals(responseType)) {
                log.warn("responseType is not code: {}", responseType);
            }
            String queryString = request.getQueryString();
            SecurityContext context = securityContextInitializer.initialize(queryString, headers);
            if (context == null) {
                log.info("获取权限上下文返回null, 请检查是否已经登录");
                throw new UnauthorizedException();
            }
            String userId = context.userId();
            String tenantId = context.tenantId();
            String redirectUrl = oauthService.genRedirectUrl(userId, tenantId, state, clientId, redirectUri);
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl)).build();
        } catch (Throwable throwable) {
            return handleException(throwable, OAuthResult::error);
        }
    }

    @RequestMapping("/access_token")
    public ResponseEntity<OAuthResult> accessToken(@RequestParam("client_id") String clientId,
                                                   @RequestParam("client_secret") String clientSecret,
                                                   @RequestParam("code") String code,
                                                   @RequestHeader HttpHeaders headers,
                                                   @Nonnull HttpServletRequest request) {
        if (log.isDebugEnabled()) {
            requestLog(headers, request);
        }
        try {
            OAuthToken token = oauthService.generateToken(code, clientId, clientSecret);
            return ResponseEntity.ok(OAuthTokenResult.success(token));
        } catch (Throwable throwable) {
            return handleException(throwable, OAuthTokenResult::error);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<UserDetailResult> user(@RequestHeader HttpHeaders headers,
                                                 @Nonnull HttpServletRequest request) {
        if (log.isDebugEnabled()) {
            requestLog(headers, request);
        }
        try {
            String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authorization == null || authorization.isBlank()) {
                log.info("authorization is null");
                throw new UnauthorizedException();
            }
            OAuthUserDetail userDetail = oauthService.getUserDetail(authorization);
            return ResponseEntity.ok(UserDetailResult.success(userDetail));
        } catch (Throwable throwable) {
            return handleException(throwable, UserDetailResult::error);
        }
    }

    @Nonnull
    private Map<String, String> queryStringToMap(@Nullable String queryString) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (queryString == null || queryString.isBlank()) {
            return hashMap;
        }
        String[] split = queryString.split("&");
        for (String s : split) {
            String[] split1 = s.split("=");
            if (split1.length == 2) {
                String key = split1[0].trim();
                String value = split1[1].trim();
                if (key.isBlank() || value.isBlank()) {
                    continue;
                }
                hashMap.put(key, value);
            }
        }
        return hashMap;
    }

    private void requestLog(@Nonnull HttpHeaders headers, @Nonnull HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        log.debug("requestURI: {}", requestURI);
        log.debug("queryString: {}", queryString);
        try {
            String headerString = objectMapper.writeValueAsString(headers);
            log.debug("headers: {}", headerString);
        } catch (JsonProcessingException e) {
            // ignore
        }
    }

    @Nonnull
    private <T extends OAuthResult> ResponseEntity<T> handleException(@Nonnull Throwable throwable,
                                                                      @Nonnull BiFunction<String, String, T> function) {
        if (throwable instanceof OAuthException exception) {
            int httpStatus = exception.getHttpStatus();
            String error = exception.getError();
            String errorDescription = exception.getErrorDescription();
            T apply = function.apply(error, errorDescription);
            return ResponseEntity.status(httpStatus).body(apply);
        }
        String name = throwable.getClass().getName();
        String message = throwable.getMessage();
        T apply = function.apply(name, message);
        return ResponseEntity.internalServerError().body(apply);
    }
}
