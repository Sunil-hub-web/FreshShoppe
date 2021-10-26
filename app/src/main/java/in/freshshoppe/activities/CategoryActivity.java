package in.freshshoppe.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import in.freshshoppe.R;
import in.freshshoppe.adapters.CategoryAdapter;
import in.freshshoppe.extras.ServerLinks;
import in.freshshoppe.extras.SessionManager;
import in.freshshoppe.extras.ViewDialog;
import in.freshshoppe.models.CategoryGetSet;
import in.freshshoppe.models.SubCatGetSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyle_cats;
    private ArrayList<CategoryGetSet> categories = new ArrayList<CategoryGetSet>();
    private ArrayList<SubCatGetSet> subcategories = new ArrayList<SubCatGetSet>();
    ViewDialog progressbar;
    SessionManager session;
    ImageView backicon;
    SwipeRefreshLayout swipeLayout;
    ImageView noconnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressbar = new ViewDialog(this);
        session = new SessionManager(this);

        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyle_cats = findViewById(R.id.recyle_cats);

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

    @Override
    protected void onResume() {
        super.onResume();
        noconnection.setVisibility(View.GONE);
        recyle_cats.setVisibility(View.GONE);
        getCategory();
    }

    public void initiateView() {
        getCategory();
    }

    @Override
    public void onRefresh() {

    }

    public void getCategory(){
        progressbar.showDialog();
        Log.d("cityget", ServerLinks.category_url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.category_url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {

                                categories = new ArrayList<CategoryGetSet>();
                                JSONArray All_category = jsonObject.getJSONArray("All_Category_list");
                                for(int j = 0; j< All_category.length(); j++){

                                    JSONObject category_json = All_category.getJSONObject(j);
                                    String category_id = category_json.getString("category_id");
                                    String cate_name = category_json.getString("cate_name");
                                    String cate_img = category_json.getString("cate_img");

                                    subcategories = new ArrayList<SubCatGetSet>();
//                                    JSONArray Subcate = category_json.getJSONArray("Subcate");
//                                    for(int k = 0; k< Subcate.length(); k++){
//                                        JSONObject subcategory_json = Subcate.getJSONObject(k);
//                                        String subcate_id = subcategory_json.getString("subcate_id");
//                                        String subcate_name = subcategory_json.getString("subcate_name");
//                                        String subcate_img = subcategory_json.getString("subcate_img");
//
//                                        subcategories.add(new SubCatGetSet(subcate_id, subcate_name, subcate_img));
//                                    }

                                    categories.add(new CategoryGetSet(category_id, cate_name, cate_img, subcategories));
                                }
                                setCategories();

                                noconnection.setVisibility(View.GONE);
                                recyle_cats.setVisibility(View.VISIBLE);

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

                            noconnection.setVisibility(View.VISIBLE);
                            recyle_cats.setVisibility(View.GONE);
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

    public void setCategories(){

        CategoryAdapter restaurantAdapter = new CategoryAdapter(categories, getApplicationContext());
        recyle_cats.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyle_cats.setLayoutManager(gridLayoutManager);
        recyle_cats.setAdapter(restaurantAdapter);
        recyle_cats.setVisibility(View.VISIBLE);

    }
}
