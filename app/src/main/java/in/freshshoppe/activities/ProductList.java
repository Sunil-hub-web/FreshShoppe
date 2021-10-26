package in.freshshoppe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import in.freshshoppe.adapters.ItemAdapter;
import in.freshshoppe.extras.Constants;
import in.freshshoppe.extras.ServerLinks;
import in.freshshoppe.extras.SessionManager;
import in.freshshoppe.extras.SharedPreference;
import in.freshshoppe.extras.ViewDialog;
import in.freshshoppe.models.CartItem;
import in.freshshoppe.models.ItemGetSet;
import in.freshshoppe.models.VariationGetSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static RecyclerView recyle_iem;
    ArrayList<ItemGetSet> itemArraylist;
    ArrayList<VariationGetSet> variations;
    TextView title;
    public static TextView itemcounter;
    SessionManager session;
    Spinner itemfolter;
    ViewDialog progressbar;
    ArrayList<String> filternamearray = new ArrayList<String>();
    HashMap<String, String> filteridid = new HashMap<String, String>();
    String sub_cate_id;
    ImageView backicon, searchicon, closeicon;
    RelativeLayout cart_layout;
    SwipeRefreshLayout swipeLayout;
    public static ImageView noconnection, noproductfound;
    RelativeLayout searchlay;
    ItemAdapter adpater;
    EditText searchtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);
        progressbar = new ViewDialog(this);

        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        noproductfound = findViewById(R.id.noproductfound);
        closeicon = findViewById(R.id.closeicon);
        searchlay = findViewById(R.id.searchlay);
        searchicon = findViewById(R.id.searchicon);
        title = findViewById(R.id.title);
        recyle_iem = findViewById(R.id.recyle_iem);
        itemcounter = findViewById(R.id.itemcounter);
        itemfolter = findViewById(R.id.itemfolter);
        searchtxt = findViewById(R.id.searchtxt);

        sub_cate_id = getIntent().getStringExtra("id");
        title.setText(getIntent().getStringExtra("title"));
        for (int a = 0; a < Constants.itemfilters.size(); a++) {

            filternamearray.add(Constants.itemfilters.get(a).getSubcate_name());
            filteridid.put(Constants.itemfilters.get(a).getSubcate_name(), Constants.itemfilters.get(a).getSubcate_id());

        }

        ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(ProductList.this,
                R.layout.spinnerdropdownitem, filternamearray);
        dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
        itemfolter.setAdapter(dataAdapterVehicle);

        itemfolter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String del = itemfolter.getItemAtPosition(itemfolter.getSelectedItemPosition()).toString();

                sub_cate_id = filteridid.get(del);

                getHomeData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getApplicationContext(), "Select Your City", Toast.LENGTH_SHORT).show();
            }
        });

        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchlay.setVisibility(View.VISIBLE);
            }
        });

        closeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adpater.getFilter().filter("");
                searchlay.setVisibility(View.GONE);
            }
        });

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

        searchtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0){
                    adpater.getFilter().filter("");
                }else{
                    adpater.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        noconnection.setVisibility(View.GONE);
        recyle_iem.setVisibility(View.GONE);
        getHomeData();
        ItemCounter();
    }

    public void initiateView() {
        getHomeData();
        ItemCounter();
    }

    @Override
    public void onRefresh() {

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


                                itemArraylist = new ArrayList<ItemGetSet>();
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
                                        Log.d("error_response", "" + variations.size());
                                        if (!products_id.equalsIgnoreCase("null")) {
                                            itemArraylist.add(new ItemGetSet(products_id, name, image, description, regular_price, sales_price, "", "", "", variations));
                                        }
                                    }

                                if(itemArraylist.size()==0){

                                    noconnection.setVisibility(View.GONE);
                                    searchicon.setVisibility(View.GONE);
                                    searchlay.setVisibility(View.GONE);
                                    recyle_iem.setVisibility(View.GONE);
                                    noproductfound.setVisibility(View.VISIBLE);

                                }else {
                                    setData();

                                    noconnection.setVisibility(View.GONE);
                                    recyle_iem.setVisibility(View.VISIBLE);
                                    searchicon.setVisibility(View.VISIBLE);
                                    noproductfound.setVisibility(View.GONE);
                                }

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
                            searchlay.setVisibility(View.GONE);
                            noproductfound.setVisibility(View.GONE);
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

    public void ItemCounter() {

        try {

            ArrayList<CartItem> favouritesBeanSampleList = new ArrayList<CartItem>();
            SharedPreference sharedPreference = new SharedPreference();
            favouritesBeanSampleList = sharedPreference.loadFavorites(ProductList.this);

            String ct = String.valueOf(favouritesBeanSampleList.size());
            Constants.cartitems = ct;

            itemcounter.setText(Constants.cartitems);

        } catch (Exception e) {

            Log.d("Bw3fev", "" + e);

        }

    }

    public void setData() {

        adpater = new ItemAdapter(itemArraylist, ProductList.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyle_iem.setLayoutManager(gridLayoutManager);
        recyle_iem.setItemAnimator(new DefaultItemAnimator());
        recyle_iem.setAdapter(adpater);
    }

}
