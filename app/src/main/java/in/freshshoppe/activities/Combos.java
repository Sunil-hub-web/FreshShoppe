package in.freshshoppe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import in.freshshoppe.R;
import in.freshshoppe.adapters.ComboListAdapter;
import in.freshshoppe.extras.Constants;
import in.freshshoppe.extras.ServerLinks;
import in.freshshoppe.extras.SessionManager;
import in.freshshoppe.extras.SharedPreference;
import in.freshshoppe.extras.ViewDialog;
import in.freshshoppe.models.CartItem;
import in.freshshoppe.models.ComboGetSet;
import in.freshshoppe.models.ItemGetSet;
import in.freshshoppe.models.VariationGetSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Combos extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyle_iem;

    ArrayList<VariationGetSet> variations;
    ArrayList<ComboGetSet> comboArraylist;
    TextView title;
    public static TextView itemcounter;
    SessionManager session;
    ViewDialog progressbar;
    ImageView backicon;
    String sub_cate_id;
    RelativeLayout cart_layout;
    SwipeRefreshLayout swipeLayout;
    ImageView noconnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combos);

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

        cart_layout = findViewById(R.id.cart_layout);
        cart_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!session.getUserID().equalsIgnoreCase("0")) {
                    Intent i = new Intent(getApplicationContext(), Cart.class);
                    startActivity(i);
//                } else {
//                    Toast.makeText(getApplicationContext(), "Login to view", Toast.LENGTH_SHORT).show();
//                    session.logoutUser();
//                }

            }
        });

        title = findViewById(R.id.title);
        itemcounter = findViewById(R.id.itemcounter);
        recyle_iem = findViewById(R.id.recyle_iem);

        title.setText(getIntent().getStringExtra("title"));
        sub_cate_id = getIntent().getStringExtra("id");

        noconnection = findViewById(R.id.noconnection);
        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeLayout.setRefreshing(true);
                initiateView();
                swipeLayout.setRefreshing(false);

            }
        });

    }

    public void initiateView() {
        getHomeData();
        ItemCounter();
    }

    @Override
    public void onRefresh() {

    }

    public void ItemCounter() {

        try {

            ArrayList<CartItem> favouritesBeanSampleList = new ArrayList<CartItem>();
            SharedPreference sharedPreference = new SharedPreference();
            favouritesBeanSampleList = sharedPreference.loadFavorites(Combos.this);

            String ct = String.valueOf(favouritesBeanSampleList.size());
            Constants.cartitems = ct;

            itemcounter.setText(Constants.cartitems);

        } catch (Exception e) {

            Log.d("Bw3fev", "" + e);

        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noconnection.setVisibility(View.GONE);
        recyle_iem.setVisibility(View.GONE);
        getHomeData();
        ItemCounter();
    }

    public void getHomeData() {
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.productlist_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {


                                comboArraylist = new ArrayList<ComboGetSet>();
                                JSONArray All_products = jsonObject.getJSONArray("All_Products");
                                for (int l = 0; l < All_products.length(); l++) {

                                    JSONObject product_json = All_products.getJSONObject(l);
                                    String products_id = product_json.getString("products_id");
                                    String name = product_json.getString("name");
                                    String image = product_json.getString("image");
                                    String description = product_json.getString("description");
                                    String regular_price = product_json.getString("regular_price");
                                    String sales_price = product_json.getString("sales_price");

                                    variations = new ArrayList<VariationGetSet>();
                                    JSONArray variationss = product_json.getJSONArray("variations");
                                    if (variationss.length() == 0) {

                                    } else {
                                        for (int m = 0; m < variationss.length(); m++) {
                                            JSONObject variation_json = variationss.getJSONObject(m);
                                            String price_id = variation_json.getString("price_id");
                                            String price = variation_json.getString("price");
                                            String varations = variation_json.getString("varations");

                                            variations.add(new VariationGetSet(price_id, price, varations));
                                        }
                                    }
                                    Log.d("error_response", name);
                                    Log.d("error_response", ""+variations.size());
                                    if (!products_id.equalsIgnoreCase("null")) {
                                        comboArraylist.add(new ComboGetSet(products_id, name, image, description, regular_price, sales_price, "", "", "", variations));
                                    }
                                }
                                setComboData();

                                noconnection.setVisibility(View.GONE);
                                recyle_iem.setVisibility(View.VISIBLE);

                                progressbar.hideDialog();

                            } else {
                                String msg = jsonObject.getString("message");

                                progressbar.hideDialog();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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


                            noconnection.setVisibility(View.VISIBLE);
                            recyle_iem.setVisibility(View.GONE);
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
                params.put("sub_cate_id", sub_cate_id);
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

//    public void getHomeData() {
//        progressbar.showDialog();
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.productlist_url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("fezgvsedzgv", response);
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String status = jsonObject.getString("success");
//                            if (status.equalsIgnoreCase("true")) {
//
//
//                                comboArraylist = new ArrayList<ComboGetSet>();
//                                JSONArray All_combo = jsonObject.getJSONArray("All_Combo_list");
//                                for(int n = 0; n< All_combo.length(); n++){
//
//                                    JSONObject productt_json = All_combo.getJSONObject(n);
//                                    String products_id = productt_json.getString("products_id");
//                                    String name = productt_json.getString("name");
//                                    String image = productt_json.getString("image");
//                                    String description = productt_json.getString("description");
//                                    String regular_price = productt_json.getString("regular_price");
//                                    String sales_price = productt_json.getString("sales_price");
//
//                                    variations = new ArrayList<VariationGetSet>();
//                                    JSONArray variationsss = productt_json.getJSONArray("variations");
//                                    if(variationsss.length()==0){
//
//                                    }else {
//                                        for (int o = 0; o < variationsss.length(); o++) {
//                                            JSONObject variationn_json = variationsss.getJSONObject(o);
//                                            String price_id = variationn_json.getString("price_id");
//                                            String price = variationn_json.getString("price");
//                                            String varations = variationn_json.getString("varations");
//
//                                            variations.add(new VariationGetSet(price_id, price, varations));
//                                        }
//                                    }
//
//                                    comboArraylist.add(new ComboGetSet(products_id, name, image, description, regular_price, sales_price, "0", "0", "0",variations));
//                                }
//                                setComboData();
//
//                                progressbar.hideDialog();
//
//                            } else {
//                                String msg = jsonObject.getString("message");
//
//
//                                progressbar.hideDialog();
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//
////
//                            }
//                        } catch (JSONException e) {
//                            progressbar.hideDialog();
//                            Log.d("error_response", String.valueOf(e));
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        progressbar.hideDialog();
//                        Log.d("error_response", error.toString());
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//
//                            Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
//                            // ...
//                        } else {
//                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
//                        }
//                        ;
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//
//                Log.d("paramsforhomeapi", "" + params);
//                return params;
//            }
//        };
//
//        stringRequest.setRetryPolicy(new
//                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        requestQueue.getCache().clear();
//        requestQueue.add(stringRequest);
//    }

    public void setComboData(){

        ComboListAdapter adpater = new ComboListAdapter(comboArraylist, Combos.this);
        recyle_iem.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Combos.this);
        recyle_iem.setLayoutManager(linearLayoutManager);
        recyle_iem.setItemAnimator(new DefaultItemAnimator());
        recyle_iem.setAdapter(adpater);


    }
}
