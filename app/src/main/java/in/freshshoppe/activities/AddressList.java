package in.freshshoppe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import in.freshshoppe.adapters.AddressAdapter;
import in.freshshoppe.extras.RecyclerTouchListener;
import in.freshshoppe.extras.ServerLinks;
import in.freshshoppe.extras.SessionManager;
import in.freshshoppe.extras.ViewDialog;
import in.freshshoppe.models.AddressGetSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddressList extends AppCompatActivity {

    SessionManager session;
    public static ViewDialog progressbar;
    ImageView backicon;
    RecyclerView address_list;
    public static int pos;
    public static ArrayList<AddressGetSet> addressarray = new ArrayList<AddressGetSet>();
    Button save_proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

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

        save_proceed = findViewById(R.id.save_proceed);
        save_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddAddress.class);
                startActivity(i);
            }
        });
        address_list = findViewById(R.id.address_list);
        address_list.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), address_list, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int post) {

                Log.d("gbrdsfbfbvdz", "clicked");

                AddressGetSet parenting = addressarray.get(post);

                pos = post;
            }

            @Override
            public void onLongClick(View view, int position) {

            }


        }));


    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressData();
    }

    public void getAddressData() {
        Log.d("error_response", session.getUserID());
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.addresslist_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {


                                addressarray = new ArrayList<AddressGetSet>();
                                JSONArray All_address = jsonObject.getJSONArray("All_address");
                                for(int n = 0; n< All_address.length(); n++){

                                    JSONObject productt_json = All_address.getJSONObject(n);
                                    String addres_id = productt_json.getString("addres_id");
                                    String name = productt_json.getString("name");
                                    String state_id = productt_json.getString("state_id");
                                    String state_name = productt_json.getString("state_name");
                                    String city_id = productt_json.getString("city_id");
                                    String city_name = productt_json.getString("city_name");
                                    String pin_id = productt_json.getString("pin_id");
                                    String pincode = productt_json.getString("pincode");
                                    String number = productt_json.getString("number");
                                    String address = productt_json.getString("address");


                                    addressarray.add(new AddressGetSet(addres_id, name, state_id, state_name, city_id, city_name, pin_id, pincode, number, address));
                                }
                                AddressAdapter adpater = new AddressAdapter(addressarray, AddressList.this);
                                address_list.setHasFixedSize(true);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddressList.this);
                                address_list.setLayoutManager(linearLayoutManager);
                                address_list.setItemAnimator(new DefaultItemAnimator());
                                address_list.setAdapter(adpater);

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
