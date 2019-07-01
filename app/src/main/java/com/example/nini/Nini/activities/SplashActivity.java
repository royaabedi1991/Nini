package com.example.nini.Nini.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.nini.Nini.models.OtpGetResult;
import com.example.nini.Nini.webService.APIClient;
import com.example.nini.Nini.webService.APIInterface;
import com.example.nini.R;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    Button button;
    AVLoadingIndicatorView avi;
    private int ServiceId = 16;

    public SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        init();

////        anim.setAnimationListener(new Animation.AnimationListener() {
////            @Override
////            public void onAnimationStart(Animation animation) {
////            }
////
////            @Override
////            public void onAnimationEnd(Animation animation) {
////                startActivity(new Intent(this,.class));
////                // HomeActivity.class is the activity to go after showing the splash screen.
////            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
// s
        startAnim();
        checkConnection();

    }

    private void init() {
        avi = findViewById(R.id.avi);


        prefs = getApplicationContext().getSharedPreferences("MyPref", 0);


    }

    private void getSubStatus(String MobileNumber, int ServiceId) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<OtpGetResult> call = apiInterface.getSubStatus(MobileNumber, ServiceId);
        call.enqueue(new Callback<OtpGetResult>() {
            @Override
            public void onResponse(Call<OtpGetResult> call, Response<OtpGetResult> response) {

                OtpGetResult apiResponse = response.body();
                if (apiResponse.getIsSuccessful() == true && apiResponse.getResult() == true) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();

                } else if (apiResponse.getIsSuccessful() == true && apiResponse.getResult() == false) {

                    startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                    finish();

                }


            }

            @Override
            public void onFailure(Call<OtpGetResult> call, Throwable t) {
                Toast.makeText(SplashActivity.this,"لطفا بعد از چند دقیقه دوباره تلاش کنید",Toast.LENGTH_LONG);
            }
        });


    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    public void checkConnection() {

        if (isOnline()) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    final SharedPreferences prefs;
                    prefs = getApplicationContext().getSharedPreferences("MyPref", 0);
                    String MobileNumber = prefs.getString("PHONENUMBER", "");
                    if (MobileNumber != null && !MobileNumber.equals("")) {
                        getSubStatus(MobileNumber, ServiceId);

                    } else {
                        startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                        finish();
                    }


                    finish();

                }
            }, 4000);


        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopAnim();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "لطفا اتصال اینترنت خود را بررسی کرده و دوباره تلاش کنید", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    button = findViewById(R.id.btnRegister);
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startAnim();

                            final SharedPreferences prefs;
                            prefs = getApplicationContext().getSharedPreferences("MyPref", 0);
                            String MobileNumber = prefs.getString("PHONENUMBER", "");
                            if (MobileNumber != null && !MobileNumber.equals("")) {
                                getSubStatus(MobileNumber, ServiceId);

                            } else {
                                startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                                finish();
                            }


                        }
                    });
                }

            }, 2000);


        }

    }

    void startAnim() {

        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.hide();
        // or avi.smoothToHide();
    }

}

