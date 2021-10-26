package in.freshshoppe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;
import in.freshshoppe.R;
import in.freshshoppe.extras.ServerLinks;
import in.freshshoppe.extras.SessionManager;
import in.freshshoppe.extras.ViewDialog;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    ViewDialog progressbar;
    SessionManager session;
    TextView btn_sign;
    TextInputEditText ed_username, ed_email, ed_number, ed_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressbar = new ViewDialog(this);
        session = new SessionManager(this);

        ed_username = findViewById(R.id.ed_username);
        ed_email = findViewById(R.id.ed_email);
        ed_number = findViewById(R.id.ed_number);
        ed_password = findViewById(R.id.ed_password);

        btn_sign = findViewById(R.id.btn_sign);
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_username.getText().toString().trim().length()==0){
                    ed_username.setError("enter fullname");
                    ed_username.requestFocus();

                }else if(ed_email.getText().toString().trim().length()==0){
                    ed_email.setError("enter email");
                    ed_email.requestFocus();

                }else if(ed_number.getText().toString().trim().length()==0){
                    ed_number.setError("enter number");
                    ed_number.requestFocus();

                }else if(ed_number.getText().toString().trim().length()!=10){
                    ed_number.setError("enter valid number");
                    ed_number.requestFocus();

                }else if(ed_password.getText().toString().trim().length()==0){
                    ed_password.setError("enter password");
                    ed_password.requestFocus();

                }else{
                    SignUp();
                }
            }
        });
    }

    public void SignUp() {
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.signup_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {


                                Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                Toast.makeText(Register.this, "Login to continue", Toast.LENGTH_SHORT).show();

                                progressbar.hideDialog();
                                Intent i = new Intent(getApplicationContext(), Login.class);
                                startActivity(i);
                                finish();
                            } else {
                                String msg = jsonObject.getString("msg");


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
                params.put("fullname", ed_username.getText().toString());
                params.put("contact", ed_number.getText().toString().trim());
                params.put("mail", ed_email.getText().toString().trim().toLowerCase());
                params.put("username", ed_email.getText().toString().trim().toLowerCase());
                params.put("password", ed_password.getText().toString());

                Log.d("paramsforhomeapi", "" + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}
