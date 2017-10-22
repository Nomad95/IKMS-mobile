package com.pollub.ikms.ikms_mobile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pollub.ikms.ikms_mobile.response.Employee;

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
            Employee myEmployee = fetchDataTask.execute().get().getBody();
            // TODO do usunięcia po zmianie endpointów kuchni
            TextView textView = (TextView) findViewById(R.id.response_value);
                   textView.append("ID: " + myEmployee.getId());
            textView.append("\n");
            textView.append("Rola: "+ myEmployee.getEmployeeRole());
            textView.append("\n");
            textView.append("Nip: "+ myEmployee.getNip());
            textView.append("\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    private class FetchDataTask extends AsyncTask<Void, Void, ResponseEntity<Employee>> {
        @Override
        protected ResponseEntity<Employee> doInBackground(Void... params) {
            try {
                //Pobieramy podstawowe dane o pracowniku z id 1 do testów
                String url = "https://ikmsdeploy.herokuapp.com//api/employee/1";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Auth-Token", token);

                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<Employee> response = restTemplate
                        .exchange(url, HttpMethod.GET, entity, Employee.class);
                return response;

            } catch (Exception e) {
                String message = e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ResponseEntity<Employee> result) {
            HttpStatus status = result.getStatusCode();
            Employee employee = result.getBody();
        }

    }
}
