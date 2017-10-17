package com.pollub.ikms.ikms_mobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pollub.ikms.ikms_mobile.request.LoginRequest;
import com.pollub.ikms.ikms_mobile.response.TokenResponse;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

public class MainScreen extends AppCompatActivity {

    private Button zalogujButton;

    private TokenResponse token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        zalogujButton = (Button) findViewById(R.id.zaloguj_button);
        zalogujButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AuthorizationTokenTask authorizationTokenTask = new AuthorizationTokenTask();
                try {
                    token = authorizationTokenTask.execute().get();
                    if(token.getToken() != null){
                        redirectToMainUserPage(v);
                    } else {
                        //TODO Nie działa bo wcześniej gdzieś łapie wyjątek w operacji asynchronicznej i wywala aplikacje po podaniu błędnych danych
                        Toast.makeText(getWindow().getContext(), "Zły username lub password", Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class AuthorizationTokenTask extends AsyncTask<Void, Void, TokenResponse> {

        private String username;
        private String password;

        @Override
        protected TokenResponse doInBackground(Void... params) {

            //TODO Zmienić endpoint na IKMS
            final String url = "http://wkitchen.eu-central-1.elasticbeanstalk.com/auth";
            RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setUsername(username);
                loginRequest.setPassword(password);
                TokenResponse token = restTemplate.postForObject(url, loginRequest, TokenResponse.class);

                return token;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            username = ((EditText)findViewById(R.id.username_value)).getText().toString();
            password = ((EditText) findViewById(R.id.password_value)).getText().toString();

        }

    }

    public void redirectToMainUserPage(View v){
        Intent intent = new Intent(this, MainUserPanelActivity.class);
        intent.putExtra("token", token.getToken());
        startActivityForResult(intent,1);
    }
}
