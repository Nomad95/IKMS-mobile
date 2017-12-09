package com.pollub.ikms.ikms_mobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.pollub.ikms.ikms_mobile.model.PhoneNumberItemModel;
import com.pollub.ikms.ikms_mobile.response.NotificationGroupedBySender;
import com.pollub.ikms.ikms_mobile.response.PhoneNumberResponse;
import com.pollub.ikms.ikms_mobile.utils.UrlManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PhoneCallActivity extends AppCompatActivity {

    private ArrayList<PhoneNumberItemModel> phoneNumberArrayList = new ArrayList<>();
    private ListView lv;
    private PhoneNumberAdapter adapter;
    private String message;
    private SharedPreferences prefs;
    private String token;
    private String tokenKey = "com.pollub.ikms.ikms_mobile.token";

    private PhoneNumberResponse[] phoneNumbers;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);
        prefs = this.getSharedPreferences(
                "com.pollub.ikms.ikms_mobile", Context.MODE_PRIVATE);
        token = prefs.getString(tokenKey, "");

        GetPhoneNumbersTask getPhoneNumbersTask = new GetPhoneNumbersTask();

        try {
            phoneNumbers = getPhoneNumbersTask.execute().get().getBody();
            phoneNumberArrayList = new ArrayList<>();
            for (PhoneNumberResponse number : phoneNumbers) {
                phoneNumberArrayList.add(new PhoneNumberItemModel(
                        number.getUserId(),
                        number.getFullName(),
                        number.getPhoneNumber()
                ));
            }
            Log.i("SUCCESS", "phone numbers were fetched successfully: " + phoneNumberArrayList);

            lv = (ListView) findViewById(R.id.phoneNumbers);
            adapter = new PhoneNumberAdapter(this, 0, phoneNumberArrayList);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener((adapterView, view, index, l) -> {
                String phoneNumber = phoneNumberArrayList.get(index).getNumber().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            });
        } catch (InterruptedException e) {
            Log.e("INTERRUPTED", "Error: interrupted getPhoneNumbers task in PhoneCallActivity class");
            message = e.getMessage();
        } catch (ExecutionException e) {
            Log.e("ERROR", "execution error in getPhoneNumbers task in PhoneCallActivity class");
            message = e.getMessage();
        }
    }

    class GetPhoneNumbersTask extends AsyncTask<Void, Void, ResponseEntity<PhoneNumberResponse[]>> {
        @Override
        protected ResponseEntity<PhoneNumberResponse[]> doInBackground(Void... params) {
            try {
                String url = UrlManager.PHONE_NUMBERS_URL;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Auth-Token", token);

                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<PhoneNumberResponse[]> response = restTemplate
                        .exchange(url, HttpMethod.GET, entity, PhoneNumberResponse[].class);
                return response;

            } catch (Exception e) {
                Log.e("FETCH_ERROR", "Error while fetching phone number data from server.");
                String message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ResponseEntity<PhoneNumberResponse[]> result) {
            super.onPostExecute(result);
            HttpStatus status = result.getStatusCode();
            PhoneNumberResponse[] numbers = result.getBody();
        }
    }
}
