package com.pollub.ikms.ikms_mobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pollub.ikms.ikms_mobile.request.LoginRequest;
import com.pollub.ikms.ikms_mobile.response.TokenResponse;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

public class MainScreen extends AppCompatActivity {

    private Button zalogujButton;

    private TokenResponse token;

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        zalogujButton = (Button) findViewById(R.id.zaloguj_button);
        zalogujButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // musze poczytać o asynchronicznych operacjach w połączeniu ze spinnerem bo chyba nie działa
                spinner.setVisibility(View.VISIBLE);
                login(v);
            }
        });
    }

    public void login(View v){
        AuthorizationTokenTask authorizationTokenTask = new AuthorizationTokenTask();
        try {
            token = authorizationTokenTask.execute().get();
            //TODO: Przydałoby się jakiś error handler zrobic później
            if(!(token.getToken().equals("denied") || token.getToken().equals("error"))){
                redirectToMainUserPage(v);
            } else {
                Toast.makeText(getWindow().getContext(), "Zły username lub password", Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class AuthorizationTokenTask extends AsyncTask<Void, Void, TokenResponse> {

        private String username;
        private String password;


        @Override
        protected TokenResponse doInBackground(Void... params) {

            final String url = "https://ikmsdeploy.herokuapp.com/auth/login";
            RestTemplate restTemplate = new RestTemplate();
            TokenResponse token = new TokenResponse();
            try {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setUsername(username);
                loginRequest.setPassword(password);
                token = restTemplate.postForObject(url, loginRequest, TokenResponse.class);

                return token;
            } catch (HttpClientErrorException eh){
                token.setToken("denied");
            } catch (Exception e) {
                token.setToken("error");
            }
            return token ;
        }

        @Override
        protected void onPreExecute() {
            username = ((EditText)findViewById(R.id.username_value)).getText().toString();
            password = ((EditText) findViewById(R.id.password_value)).getText().toString();

        }

        @Override
        protected void onPostExecute(TokenResponse tokenResponse) {
            spinner.setVisibility(View.GONE);
        }
    }

    public void redirectToMainUserPage(View v){
        Intent intent = new Intent(this, MainUserPanelActivity.class);
        intent.putExtra("token", token.getToken());
        startActivityForResult(intent,1);
    }

}
