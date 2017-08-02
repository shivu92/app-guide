package com.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.R;
import com..GlobalController;
import com.model.ApiRequestSingleton;
import com.model.CurrentUserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;

    String email;
    String password;

    private  EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private String TAG="LoginActivity.this";

    private String MESSAGE;
    ProgressDialog progressDialog;

    private String LOGIN_TOKEN;


    private  String userEmail,userPassword,userName;

    private GlobalController globalController;
    private CurrentUserModel currentUserModel;
    /*private LoginControl loginControl;*/

    private void initialization(){
        _emailText= (EditText) findViewById(R.id.input_email);
        _passwordText= (EditText) findViewById(R.id.input_password);
        _loginButton= (Button) findViewById(R.id.btn_login);
        _signupLink= (TextView) findViewById(R.id.link_signup);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialization();

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }


    public void login() {


        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

         progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

         email = _emailText.getText().toString();
         password = _passwordText.getText().toString();



        // TODO: Implement your own authentication logic here.
        if (GlobalController.divice_ip!=null){
            GlobalController.getIp(LoginActivity.this);
        }
        makeJsonObjReq();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 1000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically

                /*this.finish();*/
            }
        }
    }

   /* @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        *//*oveTaskToBack(true);*//*
        fileList();
    }*/

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        /*finish();*/
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void makeJsonObjReq() {
        /*showProgressDialog();*/
        Map<String, String> params= new HashMap<String, String>();

        
        params.put("email_id", email);
        
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                GlobalController.API_SIGNIN, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                           


                            }


                        } catch (JSONException e) {
                            Log.e(GlobalController.LOG_JSonException_TAG, "JSONException:" + e);
                            Toast.makeText(getBaseContext(),"Login Failed!",Toast.LENGTH_SHORT).show();
                            
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getBaseContext(),"Login Failed!",Toast.LENGTH_SHORT).show();
                
                /*hideProgressDialog();*/
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }



        };

        // Adding request to request queue
       /* AppController.getInstance().addToRequestQueue(jsonObjReq,tag_json_obj);*/
        ApiRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

        
    }
}

