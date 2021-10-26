package in.freshshoppe.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import in.freshshoppe.R;
import in.freshshoppe.activities.DashBoard;
import in.freshshoppe.adapters.CategoryAdapter;
import in.freshshoppe.adapters.ComboAdapter;
import in.freshshoppe.adapters.HomeItemAdapter;
import in.freshshoppe.extras.Constants;
import in.freshshoppe.extras.ServerLinks;
import in.freshshoppe.extras.SessionManager;
import in.freshshoppe.extras.SharedPreference;
import in.freshshoppe.extras.ViewDialog;
import in.freshshoppe.models.CartItem;
import in.freshshoppe.models.CategoryGetSet;
import in.freshshoppe.models.ComboGetSet;
import in.freshshoppe.models.ItemGetSet;
import in.freshshoppe.models.SliderGetSet;
import in.freshshoppe.models.SubCatGetSet;
import in.freshshoppe.models.VariationGetSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<SliderGetSet> urls = new ArrayList<SliderGetSet>();
    private ArrayList<CategoryGetSet> categories = new ArrayList<CategoryGetSet>();
    private ArrayList<SubCatGetSet> subcategories = new ArrayList<SubCatGetSet>();
    ArrayList<ItemGetSet> itemArraylist;
    ArrayList<VariationGetSet> variations;
    ArrayList<ComboGetSet> comboArraylist;
    private int currentPage = 0;
    ViewPager viewPager;
    TabLayout tabview;
    RecyclerView recyle_iem, recyle_cats, recyle_combo ;
    ViewDialog progressbar;
    SessionManager session;
    NestedScrollView mainlayout;
    SwipeRefreshLayout swipeLayout;
    ImageView noconnection;
    String currentVersion, latestVersion;
    Dialog forceUpdateDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());


        viewPager = v.findViewById(R.id.viewPager);
        tabview = v.findViewById(R.id.tabview);
        recyle_cats = v.findViewById(R.id.recyle_cats);
        recyle_iem = v.findViewById(R.id.recyle_iem);
        recyle_combo = v.findViewById(R.id.recyle_combo);
        noconnection = v.findViewById(R.id.noconnection);
        mainlayout = v.findViewById(R.id.mainlayout);

        swipeLayout = v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeLayout.setRefreshing(true);
                initiateView();
                swipeLayout.setRefreshing(false);

            }
        });

        return v;
    }

    public void initiateView() {
        getHomeData();
        ItemCounter();
//        getVersonData();
    }

    @Override
    public void onResume() {
        super.onResume();
        noconnection.setVisibility(View.GONE);
        mainlayout.setVisibility(View.GONE);

        PackageManager packageManager = getActivity().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        currentVersion = packageInfo.versionName;

        getHomeData();
        ItemCounter();
//        getVersonData();
    }

    public void getHomeData() {
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.home_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {

                                urls = new ArrayList<SliderGetSet>();
                                JSONArray All_banner = jsonObject.getJSONArray("All_banner");
                                for(int i = 0; i< All_banner.length(); i++){

                                    JSONObject banner_json = All_banner.getJSONObject(i);
                                    String banner_id = banner_json.getString("banner_id");
                                    String title = banner_json.getString("title");
                                    String img = banner_json.getString("img");

                                    urls.add(new SliderGetSet(banner_id, title, img));
                                }
                                init();


                                categories = new ArrayList<CategoryGetSet>();
                                JSONArray All_category = jsonObject.getJSONArray("All_category");
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

                                itemArraylist = new ArrayList<ItemGetSet>();
                                JSONArray All_products = jsonObject.getJSONArray("All_products");
                                for(int l = 0; l< All_products.length(); l++){

                                    JSONObject product_json = All_products.getJSONObject(l);
                                    String products_id = product_json.getString("products_id");
                                    String name = product_json.getString("name");
                                    String image = product_json.getString("image");
                                    String description = product_json.getString("description");
                                    String regular_price = product_json.getString("regular_price");
                                    String sales_price = product_json.getString("sales_price");


                                    JSONArray variationss = product_json.getJSONArray("variations");
                                    if(variationss.length()==0){
                                        variations = new ArrayList<VariationGetSet>();
                                    }else {
                                        variations = new ArrayList<VariationGetSet>();
                                        for (int m = 0; m < variationss.length(); m++) {
                                            JSONObject variation_json = variationss.getJSONObject(m);
                                            String price_id = variation_json.getString("price_id");
                                            String price = variation_json.getString("price");
                                            String varations = variation_json.getString("varations");

                                            variations.add(new VariationGetSet(price_id, price, varations));
                                        }
                                    }
                                    Log.d("error_response", name);
                                    itemArraylist.add(new ItemGetSet(products_id, name, image, description, regular_price, sales_price, "0", "0", "0", variations));
                                }
                                setData();

                                comboArraylist = new ArrayList<ComboGetSet>();
                                JSONArray All_combo = jsonObject.getJSONArray("All_combo");
                                for(int n = 0; n< All_combo.length(); n++){

                                    JSONObject productt_json = All_combo.getJSONObject(n);
                                    String products_id = productt_json.getString("products_id");
                                    String name = productt_json.getString("name");
                                    String image = productt_json.getString("image");
                                    String description = productt_json.getString("description");
                                    String regular_price = productt_json.getString("regular_price");
                                    String sales_price = productt_json.getString("sales_price");

                                    variations = new ArrayList<VariationGetSet>();


                                    comboArraylist.add(new ComboGetSet(products_id, name, image, description, regular_price, sales_price, "0", "0", "0", variations));
                                }
                                setComboData();

                                noconnection.setVisibility(View.GONE);
                                mainlayout.setVisibility(View.VISIBLE);
                                progressbar.hideDialog();

                            } else {
                                String msg = jsonObject.getString("message");


                                progressbar.hideDialog();
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
                            noconnection.setVisibility(View.VISIBLE);
                            mainlayout.setVisibility(View.GONE);
                            // ...
                        } else {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void getVersonData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.Verson_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {

                                latestVersion = jsonObject.getString("version_name");

                                update();

                            } else {
                                String msg = jsonObject.getString("message");

                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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

                            Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
                            noconnection.setVisibility(View.VISIBLE);
                            mainlayout.setVisibility(View.GONE);
                            // ...
                        } else {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void update() {

        latestVersion = latestVersion.replace("Versoin ", "");
        latestVersion = latestVersion.replace(".", "");
        currentVersion = currentVersion.replace(".", "");

        int lv = Integer.parseInt(latestVersion);
        int cv = Integer.parseInt(currentVersion);

        Log.d("iuhfnevd", "Latest: " + lv);
        Log.d("iuhfnevd", "Current: " + cv);

        if (lv>cv) {
            setForceUpdateDialog();
        } else {

        }
    }

    private void setForceUpdateDialog() {
        forceUpdateDialog = new Dialog(getActivity());
        forceUpdateDialog.setContentView(R.layout.force_update_dialog);
        Window window = forceUpdateDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        TextView sure = forceUpdateDialog.findViewById(R.id.tv_sure);
        TextView cancel = forceUpdateDialog.findViewById(R.id.tv_cancel);

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forceUpdateDialog.dismiss();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + "&hl=en_US")));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forceUpdateDialog.dismiss();
            }
        });

        forceUpdateDialog.setCancelable(false);
        forceUpdateDialog.show();
    }

    private void init() {

        MyCustomPagerAdapter myCustomPagerAdapter = new MyCustomPagerAdapter(getActivity(), urls);
        viewPager.setAdapter(myCustomPagerAdapter);
        tabview.setupWithViewPager(viewPager, true);
        setupAutoPager();
    }

    private void setupAutoPager() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (viewPager == null) {
                    return;
                }
                viewPager.setCurrentItem(currentPage, true);
                if (currentPage == urls.size()) {
                    currentPage = 0;
                } else {
                    ++currentPage;
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 3000, 3000);
    }

    @Override
    public void onRefresh() {

    }

    public class MyCustomPagerAdapter extends PagerAdapter {
        Context context;
        List<SliderGetSet> bannerDatumList;
        LayoutInflater layoutInflater;

        public MyCustomPagerAdapter(Context context, List<SliderGetSet> bannerDatumList) {
            this.context = context;
            this.bannerDatumList = bannerDatumList;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return bannerDatumList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.item_banner, container, false);
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            Glide.with(context).load(
//                    APIClient.Base_URL + "/" +
                    bannerDatumList.get(position).getImg()).into(imageView);
            container.addView(itemView);
            //listening to image click
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
                }
            });
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    public void setCategories(){

        CategoryAdapter restaurantAdapter = new CategoryAdapter(categories, getActivity());
        recyle_cats.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyle_cats.setLayoutManager(gridLayoutManager);
        recyle_cats.setAdapter(restaurantAdapter);
        recyle_cats.setVisibility(View.VISIBLE);

    }

    public void setData(){

        HomeItemAdapter adpater = new HomeItemAdapter(itemArraylist, getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyle_iem.setLayoutManager(gridLayoutManager);
        recyle_iem.setItemAnimator(new DefaultItemAnimator());
        recyle_iem.setAdapter(adpater);
    }

    public void setComboData(){

        ComboAdapter adpater = new ComboAdapter(comboArraylist, getActivity());
        recyle_combo.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        recyle_combo.setLayoutManager(gridLayoutManager);
        recyle_combo.setItemAnimator(new DefaultItemAnimator());
        recyle_combo.setAdapter(adpater);


    }

    public void ItemCounter() {

        try {

            ArrayList<CartItem> favouritesBeanSampleList = new ArrayList<CartItem>();
            SharedPreference sharedPreference = new SharedPreference();
            favouritesBeanSampleList = sharedPreference.loadFavorites(getActivity());

            String ct = String.valueOf(favouritesBeanSampleList.size());
            Constants.cartitems = ct;

            DashBoard.itemcounter.setText(Constants.cartitems);

        } catch (Exception e) {

            Log.d("Bw3fev", ""+e);

        }

    }
}
