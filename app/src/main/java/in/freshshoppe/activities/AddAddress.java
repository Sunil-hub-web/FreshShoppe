package in.freshshoppe.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import in.freshshoppe.R;
import in.freshshoppe.extras.ServerLinks;
import in.freshshoppe.extras.SessionManager;
import in.freshshoppe.extras.ViewDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class AddAddress extends AppCompatActivity {

    SessionManager session;
    public static ViewDialog progressbar;
    ImageView backicon;
    ArrayList<String> statelist = new ArrayList<String>();
    ArrayList<String> citylist = new ArrayList<String>();
    ArrayList<String> pinlist = new ArrayList<String>();
    HashMap<String, String> stateid = new HashMap<String, String>();
    HashMap<String, String> cityid = new HashMap<String, String>();
    HashMap<String, String> pinid = new HashMap<String, String>();
    EditText edt_name, edt_address, edt_phone;
    Spinner edt_state, edt_city, edt_pin;
    String stid = "", ctid = "", pnid = "";
    Button save_proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);
        progressbar = new ViewDialog(this);

        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edt_name = findViewById(R.id.edt_name);
        edt_address = findViewById(R.id.edt_address);
        edt_phone = findViewById(R.id.edt_phone);;

        edt_state = findViewById(R.id.edt_state);
        edt_city = findViewById(R.id.edt_city);
        edt_pin = findViewById(R.id.edt_pin);
        save_proceed = findViewById(R.id.save_proceed);

        save_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_name.getText().toString().trim().length()==0){
                    edt_name.setError("enter name");
                    edt_name.requestFocus();

                }else if(edt_address.getText().toString().trim().length()==0){
                    edt_address.setError("enter address");
                    edt_address.requestFocus();

                }else if(stid.length()==0){
                    Toast.makeText(AddAddress.this, "Select State", Toast.LENGTH_SHORT).show();

                }else if(ctid.length()==0){
                    Toast.makeText(AddAddress.this, "Select City", Toast.LENGTH_SHORT).show();

                }else if(pnid.length()==0){
                    Toast.makeText(AddAddress.this, "Select Pincode", Toast.LENGTH_SHORT).show();

                }else if(edt_phone.getText().toString().trim().length()==0){
                    edt_phone.setError("enter phone number");
                    edt_phone.requestFocus();

                }else if(edt_phone.getText().toString().trim().length()!=10){
                    edt_phone.setError("invalid number");
                    edt_phone.requestFocus();

                }else{
                    AddAddress_url();
                }
            }
        });

        edt_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String del  = edt_state.getItemAtPosition(edt_state.getSelectedItemPosition()).toString();

                stid = stateid.get(del);

                GetLocationsCity_url();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getApplicationContext(), "Select Your City", Toast.LENGTH_SHORT).show();
            }
        });

        edt_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String del  = edt_city.getItemAtPosition(edt_city.getSelectedItemPosition()).toString();

                ctid = cityid.get(del);

                GetLocationsPin_url();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getApplicationContext(), "Select Your City", Toast.LENGTH_SHORT).show();
            }
        });

        edt_pin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String del  = edt_pin.getItemAtPosition(edt_pin.getSelectedItemPosition()).toString();

                pnid = pinid.get(del);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getApplicationContext(), "Select Your City", Toast.LENGTH_SHORT).show();
            }
        });

        GetLocationsState_url();
    }

    public void GetLocationsState_url() {
        Log.d("error_response", session.getUserID());
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.GetLocations_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {
                                citylist = new ArrayList<>();
                                cityid = new HashMap<>();
                                pinlist = new ArrayList<>();
                                pinid = new HashMap<>();
                                statelist = new ArrayList<String>();
                                JSONArray All_loc = jsonObject.getJSONArray("All_loc");
                                for(int n = 0; n< All_loc.length(); n++){

                                    JSONObject productt_json = All_loc.getJSONObject(n);
                                    String state_id = productt_json.getString("state_id");
                                    String state_name = productt_json.getString("state_name");


                                    statelist.add(state_name);
                                    stateid.put(state_name, state_id);
                                }
                                ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(AddAddress.this,
                                        R.layout.spinnerdropdownitem, statelist);
                                dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                                edt_state.setAdapter(dataAdapterVehicle);

                                ArrayAdapter<String> dataAdapterVehicless = new ArrayAdapter<String>(AddAddress.this,
                                        R.layout.spinnerdropdownitem, citylist);
                                dataAdapterVehicless.setDropDownViewResource(R.layout.spinneritem);
                                edt_city.setAdapter(dataAdapterVehicless);

                                ArrayAdapter<String> dataAdapterVehiclesse = new ArrayAdapter<String>(AddAddress.this,
                                        R.layout.spinnerdropdownitem, pinlist);
                                dataAdapterVehiclesse.setDropDownViewResource(R.layout.spinneritem);
                                edt_pin.setAdapter(dataAdapterVehiclesse);

                                progressbar.hideDialog();

                            } else {
                                String msg = jsonObject.getString("message");


                                progressbar.hideDialog();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

//
                            }
                        } catch (JSONException e) {
                            progressbar.hideDialog();
                            Log.d("error_response", String.valueOf(e));
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressbar.hideDialog();
                        Log.d("error_response", error.toString());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                        ;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("paramsforhomeapi", "" + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void GetLocationsCity_url() {
        Log.d("error_response", session.getUserID());
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.GetLocations_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {

                                citylist = new ArrayList<>();
                                cityid = new HashMap<>();

                                pinlist = new ArrayList<>();
                                pinid = new HashMap<>();
                                statelist = new ArrayList<String>();
                                JSONArray All_loc = jsonObject.getJSONArray("All_loc");
                                for(int n = 0; n< All_loc.length(); n++){

                                    JSONObject productt_json = All_loc.getJSONObject(n);
                                    String state_id = productt_json.getString("state_id");
                                    String state_name = productt_json.getString("state_name");

                                    if(state_id.equalsIgnoreCase(stid)){
                                        JSONArray city_list = productt_json.getJSONArray("city_list");
                                        for(int o = 0; o< city_list.length(); o++) {

                                            JSONObject ct_json = city_list.getJSONObject(o);
                                            String city_id = ct_json.getString("city_id");
                                            String city_name = ct_json.getString("city_name");

                                            citylist.add(city_name);
                                            cityid.put(city_name, city_id);
                                        }
                                    }


                                }

                                ArrayAdapter<String> dataAdapterVehicless = new ArrayAdapter<String>(AddAddress.this,
                                        R.layout.spinnerdropdownitem, citylist);
                                dataAdapterVehicless.setDropDownViewResource(R.layout.spinneritem);
                                edt_city.setAdapter(dataAdapterVehicless);

                                ArrayAdapter<String> dataAdapterVehiclesse = new ArrayAdapter<String>(AddAddress.this,
                                        R.layout.spinnerdropdownitem, pinlist);
                                dataAdapterVehiclesse.setDropDownViewResource(R.layout.spinneritem);
                                edt_pin.setAdapter(dataAdapterVehiclesse);

                                progressbar.hideDialog();

                            } else {
                                String msg = jsonObject.getString("message");


                                progressbar.hideDialog();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

//
                            }
                        } catch (JSONException e) {
                            progressbar.hideDialog();
                            Log.d("error_response", String.valueOf(e));
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressbar.hideDialog();
                        Log.d("error_response", error.toString());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                        ;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("paramsforhomeapi", "" + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void GetLocationsPin_url() {
        Log.d("error_response", session.getUserID());
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.GetLocations_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {


                                pinlist = new ArrayList<>();
                                pinid = new HashMap<>();

                                statelist = new ArrayList<String>();
                                JSONArray All_loc = jsonObject.getJSONArray("All_loc");
                                for(int n = 0; n< All_loc.length(); n++){

                                    JSONObject productt_json = All_loc.getJSONObject(n);
                                    String state_id = productt_json.getString("state_id");
                                    String state_name = productt_json.getString("state_name");

                                    if(state_id.equalsIgnoreCase(stid)){
                                        JSONArray city_list = productt_json.getJSONArray("city_list");
                                        for(int o = 0; o< city_list.length(); o++) {

                                            JSONObject ct_json = city_list.getJSONObject(o);
                                            String city_id = ct_json.getString("city_id");
                                            String city_name = ct_json.getString("city_name");

                                            if(city_id.equalsIgnoreCase(ctid)){
                                                JSONArray pincode_list = ct_json.getJSONArray("pincode_list");
                                                for(int p = 0; p< pincode_list.length(); p++) {

                                                    JSONObject pn_json = pincode_list.getJSONObject(p);
                                                    String pin_id = pn_json.getString("pin_id");
                                                    String pincode = pn_json.getString("pincode");


                                                    pinlist.add(pincode);
                                                    pinid.put(pincode, pin_id);
                                                }

                                            }


                                        }
                                    }
                                }
                                ArrayAdapter<String> dataAdapterVehiclesse = new ArrayAdapter<String>(AddAddress.this,
                                        R.layout.spinnerdropdownitem, pinlist);
                                dataAdapterVehiclesse.setDropDownViewResource(R.layout.spinneritem);
                                edt_pin.setAdapter(dataAdapterVehiclesse);

                                progressbar.hideDialog();

                            } else {
                                String msg = jsonObject.getString("message");


                                progressbar.hideDialog();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

//
                            }
                        } catch (JSONException e) {
                            progressbar.hideDialog();
                            Log.d("error_response", String.valueOf(e));
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressbar.hideDialog();
                        Log.d("error_response", error.toString());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                        ;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("paramsforhomeapi", "" + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void AddAddress_url() {
        Log.d("error_response", session.getUserID());
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.AddAddress_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {

                                finish();

                                progressbar.hideDialog();

                            } else {
                                String msg = jsonObject.getString("message");


                                progressbar.hideDialog();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

//
                            }
                        } catch (JSONException e) {
                            progressbar.hideDialog();
                            Log.d("error_response", String.valueOf(e));
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressbar.hideDialog();
                        Log.d("error_response", error.toString());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                        ;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", session.getUserID());
                params.put("name", edt_name.getText().toString());
                params.put("state_id", stid);
                params.put("city_id", ctid);
                params.put("pincode", pnid);
                params.put("number", edt_phone.getText().toString());
                params.put("address", edt_address.getText().toString());
                Log.d("paramsforhomeapi", "" + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}
