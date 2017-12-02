package com.pollub.ikms.ikms_mobile.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.pollub.ikms.ikms_mobile.request.LoginRequest;
import com.pollub.ikms.ikms_mobile.response.TokenResponse;
import com.pollub.ikms.ikms_mobile.utils.UrlManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import lombok.SneakyThrows;

/**
 * Created by ATyKondziu on 17.11.2017.
 */

public class LoginService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "LoginService";

    private String username;
    private String password;

    private String token;

    private ResponseEntity<TokenResponse> tokenResponse;

    public LoginService() {
        super(TAG);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service Started!");

        username = intent.getExtras().getString("username");
        password = intent.getExtras().getString("password");
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle loginBundle = new Bundle();
        receiver.send(STATUS_RUNNING,loginBundle);
        try {
            token = getAuthorizationToken(receiver).getToken();
            if (!token.isEmpty()) {
                loginBundle.putString("result", token);
                receiver.send(STATUS_FINISHED, loginBundle);
            }
        } catch (Exception e) {
            loginBundle.putString(Intent.EXTRA_TEXT,e.getMessage());
            receiver.send(STATUS_ERROR, loginBundle);
        }

        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    @SneakyThrows
    private TokenResponse getAuthorizationToken(ResultReceiver receiver) {
        final String url = UrlManager.getInstance().AUTH_LOGIN_URL;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        LoginRequest loginRequest = new LoginRequest();
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest);
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        tokenResponse = restTemplate.exchange(url, HttpMethod.POST, entity, TokenResponse.class);

        if (tokenResponse.getStatusCode().value() == 200) {
            return tokenResponse.getBody();
        } else {
            throw new AuthorizationException("Nie udało się zalogować");
        }
    }


    public class AuthorizationException extends Exception {

        public AuthorizationException(String message) {
            super(message);
        }

        public AuthorizationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
