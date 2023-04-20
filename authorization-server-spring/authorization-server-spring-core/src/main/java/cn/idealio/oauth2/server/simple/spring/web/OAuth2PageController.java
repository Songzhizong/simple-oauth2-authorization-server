package cn.idealio.oauth2.server.simple.spring.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 宋志宗 on 2023/4/20
 */
@RequestMapping("/oauth2")
public class OAuth2PageController {

    @GetMapping("/authorize")
    public String authorize() {
        return "oauth2_authorize";
    }
}
