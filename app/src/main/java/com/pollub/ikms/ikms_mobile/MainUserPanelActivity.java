package com.pollub.ikms.ikms_mobile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pollub.ikms.ikms_mobile.response.UserAccount;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

public class MainUserPanelActivity extends AppCompatActivity {

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_panel);

        if (savedInstanceState == null) {
            Bundle gettingToken = getIntent().getExtras();
            token = gettingToken.getString("token");
        } else
            token = "brak";

        FetchDataTask fetchDataTask = new FetchDataTask();
        try {
            UserAccount myUserAccount = fetchDataTask.execute().get().getBody();
            // TODO do usunięcia po zmianie endpointów kuchni
            TextView textView = (TextView) findViewById(R.id.response_value);
                   textView.append("Nick: " + myUserAccount.getNick());
            textView.append("\n");
            textView.append("Email: "+ myUserAccount.getEmail());
            textView.append("\n");
            textView.append("Rola: "+ myUserAccount.getAuthorities());
            textView.append("\n");
            textView.append("Kraj: "+ myUserAccount.getCountry());
            textView.append("\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    //TODO Zmienić UserAccount w całym obrębie klasy FetchDataTask na dany response z IKMSu
    private class FetchDataTask extends AsyncTask<Void, Void, ResponseEntity<UserAccount>> {
        @Override
        protected ResponseEntity<UserAccount> doInBackground(Void... params) {
            try {
                //TODO Zmienić endpoint na IKMS
                String url = "http://wkitchen.eu-central-1.elasticbeanstalk.com/api/user/account/intelcan";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("X-Auth-Token", token);

                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<UserAccount> response = restTemplate
                        .exchange(url, HttpMethod.GET, entity, UserAccount.class);
                return response;

            } catch (Exception e) {
                String message = e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ResponseEntity<UserAccount> result) {
            HttpStatus status = result.getStatusCode();
            UserAccount userAccount = result.getBody();
        }

    }
}
