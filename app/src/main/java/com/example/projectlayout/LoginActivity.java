package com.example.projectlayout;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.util.Log;
=======
>>>>>>> remotes/github/Alex
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private RequestQueue requestQueue;
    private static String URL = "http://10.0.2.2:8080/myWant/login.php";
    private StringRequest request;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.btnLogin);

        requestQueue = Volley.newRequestQueue(this);


        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String mEmail = email.getText().toString().trim();
                String mPassword = password.getText().toString().trim();


                if (!mEmail.isEmpty() || !mPassword.isEmpty()){
                    Login(mEmail, mPassword);
                } else {
                    email.setError("Please enter your Email");
                    password.setError("Please enter your Password");
                }
            }
        });

    }

    //private void Login(final String email, final String password) {
    private void Login(final String email, final String password) {
        //login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")){

<<<<<<< HEAD
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
=======
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
>>>>>>> remotes/github/Alex
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    //String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    Toast.makeText(LoginActivity.this,
                                            "Login successful "
                                                    + email, Toast.LENGTH_SHORT).show();


                                }
                            }else{
                                if (success.equals("2")){
                                    Toast.makeText(LoginActivity.this,
                                            "Account not found",Toast.LENGTH_SHORT).show();
                                }
                                if (success.equals("0")){
                                    Toast.makeText(LoginActivity.this,
                                            "Incorrect Password",Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this,"Error"+ e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,"Error"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

}