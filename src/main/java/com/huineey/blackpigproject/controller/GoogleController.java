package com.huineey.blackpigproject.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Controller
public class GoogleController {

    @Autowired
    GoogleConnectionFactory googleConnectionFactory;
    @Autowired
    OAuth2Parameters googleOAuth2Parameters;

    // 구글 로그인 페이지
    @GetMapping("/personal")
    public String main() {
        OAuth2Operations oAuth2Operations = googleConnectionFactory.getOAuthOperations();
        String url = oAuth2Operations.buildAuthenticateUrl(GrantType.AUTHORIZATION_CODE,googleOAuth2Parameters);
        System.out.println(url);
        return "redirect:"+url;
    }

    @GetMapping("/google/callback")
    public String GetRequestToken(HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("code", request.getParameter("code"));
        parameters.add("client_id", "1005769212578-sagsuujgoisaih1j0c4vrlt3la6dj564.apps.googleusercontent.com");
        parameters.add("client_secret", "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");
        parameters.add("redirect_uri", googleOAuth2Parameters.getRedirectUri());
        parameters.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange("https://www.googleapis.com/oauth2/v4/token", HttpMethod.POST, requestEntity, Map.class);
        Map<String, Object> responseMap = responseEntity.getBody();

        System.out.println(responseEntity.toString());

        String[] tokens = ((String)responseMap.get("id_token")).split("\\.");
        Base64 base64 = new Base64(true);
        String body = new String(base64.decode(tokens[1]));

        System.out.println(tokens.length);
        System.out.println(new String(Base64.decodeBase64(tokens[0]), StandardCharsets.UTF_8));
        System.out.println(new String(Base64.decodeBase64(tokens[1]), StandardCharsets.UTF_8));

        //JSON을 자바 Map 형식으로 변환
        ObjectMapper mapper = new ObjectMapper();
        Map result = mapper.readValue(body, Map.class);
        System.out.println(result.get(""));


        return "redirect:http://localhost:8080/personal/info";
    }


}