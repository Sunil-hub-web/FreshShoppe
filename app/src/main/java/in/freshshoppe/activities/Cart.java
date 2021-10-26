package in.freshshoppe.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import androidx.cardview.widget.CardView;
import in.freshshoppe.R;
import in.freshshoppe.adapters.CartAdapter;
import in.freshshoppe.extras.ServerLinks;
import in.freshshoppe.extras.SessionManager;
import in.freshshoppe.extras.SharedPreference;
import in.freshshoppe.extras.ViewDialog;
import in.freshshoppe.models.CartItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Cart extends AppCompatActivity {

    ImageView iv_back;
    RecyclerView recycle_rest_cats;
    ArrayList<CartItem> favouritesBeanSampleList = new ArrayList<CartItem>();
    ArrayList<String> deliverychargesarray = new ArrayList<String>();
    HashMap<String, String> deliverychargesid = new HashMap<String, String>();
    HashMap<String, String> deliverychargesprice = new HashMap<String, String>();
    SharedPreference sharedPreference;
    public static TextView paybleamount, tv_change, itemtotal, tv_ship_price, tv_checkout;
    ViewDialog progressbar;
    SessionManager session;
    Spinner shippingchargename;
    public static double sum = 0.00, delivery_ch = 0.00, coupon_amt = 0.00;
    String deliveryid, slotname = "", dateselected = "";
    RelativeLayout noitem_layout;
    LinearLayout cartlayout, datetimeslot_layout;
    TextView name_txt, address_txt, pincode_txt, phoneno_txt, date_txt;
    //    public ArrayList<CartItem> cartfinalarray;
    Spinner timeslot;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int i;
    int dayOfMonth;
    Calendar calendar;
    CardView addresslayout;
    long timeInMilliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressbar = new ViewDialog(this);
        session = new SessionManager(this);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addresslayout = findViewById(R.id.addresslayout);
        datetimeslot_layout = findViewById(R.id.datetimeslot_layout);
        cartlayout = findViewById(R.id.cartlayout);
        noitem_layout = findViewById(R.id.noitem_layout);
        recycle_rest_cats = findViewById(R.id.recycle_rest_cats);
        paybleamount = findViewById(R.id.paybleamount);
        tv_change = findViewById(R.id.tv_change);
        date_txt = findViewById(R.id.date_txt);
        itemtotal = findViewById(R.id.itemtotal);
        tv_ship_price = findViewById(R.id.tv_ship_price);
        shippingchargename = findViewById(R.id.shippingchargename);
        tv_checkout = findViewById(R.id.tv_checkout);
        timeslot = findViewById(R.id.timeslot);

        name_txt = findViewById(R.id.name_txt);
        address_txt = findViewById(R.id.address_txt);
        pincode_txt = findViewById(R.id.pincode_txt);
        phoneno_txt = findViewById(R.id.phoneno_txt);

        sharedPreference = new SharedPreference();


        favouritesBeanSampleList = sharedPreference.loadFavorites(Cart.this);


        try {
            if (favouritesBeanSampleList.size() != 0) {

                CartAdapter productListAdapter = new CartAdapter(Cart.this, favouritesBeanSampleList);
                recycle_rest_cats.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recycle_rest_cats.setItemAnimator(new DefaultItemAnimator());
                recycle_rest_cats.setAdapter(productListAdapter);
                noitem_layout.setVisibility(View.GONE);
                cartlayout.setVisibility(View.VISIBLE);
            } else {
                noitem_layout.setVisibility(View.VISIBLE);
                cartlayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            noitem_layout.setVisibility(View.VISIBLE);
            cartlayout.setVisibility(View.GONE);
        }

        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddressList.class);
                startActivity(i);
            }
        });

        getDeliveryCharges();

        shippingchargename.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String del = shippingchargename.getItemAtPosition(shippingchargename.getSelectedItemPosition()).toString();

                deliveryid = deliverychargesid.get(del);
                delivery_ch = Double.parseDouble(deliverychargesprice.get(del));

                tv_ship_price.setText("₹" + delivery_ch);

                getTotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getApplicationContext(), "Select Your City", Toast.LENGTH_SHORT).show();
            }
        });

        date_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(Cart.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                                edit_date.setText(year + "-" + (month + 1) + "-" + day);

                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-d");
                                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                String inputDateStr = year + "-" + (month + 1) + "-" + day;
                                Log.d("sufifn", inputDateStr);
                                dateselected = inputDateStr;
                                Date date = null;
                                try {
                                    date = inputFormat.parse(inputDateStr);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String outputDateStr = outputFormat.format(date);

                                date_txt.setText(outputDateStr);
                                setSpinnerData();
//                                fragHome_Date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        tv_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!session.getUserID().equalsIgnoreCase("0")) {


                    if (session.getBillFirstANme().equalsIgnoreCase("First Name")) {
                        Toast.makeText(Cart.this, "Selct Address", Toast.LENGTH_SHORT).show();
                    } else if (datetimeslot_layout.getVisibility() == View.GONE) {
                        datetimeslot_layout.setVisibility(View.VISIBLE);
                        cartlayout.setVisibility(View.GONE);
                    }else if (dateselected.equalsIgnoreCase("")) {
                        Toast.makeText(Cart.this, "Select Delivary Date", Toast.LENGTH_SHORT).show();
                    }else if (slotname.trim().length()==0 || slotname.equalsIgnoreCase("Select time slot")) {
                        Toast.makeText(Cart.this, "Select Time Slot", Toast.LENGTH_SHORT).show();
                    } else {
                        CreateProductArray();
                    }
                } else {
                    Toast.makeText(Cart.this, "Login to place order", Toast.LENGTH_SHORT).show();
                    session.logoutUser();
                }
            }
        });

        timeslot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                slotname = timeslot.getItemAtPosition(timeslot.getSelectedItemPosition()).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here

            }
        });

        if (!session.getUserID().equalsIgnoreCase("0")) {
            addresslayout.setVisibility(View.VISIBLE);
        } else {
            addresslayout.setVisibility(View.GONE);
        }

    }

    ArrayAdapter<CharSequence> adapter;

    public void setSpinnerData() {

        Date currentTime = Calendar.getInstance().getTime();

        timeInMilliseconds = getMilliFromDate(dateselected);
        if (DateUtils.isToday(timeInMilliseconds)) {

            try {

                Date time1 = new SimpleDateFormat("HH:mm:ss").parse("07:00:00");
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(time1);
                calendar1.add(Calendar.DATE, 1);

                Date time2 = new SimpleDateFormat("HH:mm:ss").parse("09:00:00");
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(time2);
                calendar2.add(Calendar.DATE, 1);

                Date time3 = new SimpleDateFormat("HH:mm:ss").parse("11:00:00");
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(time3);
                calendar3.add(Calendar.DATE, 1);

                Date time4 = new SimpleDateFormat("HH:mm:ss").parse("13:00:00");
                Calendar calendar4 = Calendar.getInstance();
                calendar4.setTime(time4);
                calendar4.add(Calendar.DATE, 1);

                Date time5 = new SimpleDateFormat("HH:mm:ss").parse("15:00:00");
                Calendar calendar5 = Calendar.getInstance();
                calendar5.setTime(time5);
                calendar5.add(Calendar.DATE, 1);

                Date time6 = new SimpleDateFormat("HH:mm:ss").parse("17:00:00");
                Calendar calendar6 = Calendar.getInstance();
                calendar6.setTime(time6);
                calendar6.add(Calendar.DATE, 1);

                Date time7 = new SimpleDateFormat("HH:mm:ss").parse("18:00:00");
                Calendar calendar7 = Calendar.getInstance();
                calendar7.setTime(time7);
                calendar7.add(Calendar.DATE, 1);

                Date x = Calendar.getInstance().getTime();
//                final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
//                Date x = dateFormat.format(new Date());

                Log.d("mkfgud_1", "" + calendar1.getTime());
                Log.d("mkfgud_2", "" + calendar2.getTime());
                Log.d("mkfgud_3", "" + calendar3.getTime());
                Log.d("mkfgud_4", "" + calendar4.getTime());
                Log.d("mkfgud_5", "" + calendar5.getTime());
                Log.d("mkfgud_6", "" + calendar6.getTime());
                Log.d("mkfgud_7", "" + calendar7.getTime());
                Log.d("mkfgud_8", "" + x);

                if (x.before(calendar1.getTime())) {
                    adapter = ArrayAdapter.createFromResource(Cart.this,
                            R.array.timearray1, R.layout.spinnerfront2);

                } else if (x.before(calendar2.getTime())) {
                    adapter = ArrayAdapter.createFromResource(Cart.this,
                            R.array.timearray2, R.layout.spinnerfront2);

                } else if (x.before(calendar3.getTime())) {
                    adapter = ArrayAdapter.createFromResource(Cart.this,
                            R.array.timearray3, R.layout.spinnerfront2);

                } else if (x.before(calendar4.getTime())) {
                    adapter = ArrayAdapter.createFromResource(Cart.this,
                            R.array.timearray4, R.layout.spinnerfront2);

                } else if (x.before(calendar5.getTime())) {
                    adapter = ArrayAdapter.createFromResource(Cart.this,
                            R.array.timearray5, R.layout.spinnerfront2);

                } else if (x.before(calendar6.getTime())) {
                    adapter = ArrayAdapter.createFromResource(Cart.this,
                            R.array.timearray6, R.layout.spinnerfront2);

                } else if (x.after(calendar7.getTime())) {
                    adapter = ArrayAdapter.createFromResource(Cart.this,
                            R.array.timearray7, R.layout.spinnerfront2);

                }

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                timeslot.setAdapter(adapter);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        } else {
            adapter = ArrayAdapter.createFromResource(Cart.this,
                    R.array.timearray1, R.layout.spinnerfront2);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            timeslot.setAdapter(adapter);
        }


    }

    public long getMilliFromDate(String dateFormat) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-d");
        try {
            date = formatter.parse(dateFormat);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Today is " + date);
        return date.getTime();
    }

    public void getTotal() {

        favouritesBeanSampleList = sharedPreference.loadFavorites(Cart.this);

//        Log.d("RanjeetCartPage", String.valueOf(favouritesBeanSampleList.size()));
        Cart.sum = 0.00;
        if (favouritesBeanSampleList==null){

        }else {
            if (favouritesBeanSampleList.size() == 0) {

            } else {

                for (int j = 0; j < favouritesBeanSampleList.size(); j++) {


                    CartItem cartIt = favouritesBeanSampleList.get(j);


                    sum = sum + Double.parseDouble(cartIt.getSales_price()) * Double.parseDouble(cartIt.getQuantity());


                }

                itemtotal.setText("₹" + sum);

                Log.d("Frfbw_2", String.valueOf(sum));
                Log.d("Frfbw_2.1", String.valueOf(delivery_ch));
                double totsm = sum + delivery_ch - coupon_amt;

                paybleamount.setText("₹" + totsm);
                Log.d("Frfbw_3", String.valueOf(totsm));
            }
        }
    }

    public void setFinalPrices() {

        try {
            sum = sharedPreference.getCartAmount(Cart.this);
            Log.d("efeawwq", String.valueOf(sum));

            itemtotal.setText("₹" + sum);

            Log.d("Frfbw_2", String.valueOf(sum));
            Log.d("Frfbw_2.1", String.valueOf(delivery_ch));
            double totsm = sum + delivery_ch - coupon_amt;

            paybleamount.setText("₹" + totsm);
            Log.d("Frfbw_3", String.valueOf(totsm));

            if (sum == 0.00) {
                noitem_layout.setVisibility(View.VISIBLE);
            } else {
                noitem_layout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.d("Frfbw_4", String.valueOf(e));
            noitem_layout.setVisibility(View.VISIBLE);
        }
    }

    public void getDeliveryCharges() {
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.ShippingChr_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);
                        Log.d("cityget", session.getUserID());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {
                                deliverychargesarray = new ArrayList<String>();


                                JSONArray latestarr = jsonObject.getJSONArray("All_shipping_chr");

                                for (int j = 0; j < latestarr.length(); j++) {

                                    JSONObject latjo = latestarr.getJSONObject(j);

                                    String shipping_id = latjo.getString("shipping_id");
                                    String price = latjo.getString("price");
                                    String delivery_price = latjo.getString("delivery_price");
                                    String name = latjo.getString("name");


                                    deliverychargesarray.add(name);
                                    deliverychargesid.put(name, shipping_id);
                                    deliverychargesprice.put(name, price);

                                }

                                ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(Cart.this,
                                        R.layout.spinnerdropdownitem, deliverychargesarray);
                                dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                                shippingchargename.setAdapter(dataAdapterVehicle);
//
//                                SetRecent();

                                progressbar.hideDialog();
                                cartlayout.setVisibility(View.VISIBLE);

                            } else {
                                cartlayout.setVisibility(View.GONE);
                                final CharSequence[] items = {"Reload", "Close"};

                                AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
                                builder.setTitle("Something went wrong, reload app..");
                                builder.setCancelable(false);
                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {


                                        if (items[item].equals("Reload")) {

                                            dialog.dismiss();
                                            getDeliveryCharges();

                                        } else if (items[item].equals("Close")) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    }
                                });
                                builder.show();

                                String error = jsonObject.getString("msg");
                                progressbar.hideDialog();
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            cartlayout.setVisibility(View.GONE);
                            final CharSequence[] items = {"Reload", "Close"};

                            AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
                            builder.setTitle("Something went wrong, reload app..");
                            builder.setCancelable(false);
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {

                                    if (items[item].equals("Reload")) {

                                        dialog.dismiss();
                                        getDeliveryCharges();

                                    } else if (items[item].equals("Close")) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }
                            });
                            builder.show();

                            Log.d("error_response", String.valueOf(e));
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressbar.hideDialog();
                        cartlayout.setVisibility(View.GONE);
                        final CharSequence[] items = {"Reload", "Close"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
                        builder.setTitle("Something went wrong, reload app..");
                        builder.setCancelable(false);
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {


                                if (items[item].equals("Reload")) {

                                    dialog.dismiss();
                                    getDeliveryCharges();

                                } else if (items[item].equals("Close")) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }
                        });
                        builder.show();
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

    public void PlaceOrder() {
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.Placeorder_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);
                        Log.d("cityget", session.getUserID());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {
                                deliverychargesarray = new ArrayList<String>();


                                String order_id = jsonObject.getString("order_id");
                                String msg = jsonObject.getString("msg");

                                progressbar.hideDialog();
                                sharedPreference.clearDate(Cart.this);
                                Toast.makeText(Cart.this, msg, Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(getApplicationContext(), OrderHistory.class);
                                startActivity(i);
                                finish();


                            } else {

                                String error = jsonObject.getString("msg");
                                progressbar.hideDialog();
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
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
                params.put("product_id", product_id);
                params.put("qty", qty);
                params.put("price_id", price_variation_id);
                params.put("shiping_id", deliveryid);
                params.put("address_id", session.getBillAddress1());
                params.put("pay_mode", "cod");
                params.put("delivery_date", dateselected);
                params.put("AvalTimeSlot", slotname);
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

    String product_id = "", qty = "", price_variation_id = "";

    public void CreateProductArray() {

//        cartfinalarray = new ArrayList<CartItem>();
//        cartfinalarray = sharedPreference.loadFavorites(Cart.this);

        sharedPreference = new SharedPreference();

        favouritesBeanSampleList = sharedPreference.loadFavorites(Cart.this);

        product_id = "";
        qty = "";
        price_variation_id = "";

        if (favouritesBeanSampleList.size() == 0) {
            Toast.makeText(this, "Add item to proceed", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < favouritesBeanSampleList.size(); i++) {

                CartItem cartItem = favouritesBeanSampleList.get(i);

                if (product_id.length() == 0) {
                    product_id = cartItem.getProduct_id();
                } else {
                    product_id = product_id + "," + cartItem.getProduct_id();
                }

                if (qty.length() == 0) {
                    qty = cartItem.getQuantity();
                } else {
                    qty = qty + "," + cartItem.getQuantity();
                }

                if (price_variation_id.length() == 0) {
                    price_variation_id = cartItem.getVarient_id();
                } else {
                    price_variation_id = price_variation_id + "," + cartItem.getVarient_id();
                }


                Log.d("iteskndfb", "CreateProductArray 2: " + product_id);
                Log.d("iteskndfb", "CreateProductArray 3: " + qty);
                Log.d("iteskndfb", "CreateProductArray 4: " + price_variation_id);


            }

            PlaceOrder();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (session.getBillFirstANme().equalsIgnoreCase("First Name")) {
            name_txt.setVisibility(View.GONE);
            address_txt.setVisibility(View.GONE);
            pincode_txt.setVisibility(View.GONE);
            phoneno_txt.setVisibility(View.GONE);
            tv_change.setText("Select");
        } else {
            name_txt.setText(session.getBillFirstANme());
            address_txt.setText(session.getBillAddres2());
            pincode_txt.setText(session.getBillPostCode());
            phoneno_txt.setText(session.getBillPhone());

            name_txt.setVisibility(View.VISIBLE);
            address_txt.setVisibility(View.VISIBLE);
            pincode_txt.setVisibility(View.VISIBLE);
            phoneno_txt.setVisibility(View.VISIBLE);
            tv_change.setText("Change");
        }
    }

    @Override
    public void onBackPressed() {

        if (datetimeslot_layout.getVisibility() == View.VISIBLE) {
            datetimeslot_layout.setVisibility(View.GONE);
            cartlayout.setVisibility(View.VISIBLE);
        } else {
            finish();
        }

    }
}
