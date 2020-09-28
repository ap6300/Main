package com.example.projectlayout.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectlayout.MainActivity;
import com.example.projectlayout.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends Fragment {

    private EditText email;
    private EditText password;
    private Button login;
    private TextView register;
    private RequestQueue requestQueue;
    //private static String URL = "http://10.0.2.2:8080/myWant/login.php";
    //For alex
    private static String URL = "http://192.168.1.120:80/mywant/login.php";
    private StringRequest request;



    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.activity_login, container, false);

        email = (EditText) root.findViewById(R.id.etEmail);
        password = (EditText) root.findViewById(R.id.etPassword);
        login = (Button) root.findViewById(R.id.btnLogin);
        register = (TextView) root.findViewById(R.id.register);

        requestQueue = Volley.newRequestQueue(getContext());


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

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //For sean
                //openlink("http://10.0.2.2:8080/myWant/Registration.html");
                //For Alex
                openlink("http://192.168.1.120/mywant/Registration.html");
            }
        });

        return root;
    }
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

                                //Do stuff here after successful login

                                startActivity(new Intent(getContext(), MainActivity.class));

                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    //String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    Toast.makeText(getContext(),"Login successful "+ email, Toast.LENGTH_SHORT).show();
                                }
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("Check", "Login");
                                editor.apply();
                                editor.commit();

                            }else{
                                if (success.equals("2")){
                                    Toast.makeText(getContext(),
                                            "Account not found",Toast.LENGTH_SHORT).show();
                                }
                                if (success.equals("0")){
                                    Toast.makeText(getContext(),
                                            "Incorrect Password",Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"Error"+ e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Error"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    private void openlink(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
