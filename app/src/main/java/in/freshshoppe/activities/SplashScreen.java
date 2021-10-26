package in.freshshoppe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import in.freshshoppe.R;
import in.freshshoppe.extras.SessionManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);

        PassIntent();
    }

    public void PassIntent() {

        new android.os.Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if(session.isLogin()){
                    Intent i = new Intent(getApplicationContext(), DashBoard.class);
                    startActivity(i);
                    finish();
                }else {
                    session.setUserID("0");
                    session.setUserName("Guest");
                    session.setUserEmail("");
                    session.setPhoto("");

                    Intent i = new Intent(getApplicationContext(), DashBoard.class);
                    startActivity(i);
                    finish();

//                    Intent i = new Intent(getApplicationContext(), Login.class);
//                    startActivity(i);
//                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }

}