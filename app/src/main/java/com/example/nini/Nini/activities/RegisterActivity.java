package com.example.nini.Nini.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.nini.Nini.activities.App.ViewAnimatorSlideUpDown;
import com.example.nini.Nini.models.OtpPostResults;
import com.example.nini.Nini.webService.APIClient;
import com.example.nini.Nini.webService.APIInterface;
import com.example.nini.R;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {
    private EditText etMobileNumber;
    private TextView massage;
    private Button register;
    private int ServiceId;
    private String Channel;
    Boolean isSucessful;
    Long result;
    SharedPreferences.Editor editor;
    AVLoadingIndicatorView avi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        etMobileNumber = findViewById(R.id.etNumber);
        massage = findViewById(R.id.TVmsg);
        register = findViewById(R.id.btnRegister2);
        ServiceId = 16;
        Channel = "AnyText";
        avi= findViewById(R.id.avi2);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnim();
                register.setVisibility(View.VISIBLE);
                editor.putString("PHONENUMBER", String.valueOf(etMobileNumber.getText()));
                editor.apply();
                registerUser(String.valueOf(etMobileNumber.getText()), ServiceId, Channel);

            }
        });
        YoYo.with(Techniques.Pulse)
                .duration(1000)
                .repeat(200)
                .playOn(findViewById(R.id.logo));

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void registerUser(final String MobileNumber, int ServiceId, String Channel) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<OtpPostResults> call = apiInterface.registerUser(MobileNumber, ServiceId, Channel);
        call.enqueue(new Callback<OtpPostResults>() {
            @Override
            public void onResponse(Call<OtpPostResults> call, Response<OtpPostResults> response) {
                if (response.isSuccessful()) {
                    isSucessful = response.body().getIsSuccessful();
                    result = response.body().getResult();


                    if (isSucessful == true & result > 0) {
                        Intent intent = new Intent(RegisterActivity.this, SubscribeActivity.class);
                        finish();
                        intent.putExtra("NUMBER", etMobileNumber.getText().toString());
                        intent.putExtra("TRANSACTIONID", response.body().getResult().toString());
                        intent.putExtra("FROMWHER", "RESULT");

                        startActivity(intent);
                        Toast.makeText(RegisterActivity.this, "درخواست عضویت با موفقیت انجام شد "
                                , Toast.LENGTH_LONG).show();

                    } else if (isSucessful == true & result == -1) {
                        Intent intent = new Intent(RegisterActivity.this, SubscribeActivity.class);
                        finish();
                        intent.putExtra("NUMBER", etMobileNumber.getText().toString());
                        intent.putExtra("TRANSACTIONID", response.body().getMessage().toString());
                        intent.putExtra("FROMWHER", "MESSAGE");

                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "لطفا بعد از چند دقیقه مجدد تلاش کنید", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OtpPostResults> call, Throwable t) {
                Log.e("error", t.toString());

            }
        });

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


