package in.freshshoppe.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.freshshoppe.R;
import in.freshshoppe.extras.ServerLinks;
import in.freshshoppe.extras.SessionManager;
import in.freshshoppe.extras.ViewDialog;
import in.freshshoppe.fragments.HomeFragment;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class DashBoard extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    LinearLayout lvl_home, categories, myprofile, myoder, combos, logout, feedback, contect;
    RelativeLayout cart_layout;
    public static TextView itemcounter;
    TextView ed_username, ed_mail, logouttext;
    SessionManager sesion;
    String currentVersion, latestVersion;
    ImageView search_icon;
    Dialog forceUpdateDialog;
    private Boolean exit = false;
    ViewDialog progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        sesion = new SessionManager(this);
        progressbar = new ViewDialog(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        lvl_home = findViewById(R.id.lvl_home);
        categories = findViewById(R.id.categories);
        cart_layout = findViewById(R.id.cart_layout);
        myprofile = findViewById(R.id.myprofile);
        myoder = findViewById(R.id.myoder);
        combos = findViewById(R.id.combos);
        itemcounter = findViewById(R.id.itemcounter);
        logout = findViewById(R.id.logout);
        feedback = findViewById(R.id.feedback);
        contect = findViewById(R.id.contect);
        search_icon = findViewById(R.id.search_icon);
        logouttext = findViewById(R.id.logouttext);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        ed_username = hView.findViewById(R.id.ed_username);
        ed_mail = hView.findViewById(R.id.ed_mail);

        ed_username.setText(sesion.getUserName());
        ed_mail.setText(sesion.getUserEmail());

        if(sesion.getUserID().equalsIgnoreCase("0")){
            logouttext.setText("Login");
        }else{
            logouttext.setText("Log Out");
        }

        ImageView menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(i);
            }
        });

        lvl_home.setOnClickListener(this);
        myprofile.setOnClickListener(this);
        categories.setOnClickListener(this);
        myoder.setOnClickListener(this);
        combos.setOnClickListener(this);
        logout.setOnClickListener(this);
        contect.setOnClickListener(this);
        feedback.setOnClickListener(this);

        cart_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(getApplicationContext(), Cart.class);
                    startActivity(i);

            }
        });

        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        currentVersion = packageInfo.versionName;

        getVersonData();

    }

    @Override
    public void onBackPressed() {


        if(sesion.getUserID().equalsIgnoreCase("0")){
           Intent i = new Intent(getApplicationContext(), Login.class);
           startActivity(i);
           finish();
        }else{
            if (exit) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void setDrawer() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        char first = session.getUserName().charAt(0);
//        Log.e("first", "-->" + first);
//        txtfirstl.setText("" + first);
//        txtName.setText("" + session.getUserName());
//        txtEmail.setText("" + session.getEmail());
//        txt_countcard = findViewById(R.id.txt_countcard);

        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack("HomePage").commit();

//        addTextWatcher();
//        txtActiontitle.setText("Hello " + session.getUserName() + "...");
    }

    @Override
    public void onClick(View v) {
        Fragment fragment;
        Bundle args;
        switch (v.getId()) {
            case R.id.lvl_home:
                fragment = new HomeFragment();
                callFragment(fragment);
                break;
            case R.id.myprofile:
                if (!sesion.getUserID().equalsIgnoreCase("0")) {
                    startActivity(new Intent(DashBoard.this, ProfileEdit.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Login to view", Toast.LENGTH_SHORT).show();
                    sesion.logoutUser();
                }
                break;
            case R.id.myoder:
                if (!sesion.getUserID().equalsIgnoreCase("0")) {
                    startActivity(new Intent(DashBoard.this, OrderHistory.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Login to view", Toast.LENGTH_SHORT).show();
                    sesion.logoutUser();
                }
                break;
            case R.id.categories:
                startActivity(new Intent(DashBoard.this, CategoryActivity.class));
                break;
            case R.id.combos:
                Intent i = new Intent(DashBoard.this, Combos.class);
                i.putExtra("id", "41");
                i.putExtra("title", "Combos");
                startActivity(i);
                break;
            case R.id.logout:
                if(sesion.getUserID().equalsIgnoreCase("0")){
                    sesion.logoutUser();
                }else {
                    final CharSequence[] items = {"Yes", "Cancel"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
                    builder.setTitle("Are you sure,\nyou want to LOGOUT ?");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            if (items[item].equals("Yes")) {

                                dialog.dismiss();
                                sesion.logoutUser();

                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }
                break;
            case R.id.feedback:
//                sessionManager.logoutUser();
//                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                finish();
                break;
            case R.id.contect:
//                startActivity(new Intent(HomeActivity.this, AboutsActivity.class));
                break;
//            case R.id.privecy:
//                startActivity(new Intent(HomeActivity.this, TramsAndConditionActivity.class));
//                break;
//            case R.id.img_search:
//                if (edSearch.getText().toString().trim().length() != 0) {
//                    args = new Bundle();
//                    args.putInt("id", 0);
//                    args.putString("search", edSearch.getText().toString().trim());
//                    fragment = new ItemListFragment();
//                    fragment.setArguments(args);
//                    callFragment(fragment);
//                }
//                break;
//            case R.id.img_cart:
////                lvlActionsearch.setVisibility(View.GONE);
//                txtActiontitle.setVisibility(View.VISIBLE);
//                rltNoti.setVisibility(View.GONE);
//                rltCart.setVisibility(View.VISIBLE);
//                txtActiontitle.setText("MyCart");
//                fragment = new CardFragment();
//                callFragment(fragment);
//
//                break;
//
//            case R.id.img_noti:
//                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
//                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void callFragment(Fragment fragmentClass) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragmentClass).addToBackStack("adds").commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    public void getVersonData() {
        progressbar.showDialog();
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
                                progressbar.hideDialog();
                                update();

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

                            Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
//                            noconnection.setVisibility(View.VISIBLE);
//                            mainlayout.setVisibility(View.GONE);
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
            setDrawer();
        }
    }

    private void setForceUpdateDialog() {
        forceUpdateDialog = new Dialog(DashBoard.this);
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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en_US")));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forceUpdateDialog.dismiss();
//                setDrawer();
            }
        });

        forceUpdateDialog.setCancelable(false);
        forceUpdateDialog.show();
    }

}
