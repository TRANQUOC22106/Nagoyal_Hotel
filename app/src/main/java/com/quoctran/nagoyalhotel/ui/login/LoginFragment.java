package com.quoctran.nagoyalhotel.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.quoctran.nagoyalhotel.activities.ForgotPasswordActivity;
import com.quoctran.nagoyalhotel.MainActivity;
import com.quoctran.nagoyalhotel.R;
import com.quoctran.nagoyalhotel.activities.RegisterActivity;

import java.util.Objects;

import static com.quoctran.nagoyalhotel.utils.FirebaseUtils.FIREBASE_AUTH;
import static com.quoctran.nagoyalhotel.utils.ValidationUtils.isEmailValid;
import static com.quoctran.nagoyalhotel.utils.ValidationUtils.isPasswordValid;

public class LoginFragment extends Fragment {
    private TextInputLayout passwordTextInput, emailTextInput;
    private TextInputEditText passwordEditText, emailEditText;
    private MaterialButton loginButton, registerButton;
    private boolean clicked = false;
    private TextView tv_forgot_password;
    private ProgressDialog loadingBar;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        preLogin(view);
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForgotPassword = new Intent(getContext(), ForgotPasswordActivity.class);
                startActivity(intentForgotPassword);
            }
        });
        return view;
    }

    private void preLogin(View view) {
        loadingBar = new ProgressDialog(getContext(), R.style.DialogStyleApp);
        passwordTextInput = view.findViewById(R.id.login_password_text_input);
        passwordEditText = view.findViewById(R.id.login_password_edit_text);
        emailTextInput = view.findViewById(R.id.login_email_text_input);
        emailEditText = view.findViewById(R.id.login_email_edit_text);
        loginButton = view.findViewById(R.id.login_button);
        registerButton = view.findViewById(R.id.register_button);
        tv_forgot_password = view.findViewById(R.id.tv_forgot_password);
        actionListener();
    }

    private void actionListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked) {
                    clicked = true;
                    loginAction();
                    new Handler().postDelayed(() -> clicked = false, 1000);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });

        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && !clicked) {
                    clicked = true;
                    loginAction();
                    new Handler().postDelayed(() -> clicked = false, 1000);
                }
                passwordTextInput.setError(null);
                emailTextInput.setError(null); //Clear the error
                return false;
            }
        });

        emailEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                passwordTextInput.setError(null);
                emailTextInput.setError(null); //Clear the error
                return false;
            }
        });

    }

    private void loginAction() {
        boolean isError = false;
        if (!isPasswordValid(passwordEditText.getText())) {
            passwordTextInput.setError(getString(R.string.error_password));
            isError = true;
        } else {
            passwordTextInput.setError(null); // Clear the error
            isError = false;
        }

        if (!isEmailValid(emailEditText.getText())) {
            isError = true;
            emailTextInput.setError(getString(R.string.error_email));
        } else {
            emailTextInput.setError(null); // Clear the error
            isError = false;
        }

        if (!isError) {
            login(emailEditText.getText().toString(), passwordEditText.getText().toString());
        }
    }

    /*
    * default user cuong@mailinator.com:admin123
    * */

    private void login (String email, String password) {
        loadingBar.setTitle(getString(R.string.loginTitle));
        loadingBar.setMessage(getString(R.string.please_wait_msg));
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        FIREBASE_AUTH.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Intent intentLoginSuccess = new Intent(getActivity(), MainActivity.class);
                            intentLoginSuccess.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intentLoginSuccess);


                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(getActivity(),
                                    "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                            loginButton.setEnabled(true);
                            registerButton.setEnabled(true);
                        }
                    }
                });
    }

}
