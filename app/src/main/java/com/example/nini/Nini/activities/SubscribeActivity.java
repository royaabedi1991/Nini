package com.example.nini.Nini.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.nini.Nini.models.OtpPostResults;
import com.example.nini.Nini.webService.APIClient;
import com.example.nini.Nini.webService.APIInterface;
import com.example.nini.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribeActivity extends AppCompatActivity {

    private EditText etPin;
    private TextView Emassage;
    private Button subscribe;
    private Button btnSubscribe;
    private String ServiceId;
    private String TransactionId;
    private Long  mobileNumber;
    private String result;
    private String message;
    private String fromWhere;

    public SubscribeActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_subscribe);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        TransactionId = intent.getStringExtra("TRANSACTIONID");
        mobileNumber = Long.valueOf(intent.getStringExtra("NUMBER"));
        fromWhere = (intent.getStringExtra("FROMWHER"));

        setContentView(R.layout.activity_subscribe);
        etPin = findViewById(R.id.etPin);
        Emassage = findViewById(R.id.TVmasage);
        subscribe = findViewById(R.id.sub);
        ServiceId = "16";
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromWhere.equals("RESULT")) {

                    subscribeUser(ServiceId, TransactionId, String.valueOf(etPin.getText()));
                } else if (fromWhere.equals("MESSAGE")) {
                    subscribeUserViaCode(ServiceId, TransactionId, String.valueOf(etPin.getText()));
                }

            }
        });

        YoYo.with(Techniques.Pulse)
                .duration(1000)
                .repeat(200)
                .playOn(findViewById(R.id.logo2));
    }

    private void subscribeUser(String ServiceId, String TransactionId, String Pin) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<OtpPostResults> call = apiInterface.subscribeUser(ServiceId, TransactionId, etPin.getText().toString());
        call.enqueue(new Callback<OtpPostResults>() {
            @Override
            public void onResponse(Call<OtpPostResults> call, Response<OtpPostResults> response) {


                OtpPostResults apiResponse = new OtpPostResults();
                apiResponse = response.body();
                if (apiResponse.getIsSuccessful() == true && apiResponse.getResult().equals(mobileNumber)) {

                    Intent intent = new Intent(SubscribeActivity.this, MainActivity.class);
                    intent.putExtra("PHONE", String.valueOf(mobileNumber));
                    startActivity(intent);
                    finish();

                    finish();
                    Toast.makeText(SubscribeActivity.this,
                            "عضویت با موفقیت انجام شد "

                            , Toast.LENGTH_LONG).show();
                } else if (apiResponse.getIsSuccessful() == false && apiResponse.getResult() == 0) {

                    Emassage.setText("خطا، لطفا کد 4 رقمی ای که توسط پیامک دریافت کرده اید را مجددا وارد کرده و بعد از چند لحظه مجددا تلاش کنید");

                } else {
                    Emassage.setText("خطا");
                }


            }


            @Override
            public void onFailure(Call<OtpPostResults> call, Throwable t) {
                Emassage.setText(t.toString());
                Emassage.setText(t.toString());
            }
        });

    }

    private void subscribeUserViaCode(String ServiceId, String TransactionId, String Pin) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<OtpPostResults> call = apiInterface.subscribeUserViaCode(ServiceId, TransactionId, Pin);
        call.enqueue(new Callback<OtpPostResults>() {
            @Override
            public void onResponse(Call<OtpPostResults> call, Response<OtpPostResults> response) {

                OtpPostResults apiResponse = response.body();

                if (apiResponse.getIsSuccessful() == true && apiResponse.getResult().equals(mobileNumber)) {
                    Intent intent = new Intent(SubscribeActivity.this, MainActivity.class);
                    intent.putExtra("PHONE", String.valueOf(mobileNumber));

                    startActivity(intent);
                    finish();
                    Toast.makeText(SubscribeActivity.this,
                            "عضویت با موفقیت انجام شد "

                            , Toast.LENGTH_LONG).show();
                } else if (apiResponse.getIsSuccessful() == false && apiResponse.getResult() == -2) {

                    Emassage.setText("خطای سیستمی، لطفا مجدد تلاش کنید");
                } else if (apiResponse.getIsSuccessful() == false && apiResponse.getResult() == -4) {
                    Emassage.setText("پین کد وارد شده اشتباه است، لطفا دوباره تلاش کنید");
                } else {
                    Emassage.setText("مشترک گرامی لطفا بعد از چند دقیقه دوباره تلاش کنید");
                }
            }

            @Override
            public void onFailure(Call<OtpPostResults> call, Throwable t) {
                Emassage.setText(t.toString());
            }
        });

    }

}
