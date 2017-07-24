 
 //json Array request
 private void getJObs() {
        Map<String, String> params= new HashMap<String, String>();

        params.put("key", GlobalController.API_KEY);
        params.put("ip", GlobalController.divice_ip);
       
        /* params.put("id", GlobalController.appLoginID);*/





        JsonArrayRequest req = new JsonArrayRequest(GlobalController.API_GET_LIST,new JSONObject(params),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("12",response.toString());


                        try {
                            // Parsing json array response
                            // loop through each json object

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject job = (JSONObject) response
                                        .get(i);

                                GetDataAdapter GetDataAdapter2 = new GetDataAdapter();
                                if(job.has("status")){


                                    GetDataAdapter2.setJobId(job.getInt("job_id"));

                                    GetDataAdapter2.setJobTitle(job.getString("job_title"));

                                    GetDataAdapter2.setCompanyName(job.getString("company_name"));
                                    GetDataAdapter2.setImageServerUrl(job.getString("logo"));



                                    GetDataAdapter2.setExperienceMin(job.getString("experience_years_f"));
                                    GetDataAdapter2.setExperienceMax(job.getString("experience_years_t"));

                                    GetDataAdapter2.setActive(job.getString("last_active"));
                                    GetDataAdapter2.setImageServerUrl("http://365innovative.com/img/logo.png");
                                    GetDataAdapter2.setPayscaleMin(job.getString("payscale_from"));
                                    GetDataAdapter2.setPayscaleMax(job.getString("payscale_to"));

                                    List<String> skills=new ArrayList<String>();
                                    List<String> locations=new ArrayList<String>();


                                    JSONArray jArray=job.getJSONArray("skill");
                                    if (jArray != null) {
                                        for (int k=0;k<jArray.length();k++){
                                            JSONObject skill = (JSONObject) jArray
                                                    .get(k);
                                            skills.add(skill.getString("skill_name"));


                                        }
                                    }

                                    JSONArray jArray1=job.getJSONArray("location");
                                    if (jArray != null) {
                                        for (int k=0;k<jArray1.length();k++){
                                            JSONObject location = (JSONObject) jArray1
                                                    .get(k);
                                            locations.add(location.getString("location_name"));


                                        }
                                    }

                                    GetDataAdapter2.setLocation(locations);

                                    GetDataAdapter2.setSkills(skills);


                                    GetDataAdapter1.add(GetDataAdapter2);
                                }

                            }
                            recyclerViewadapter = new RecyclerViewAdapter(GetDataAdapter1,HomeActivity.this);

                            recyclerView.setAdapter(recyclerViewadapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Intent intent=new Intent(HomeActivity.this,NetworkInfo.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Intent intent=new Intent(HomeActivity.this,NetworkInfo.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                /*Log.e(TAG,error.getMessage());*/

            }
        });

        // Adding request to request queue
        ApiRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }
	
	//json Request
	private void makeJsonObjReq() {
        /*showProgressDialog();*/
        Map<String, String> params= new HashMap<String, String>();

        params.put("key",GlobalController.API_KEY);
        params.put("email_id", email);
        params.put("password",password);
        params.put("ip", GlobalController.divice_ip);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                GlobalController.API_SIGNIN, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            boolean check=response.has("status");

                            if(check){
                                MESSAGE=response.getString("status");

                                /*if (MESSAGE.equals("LS")){

                                    String id=response.getString("id");
                                    String name=response.getString("name");

                                     if(response.has("id")){

                                         globalController= (GlobalController) getApplication();

                                          GlobalController.appLoginID=response.getString("id");
                                         globalController.localStoreLoginInfo(LoginActivity.this,email,password,name);
                                         progressDialog.dismiss();
                                         Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                         intent.putExtra("USER_ID", GlobalController.appLoginID);
                                         startActivity(intent);
                                         finish();

                                     }
                                     else {
                                         Toast.makeText(getBaseContext(),"Login Failed!",Toast.LENGTH_SHORT).show();
                                         startActivity(new Intent(LoginActivity.this,JobSearchActivity.class));

                                     }



                                }*/

                                switch(MESSAGE){
                                    case "LS":
                                        String id=response.getString("id");
                                        String name=response.getString("name");
                                        globalController= (GlobalController) getApplication();

                                        GlobalController.appLoginID=response.getString("id");
                                        globalController.localStoreLoginInfo(LoginActivity.this,email,password,name);

                                        progressDialog.dismiss();
                                        Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("USER_ID", GlobalController.appLoginID);
                                        startActivity(intent);
                                        finish();
                                        break;  //optional
                                    case "ENV":
                                        Toast.makeText(getBaseContext(),"Account Inactive, please verify your email_address to activate your account.", Toast.LENGTH_LONG).show();
                                        break;  //optional

                                    case "AF":
                                        Toast.makeText(getBaseContext(),"Invalid Usename or Password.", Toast.LENGTH_LONG).show();
                                        break;  //optional

                                    case "AB":
                                        Toast.makeText(getBaseContext(),"account blocked", Toast.LENGTH_LONG).show();
                                        break;
                                    default:
                                        Toast.makeText(getBaseContext(),"Login Failed!",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this,JobSearchActivity.class));
                                }
                                /*Toast.makeText(SignupActivity.this,MESSAGE,Toast.LENGTH_LONG).show();
                                System.out.println(MESSAGE);*/
                            }else{


                               /* Intent intent=new Intent(SignupActivity.this,ActivityEventsList.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);*/

                                Intent intent=new Intent(LoginActivity.this,NetworkInfo.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);



                            }


                        } catch (JSONException e) {
                            Log.e(GlobalController.LOG_JSonException_TAG, "JSONException:" + e);
                            Toast.makeText(getBaseContext(),"Login Failed!",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this,NetworkInfo.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getBaseContext(),"Login Failed!",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(LoginActivity.this,NetworkInfo.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }
}

