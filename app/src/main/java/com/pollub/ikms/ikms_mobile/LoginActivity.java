package com.pollub.ikms.ikms_mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pollub.ikms.ikms_mobile.data.DBHelper;
import com.pollub.ikms.ikms_mobile.data.DatabaseManager;
import com.pollub.ikms.ikms_mobile.receiver.RequestResultReceiver;
import com.pollub.ikms.ikms_mobile.service.LoginService;
import com.pollub.ikms.ikms_mobile.utils.UrlManager;

import static com.pollub.ikms.ikms_mobile.utils.constants.StatusCode.STATUS_ERROR;
import static com.pollub.ikms.ikms_mobile.utils.constants.StatusCode.STATUS_FINISHED;
import static com.pollub.ikms.ikms_mobile.utils.constants.StatusCode.STATUS_RUNNING;

public class LoginActivity extends AppCompatActivity implements RequestResultReceiver.Receiver {

    private Button buttonLogin;

    private String token = "";

    private RequestResultReceiver receiver;

    private String username;
    private String password;

    private ProgressDialog progressDialog;

    private SharedPreferences prefs;

    private String tokenKey = "com.pollub.ikms.ikms_mobile.token";
    private static Context context;
    private static DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    context=getApplicationContext();
        dbHelper = new DBHelper(context);
        DatabaseManager.initializeInstance(dbHelper);
        prefs = this.getSharedPreferences(
                "com.pollub.ikms.ikms_mobile", Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Proszę czekać..");
        progressDialog.setTitle("Logowanie");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        token = prefs.getString(tokenKey, "");
        if(token.length()>10){

            progressDialog.show();
            //redirectToMyNotifications();
            redirectToMainMenuActivity();
        }
        buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    username = ((EditText) findViewById(R.id.username_value)).getText().toString();
                    password = ((EditText) findViewById(R.id.password_value)).getText().toString();

                    /* Starting Login Service */
                    receiver = new RequestResultReceiver(new Handler());
                    receiver.setReceiver(LoginActivity.this);
                    Intent intent = new Intent(Intent.ACTION_SYNC, null, LoginActivity.this, LoginService.class);

                /* Send optional extras to Login IntentService */
                    intent.putExtra("url", UrlManager.AUTH_LOGIN_URL);
                    intent.putExtra("receiver", receiver);
                    intent.putExtra("requestId", 101);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);

                    startService(intent);
                } else {
                    Toast.makeText(getWindow().getContext(), "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case STATUS_RUNNING:
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Logowanie");
                progressDialog.setMessage("Proszę czekać..");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(true);
                progressDialog.show();
                break;

            case STATUS_FINISHED:
                token = resultData.getString("result");
                redirectToMyNotifications();
                break;

            case STATUS_ERROR:
                String statusCode = resultData.getString(Intent.EXTRA_TEXT);
                progressDialog.dismiss();
                Toast.makeText(this, "Niepoprawne dane logowania", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void redirectToMyNotifications() {
        Intent intent = new Intent(this, MyNotificationsListActivity.class);
        prefs.edit().putString(tokenKey, token).apply();
        startActivityForResult(intent, 1);
        finish();
    }
    public void redirectToMainMenuActivity() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        prefs.edit().putString(tokenKey, token).apply();
        startActivityForResult(intent, 1);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

}
