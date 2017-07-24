import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.model.ApiRequestSingleton;
import com.model.CurrentUserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
  
  
  public class GlobalController extends Application {
	  //api calls url
	    private static final String API_URI="dslfnlkndl";
		public static final String API_GET_IP =API_URI+"get_somthing";
		
		
		//GLOBAL SHARING METHOD AREA
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences=null;
    private int PRIVATE_MODE=0;
    private final String PREFERANCE_NAME="APP_NAME";

    //GLOBAL SHARING METHOD AREA KEYS

    public static final String KEY_USEREAMIL="USER_EMAIL";
    public static final String KEY_PASSWORD="USER_PASSWORD";
    public static final String KEY_USERNAME="USER_NAME";
	
	//LOGIN CHECK
	public static final String IS_LOGIN = "IODHDT";
	
	//STORING USER INFORMATION INTO SHARED PREFERENCE
	 public void localStoreLoginInfo(Context context ,String email,String password,String name){
        String encryptUsermail;
        String encryptUserPassword;
        String encryptUserName;

        try {
            encryptUsermail=strinChainEncrypt(email);
            encryptUserPassword=strinChainEncrypt(password);
            encryptUserName=strinChainEncrypt(name);


            sharedPreferences=getApplicationContext().getSharedPreferences(PREFERANCE_NAME,PRIVATE_MODE);
            editor=sharedPreferences.edit();
            editor.putBoolean(IS_LOGIN,true);

            editor.putString(KEY_USEREAMIL,encryptUsermail);
            editor.putString(KEY_PASSWORD,encryptUserPassword);
            editor.putString(KEY_USERNAME,encryptUserName);

            editor.commit();

        }
        catch (Exception e){

        }

    }
	
	//GETTING USER DATA FROM SHARED PREFERENCE
	 public CurrentUserModel getCurrentUserDetails(Context context){



        String decriptEmail;
        String decriptPassword;
        String decriptName;
        boolean is_login;

        CurrentUserModel obj= new CurrentUserModel();

        try {

            sharedPreferences=context.getSharedPreferences(PREFERANCE_NAME,PRIVATE_MODE);
            decriptEmail=stringChainDecrypt(sharedPreferences.getString(KEY_USEREAMIL,null));
            decriptPassword=stringChainDecrypt(sharedPreferences.getString(KEY_PASSWORD,null));
            decriptName=stringChainDecrypt(sharedPreferences.getString(KEY_USERNAME,null));


            is_login=sharedPreferences.getBoolean(IS_LOGIN,true);
            obj.setIs_login(is_login);
            obj.setUserEmail(decriptEmail);
            obj.setUserPassword(decriptPassword);
            obj.setUserName(decriptName);



        }catch (Exception e){

        }
        return obj;
    }

   //CHECKING lOGIN OR NOT
    public boolean isSignedIn(Context context){

        sharedPreferences=context.getSharedPreferences(PREFERANCE_NAME,PRIVATE_MODE);
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }
	
	 
	 //SINGOUT PROCESS
    public void signOut(Context context){
        //new AsyncTaskUnregisterGCM(context).execute();
        //storeRegistrationId("");
        sharedPreferences = context.getSharedPreferences(PREFERANCE_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Login_SessionId=null;

        Toast.makeText(context,"User Log Out Successfully",Toast.LENGTH_LONG).show();


    }
	
	
	//STRING ENCRYPTION AND DECRYPTION
    private String strinChainEncrypt(String string){

        return chainEncrypt(chainEncrypt(string));


    }

    private String stringChainDecrypt(String string){
        return chainDecrypt(chainDecrypt(string));
    }



    private String chainEncrypt(String string){
        try {
            return encrypt(encrypt(encrypt(encrypt(string))));
        } catch (Exception e) {
            return "";
        }
    }
    private String chainDecrypt(String string){
        try {
            return decrypt(decrypt(decrypt(decrypt(string))));
        } catch (Exception e) {
            return "";
        }
    }
//Emcrypstion and decryption
    private final String ALGORITHM = "AES";
	//16 CHARECTORS 
    private final byte[] keyValue = new byte[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P' };

    @SuppressLint("TrulyRandom")
    private String encrypt(String valueToEnc) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = valueToEnc.getBytes("UTF-8");
        String encryptedValue = Base64.encodeToString(encValue, Base64.DEFAULT);

        return encryptedValue;
    }

    private String decrypt(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);

        byte[] data = Base64.decode(encryptedValue, Base64.DEFAULT);
        String decryptedValue = new String(data, "UTF-8");
        return decryptedValue;
    }

    private Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        return key;
    }

 //STRING REQUIST
    public static void getIp(Context context){
        StringRequest stringRequest= new StringRequest(Request.Method.GET, GlobalController.API_GET_IP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            /*JSONObject jsonObject = new JSONObject(response);*/
                           /*JSONArray jsonArray=new JSONArray(response);*/
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
                           String MESSAGE=obj.getString("status");

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



        };


        ApiRequestSingleton.getInstance(context).addToRequestQueue(stringRequest);

    }


  }
  
  //ANDROID MANIFEST
  <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
	//DEFINE GLOBAL CONTROLLER 
        android:name=".global.GlobalController"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".main.MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
		<activity
            android:name=".main.SomeActivity"
            android:label="@string/title_activity_some_search"
            android:theme="@style/AppTheme.NoActionBar"
			//HIDDING KEYBOARD
            android:windowSoftInputMode="stateAlwaysHidden" />
        <activity android:name=".main.NetworkInfo" />
		
		</application>
  
  
  
  
  
  //CurrentUserModel  CLASS
