import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com..R;
import com.GlobalController;
import com.model.ApiRequestSingleton;
import com.model.LoginControl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    String TAG="MainActivity.class";
    private String MESSAGE;
    Button button;
    private static int SPLASH_TIME_OUT = 5000;
    GlobalController global;
    private LoginControl loginControl;

    private void initialization(){

      /*  button= (Button) findViewById(R.id.button_dummy);*/
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GETTING IP
        GlobalController.getIp(MainActivity.this);
        /*getIp();*/
        //GET USER ID



        initialization();



        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });*/

        global= (GlobalController) getApplicationContext();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
*/

            //@Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                // Intent i = new Intent(ActivitySplash.this, ActivityHome.class);
                //startActivity(i);

                if(global.isSignedIn(MainActivity.this)){

                    loginControl=LoginControl.getLoginControl();
                    loginControl.getUserId(MainActivity.this);
                    
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(MainActivity.this,JobSearchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    /*private void getIp(){
        StringRequest stringRequest= new StringRequest(Request.Method.GET, GlobalController.API_GET_IP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            *//*JSONObject jsonObject = new JSONObject(response);*//*
                           *//*JSONArray jsonArray=new JSONArray(response);*//*
                            JSONObject myObject = new JSONObject(response);
                            String ip=myObject.getString("ip");

                            GlobalController.divice_ip=ip;

                        } catch (JSONException e) {

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        boolean check=obj.has("message");

                        if(check){
                            MESSAGE=obj.getString("status");

                        }
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }




            }
        }){


           *//* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                // Removed this line if you dont need it or Use application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }*//*
        };

*//*  int socketTimeout = 50000;//50 seconds to wait for the response - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);*//*
        *//*ApiRequestSingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);*//*
        ApiRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }*/
}
