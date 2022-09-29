package com.project.amit.onlinefurniturestore.activites;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project.amit.onlinefurniturestore.R;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView subTotal,discount,shipping,total;
     Button paymentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //Toolbar
        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        double amount = 0.0;
        amount = getIntent().getDoubleExtra("amount", 0.0);

        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        shipping = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);
        paymentBtn = findViewById(R.id.pay_btn);

        subTotal.setText("₹ " + amount);

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GooglePay.class));
                finish();
            }
        });


/*        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentUsingGooglePay();
            }
        });





    }



    private void paymentUsingGooglePay() {
        String GOOGLE_PAY_PACKAGE_NAME = "io.github.aigens.order.place:googlepay:0.0.23";
        Uri uri=Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa","susheelaramdev123@oksbi") // Here you Enter upi id
                .appendQueryParameter("pn","Developer Amit") // Enter your name
                .appendQueryParameter("tn","Payment just for testing")//Enter note here
                .appendQueryParameter("am","1") // Enter amount here
                .appendQueryParameter("cu","INR") // Currency type
                .build();

        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        try{
            startActivityForResult(intent,101);
        }catch (Exception e){
            Toast.makeText(this, "Google pay app not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101)
        {
            if (requestCode==RESULT_OK)
            {
                if(data != null)
                {
                    String value = data.getStringExtra("response");

                    //This list contain all data after payment successfull
                    ArrayList<String> list = new ArrayList<>();
                    list.add(value);
                    Toast.makeText(this, "Payment Successfull", Toast.LENGTH_SHORT).show();

                }
            }else{
                Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void getStatus(String data){
        boolean ispaymentcancel=false;
        boolean ispaymentsuccess=false;
        boolean paymentfailed=false;
        String value[]=data.split("&");
        for (int i=0;i<value.length;i++){
            String checkString[]=value[i].split("=");
            if (checkString.length>=2) {
                if(checkString[0].toLowerCase().equals("status")){
                    if (checkString[1].equals("success")){

                        ispaymentsuccess=true;
                    }
                }
            }else{
                ispaymentcancel=true;

            }
        }
        if (ispaymentsuccess){
            Toast.makeText(this, "Payment Successfull", Toast.LENGTH_SHORT).show();
        }else if(ispaymentcancel){
            Toast.makeText(this, "Payment Cancled", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        }*/



    }
}