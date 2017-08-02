import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.global.GlobalController;
import com.main.HomeActivity;
import com.main.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginControl extends Application {

    private static LoginControl loginControl=null;
    private GlobalController globalController;
    private CurrentUserModel currentUserModel;

    private String userEmail;
    private String userName;
    private String userPassword;
    private String status;
    private String TAG="LoginControl.java";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    private LoginControl(){

    }

    public static LoginControl getLoginControl(){
        if (loginControl==null){
            loginControl=new LoginControl();
        }
        return loginControl;
    }

    public void getUserId(Context context){

        globalController= new GlobalController();


        currentUserModel=globalController.getCurrentUserDetails(context);
        userEmail=currentUserModel.getUserEmail();
        userPassword=currentUserModel.getUserPassword();
        userName=currentUserModel.getUserName();

        LoginReq(context);

    }


    private void LoginReq(Context context) {

        Map<String, String> params= new HashMap<String, String>();

        params.put("key",GlobalController.API_KEY);
        params.put("email_id", userEmail);
        params.put("password",userPassword);
       
       


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                GlobalController.API_SIGNIN, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {



                            }


                        } catch (JSONException e) {
                            Log.e(GlobalController.LOG_JSonException_TAG, "JSONException:" + e);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
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

       
        ApiRequestSingleton.getInstance(context).addToRequestQueue(jsonObjReq);

        ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }
}

