package com.project.amit.onlinefurniturestore.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.project.amit.onlinefurniturestore.R;
import com.project.amit.onlinefurniturestore.models.UserModel;

import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {

    EditText enternumber,mFullName,mEmail,mPassword,cPassword;
    TextView mLoginBtn;
    Button getotpbutton;
    FirebaseAuth fAuth;
    FirebaseDatabase database;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.createText);
        enternumber = findViewById(R.id.phone);
        getotpbutton = findViewById(R.id.registerBtn);
        cPassword = findViewById(R.id.conf_password);

        sharedPreferences = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);

        boolean isFirstTime = sharedPreferences.getBoolean("firstTime",true);

        if (isFirstTime){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTime",false);
            editor.commit();

            Intent intent = new Intent(RegistrationActivity.this,OnBoardingActivity.class);
            startActivity(intent);
            finish();
        }

        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        ProgressBar progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        getotpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= mFullName.getText().toString().trim();
                String phone = enternumber.getText().toString().trim();
                String email= mEmail.getText().toString().trim();
                String password= mPassword.getText().toString().trim();
                String cpass = cPassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    mFullName.setError("Name is Required.");
                    return;
                }
                if (TextUtils.isEmpty(phone)){
                    enternumber.setError("Number is Required.");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }


                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(cpass)){
                    cPassword.setError("Password is Required");
                    return;
                }

                if (password.length() < 6){
                    mPassword.setError("Password Must be <= 6 Characters");
                    return;
                }

                if (cpass.length() < 6){
                    cPassword.setError("Password Must be <= 6 Characters");
                    return;
                }
                // Register the user in firebase

               /* firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                        .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                        MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                        cartModelList.add(myCartModel);
                                        cartAdapter.notifyDataSetChanged();
                                    }
                                }

                            }
                        });*/

                fAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            UserModel userModel = new UserModel(name,phone,email,password,cpass);
                            String id = task.getResult().getUser().getUid();

                            database.getReference().child("Users").child(id).setValue(userModel);


                        }else{
                            Toast.makeText(RegistrationActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
                if (!enternumber.getText().toString().trim().isEmpty()) {
                    if ((enternumber.getText().toString().trim()).length() == 10){

                        progressBar.setVisibility(View.VISIBLE);
                        getotpbutton.setVisibility(View.INVISIBLE);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91" + enternumber.getText().toString(),
                                60,
                                TimeUnit.SECONDS,
                                RegistrationActivity.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        getotpbutton.setVisibility(View.INVISIBLE);

                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        getotpbutton.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(getApplicationContext(),VerifyOTP.class);
                                        intent.putExtra("mobile",enternumber.getText().toString());
                                        intent.putExtra("backendotp", backendotp);
                                        Toast.makeText(RegistrationActivity.this, "Otp send successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);

                                    }
                                }
                        );
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "Enter Mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

    }
}

