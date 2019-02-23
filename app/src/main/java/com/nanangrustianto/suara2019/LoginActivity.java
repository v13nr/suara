package com.nanangrustianto.suara2019;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.nanangrustianto.suara2019.AppConfig;
import com.nanangrustianto.suara2019.app.AppController2;
import com.nanangrustianto.suara2019.R;

/**
 * Created by Nanang R on 20/10/2017.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private TextView btnLinkToRegister,skip,reset;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    //private SharedPreferences pref;

    SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(getApplicationContext());




        TextView tv = (TextView)findViewById(R.id.link_signup);

        //pref = getSharedPreferences("data", MODE_PRIVATE);

        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        //btnLinkToRegister = (TextView) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // ngecek apakah user udah login atau belum
        //pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        //final int login = pref.getInt("login", 0);
        /*if (login == 1) {
            // kalau user ternyata udah login langsung di lempar ke main activity tanpa harus login terlebih dahulu
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/

        if (sessionManager.isLogin()) {
            // mainactivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // ketika login button di klik
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // ngecek apakah inputannya kosong atau tidak
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // jika inputan kosong tampilkan pesan
                    Toast.makeText(getApplicationContext(),
                            "Jangan kosongkan email dan password!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link ke register activity
       /* btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

          *//*  public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }*//*
        });*/

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "Anda Akan Mendaftar sebagai saksi..!",
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), daftar.class);
                startActivity(intent);

            }
        });

    }


    private void checkLogin(final String email, final String password) {
        // Tag biasanya digunakan ketika ingin membatalkan request volley
        String tag_string_req = "req_login";
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // ngecek node error dari api
                    if (!error) {
                        // user berhasil login
                        String uid = jObj.getString("username");
                        String nama = jObj.getString("nama");
                        String email = jObj.getString("username");
                        String alamat = jObj.getString("username");



                        AppConfig.emailAktif = jObj.getString("username");
                        sessionManager.createLogin(uid, nama);
                        // buat session user yang sudah login yang menyimpan nama,apikey,alamat dan email
                        /*SharedPreferences.Editor editor = pref.edit();
                        editor.putString("apikey", uid);
                        editor.putString("nama",nama);
                        //editor.putString("email",email);
                        //editor.putString("alamat", alamat);
                        editor.putInt("login", 1);
                        editor.commit();*/

                        //jika sudah masuk ke mainactivity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // terjadi error dan tampilkan pesan error dari API
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                //cek error timeout, noconnection dan network error
                if ( error instanceof TimeoutError || error instanceof NoConnectionError ||error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(),
                            "Please Check Your Connection",
                            Toast.LENGTH_SHORT).show();}
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // kirim parameter ke server
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", email);
                params.put("password", password);

                return params;
            }
        };

        // menggunakan fungsi volley adrequest yang kita taro di appcontroller
        AppController2.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    //untuk menampilkan loading dialog
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}