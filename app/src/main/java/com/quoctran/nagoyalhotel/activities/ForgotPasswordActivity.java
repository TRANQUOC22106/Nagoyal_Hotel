package com.quoctran.nagoyalhotel.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.quoctran.nagoyalhotel.R;
import com.quoctran.nagoyalhotel.ui.login.LoginFragment;

import java.util.Objects;

import static com.quoctran.nagoyalhotel.utils.FirebaseUtils.FIREBASE_AUTH;

public class ForgotPasswordActivity extends AppCompatActivity {
    private boolean actionBackToLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        TextInputEditText edt_emailForgot_forgotPassword = findViewById(R.id.edt_emailForgot_forgotPassword);
        MaterialButton btn_emailForgot_forgotPassword = findViewById(R.id.btn_emailForgot_forgotPassword);
        TextInputLayout tv_emailForgot_forgotPassword = findViewById(R.id.tv_emailForgot_forgotPassword);
        TextView tv_forgot_password = findViewById(R.id.tv_forgot_password);
        ProgressDialog loadingBar = new ProgressDialog(this, R.style.DialogStyleApp);
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToLoginScreen();
            }
        });
        btn_emailForgot_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionBackToLogin) {
                   backToLoginScreen();


                } else {
                    loadingBar.setTitle(getString(R.string.resetTitle));
                    loadingBar.setMessage(getString(R.string.please_wait_msg));
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    String emailForgot = Objects.requireNonNull(edt_emailForgot_forgotPassword.getText()).toString();
                    FIREBASE_AUTH.sendPasswordResetEmail(emailForgot).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loadingBar.dismiss();
                                Toast.makeText(ForgotPasswordActivity.this, getString(R.string.emailMsg), Toast.LENGTH_SHORT).show();
                                tv_emailForgot_forgotPassword.setVisibility(View.GONE);
                                tv_forgot_password.setVisibility(View.GONE);
                                btn_emailForgot_forgotPassword.setText(getString(R.string.backToLogin));
                                actionBackToLogin = true;
                            } else {

                                loadingBar.dismiss();
                                Toast.makeText(ForgotPasswordActivity.this, getString(R.string.errorMsg), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
    @Override
    public void onBackPressed() {

        return;
    }
    private void backToLoginScreen() {
        Intent mServiceIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(mServiceIntent);
    }
}
