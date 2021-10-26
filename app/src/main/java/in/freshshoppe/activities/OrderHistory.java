package in.freshshoppe.activities;

import android.content.Intent;
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
import in.freshshoppe.adapters.OrderHistoryAdapter;
import in.freshshoppe.extras.RecyclerTouchListener;
import in.freshshoppe.extras.ServerLinks;
import in.freshshoppe.extras.SessionManager;
import in.freshshoppe.extras.ViewDialog;
import in.freshshoppe.models.OrderGetSet;
import in.freshshoppe.models.OrderItemGetSet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistory extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ImageView iv_back;
    RecyclerView orderhist;
    ImageView iv_add;
    ArrayList<OrderGetSet> orderarray;
    ArrayList<OrderItemGetSet> orderitemarray;
    ViewDialog progressbar;
    SessionManager session;
    SwipeRefreshLayout swipeLayout;
    ImageView noconnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressbar = new ViewDialog(this);
        session = new SessionManager(this);

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        orderhist = findViewById(R.id.orderhist);

        orderhist.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), orderhist, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                OrderGetSet parenting = orderarray.get(position);

//                Constants.orderitem = new ArrayList<OrderItemGetSet>();
//                Constants.orderitem = parenting.getItemsarray();
//
//                Intent i = new Intent(getApplicationContext(), OrderDetails.class);
//                i.putExtra("orderid",parenting.getOrder_id());
//                i.putExtra("itemtotal",parenting.getTotal());
//                i.putExtra("delivery",parenting.getShipping_char());
//                i.putExtra("discount",parenting.getCoupon_amnt());
//                i.putExtra("wallet",parenting.getWallet());
//                i.putExtra("totalamt",parenting.getGrand_total());
//                i.putExtra("address",parenting.getName()+"\n"+parenting.getAddress()+"\n"+parenting.getCity()+"("+parenting.getPin()+")"+"\n"+parenting.getContact()+"\n"+parenting.getEmail());
//                i.putExtra("date",parenting.getOrder_date());
//                i.putExtra("paymode",parenting.getPay_mode());
//                i.putExtra("status",parenting.getStatus());
//                startActivity(i);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

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
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), DashBoard.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noconnection.setVisibility(View.GONE);
        orderhist.setVisibility(View.GONE);
        getOrderHistory();
    }

    public void initiateView() {
        getOrderHistory();
    }

    @Override
    public void onRefresh() {

    }

    public void getAddress() {


        OrderHistoryAdapter adpater = new OrderHistoryAdapter(OrderHistory.this, orderarray);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderHistory.this);
        orderhist.setLayoutManager(layoutManager);
        orderhist.setItemAnimator(new DefaultItemAnimator());
        orderhist.setAdapter(adpater);

    }

    public void getOrderHistory() {
        Log.d("error_response", session.getUserID());
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.GetOrders_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {



                                orderarray = new ArrayList<OrderGetSet>();
                                JSONArray All_address = jsonObject.getJSONArray("All_Orders");
                                for(int n = 0; n< All_address.length(); n++){

                                    JSONObject productt_json = All_address.getJSONObject(n);
                                    String order_id = productt_json.getString("order_id");
                                    String order_status = productt_json.getString("order_status");
                                    String shiping_type = productt_json.getString("shiping_type");
                                    String shipping_charge = productt_json.getString("shipping_charge");
                                    String payment_mode = productt_json.getString("payment_mode");
                                    String subtotal = productt_json.getString("subtotal");
                                    String total = productt_json.getString("total");
                                    String delivery_date = productt_json.getString("delivery_date");

                                    Log.d("error_response", delivery_date);

                                    if(!delivery_date.equalsIgnoreCase("null") && !delivery_date.equalsIgnoreCase("")) {

                                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-d");
                                        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");


                                        Date date = null;
                                        try {
                                            date = inputFormat.parse(delivery_date);
                                            delivery_date = outputFormat.format(date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }



                                        delivery_date = delivery_date + "(" + productt_json.getString("timeSlot") + ")";
                                    }

                                    orderitemarray = new ArrayList<OrderItemGetSet>();
                                    JSONArray Order_details = productt_json.getJSONArray("Order_details");
                                    for(int m = 0; m< Order_details.length(); m++) {

                                        JSONObject item_details = Order_details.getJSONObject(m);
                                        String id = item_details.getString("id");
                                        String name = item_details.getString("name");
                                        String qty = item_details.getString("qty");
                                        String img = item_details.getString("img");
                                        String price = item_details.getString("price");
                                        String weight = item_details.getString("weight");


                                        orderitemarray.add(new OrderItemGetSet(id, name, qty, img, price, weight));
                                    }

                                    orderarray.add(new OrderGetSet(order_id, order_status, shiping_type, shipping_charge, payment_mode, subtotal, total, delivery_date, orderitemarray));

                                }
//                                AddressAdapter adpater = new AddressAdapter(addressarray, AddressList.this);
//                                address_list.setHasFixedSize(true);
//                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddressList.this);
//                                address_list.setLayoutManager(linearLayoutManager);
//                                address_list.setItemAnimator(new DefaultItemAnimator());
//                                address_list.setAdapter(adpater);
                                getAddress();

                                noconnection.setVisibility(View.GONE);
                                orderhist.setVisibility(View.VISIBLE);

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
                            orderhist.setVisibility(View.GONE);
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
