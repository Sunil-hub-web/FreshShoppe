package in.freshshoppe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.freshshoppe.R;
import in.freshshoppe.adapters.ItemAdapter;
import in.freshshoppe.adapters.SearchAdapter;
import in.freshshoppe.extras.ServerLinks;
import in.freshshoppe.extras.SessionManager;
import in.freshshoppe.extras.ViewDialog;
import in.freshshoppe.models.ItemGetSet;
import in.freshshoppe.models.VariationGetSet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    SessionManager session;
    EditText searchtxt;
    RecyclerView searchitemsrecycler;
    ArrayList<String> filternamearray = new ArrayList<String>();
    HashMap<String, String> filteridid = new HashMap<String, String>();
    ArrayList<ItemGetSet> itemArraylist;
    ArrayList<VariationGetSet> variations;
    String keyword;
    ImageView noproductfound, noconnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        session = new SessionManager(this);

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchitemsrecycler = findViewById(R.id.searchitemsrecycler);
        searchtxt = findViewById(R.id.searchtxt);
        noproductfound = findViewById(R.id.noproductfound);
        noconnection = findViewById(R.id.noconnection);

        searchtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0 || s==null){
                    searchitemsrecycler.setVisibility(View.GONE);
                    keyword = "";
                }else{
                    keyword = String.valueOf(s);
                    searchItemData();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void searchItemData() {

        noconnection.setVisibility(View.GONE);
        searchitemsrecycler.setVisibility(View.GONE);
        noproductfound.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.SearchProduct_url,
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
                                    Log.d("error_response", ""+variations.size());
                                    if (!products_id.equalsIgnoreCase("null")) {
                                        itemArraylist.add(new ItemGetSet(products_id, name, image, description, regular_price, sales_price, "", "", "", variations));
                                    }
                                }

                                if(itemArraylist.size()==0){

                                    noconnection.setVisibility(View.GONE);
                                    searchitemsrecycler.setVisibility(View.GONE);
                                    noproductfound.setVisibility(View.VISIBLE);

                                }else {
                                    setData();

                                    noconnection.setVisibility(View.GONE);
                                    searchitemsrecycler.setVisibility(View.VISIBLE);
                                    noproductfound.setVisibility(View.GONE);
                                }


                                setData();

                                searchitemsrecycler.setVisibility(View.VISIBLE);

                            } else {
                                String msg = jsonObject.getString("message");


                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                            Log.d("error_response", String.valueOf(e));
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Log.d("error_response", error.toString());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            noconnection.setVisibility(View.VISIBLE);
                            searchitemsrecycler.setVisibility(View.GONE);
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
                params.put("keyword", keyword);
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

    public void setData() {

        SearchAdapter adpater = new SearchAdapter(itemArraylist, SearchActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        searchitemsrecycler.setLayoutManager(gridLayoutManager);
        searchitemsrecycler.setItemAnimator(new DefaultItemAnimator());
        searchitemsrecycler.setAdapter(adpater);
    }
}
