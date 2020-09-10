package com.officialshopwala.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPVerificationActivity extends AppCompatActivity {

    EditText otpEditText;

    String phoneNumber;

    String verificationCodeBySystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verification);

        otpEditText = findViewById(R.id.otpEditText);
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        if (!checkIfUserExists()) {
            verifyOtp();
        } else {

        }

    }

    private boolean checkIfUserExists() {
        return false;
    }

    public void verifyOtp () {
        Log.i("verify otp" , "called");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.i("on code sent", "called");
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.i("onVerificationCompleted", "called");
            String code = phoneAuthCredential.getSmsCode();
            if (code!=null) {
                Log.i("code on same phone", code);
                verifyCode(code);
            } else {
                Log.i("code", "is null");
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.i("onVerificationFailed", "called");
            Toast.makeText(OTPVerificationActivity.this, "failed", Toast.LENGTH_SHORT).show();
        }

    };

    public void manuallyVerifyTheCode (View view) {
        Log.i("verify the code", "called");
        String userInputOtp = otpEditText.getText().toString();
        if (userInputOtp.length() == 6 ) {
            verifyCode(userInputOtp);
        } else {
            Toast.makeText(this, getString(R.string.provide_otp), Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyCode ( String verificationCodebyUser ) {
        Log.i("verifycode", "called");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, verificationCodebyUser);
        signInTheUserByCredential ( credential );
    }

    private void signInTheUserByCredential ( PhoneAuthCredential credential) {

        Log.i("signInTheUserByCredent", "called");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPVerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), ShopSaveActivity.class));
                            finish();
                        } else {
                            Toast.makeText(OTPVerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}