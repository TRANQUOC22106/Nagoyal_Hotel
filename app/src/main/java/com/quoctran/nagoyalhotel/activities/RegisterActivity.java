package com.quoctran.nagoyalhotel.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.quoctran.nagoyalhotel.MainActivity;
import com.quoctran.nagoyalhotel.R;
import com.quoctran.nagoyalhotel.models.Profiles;
import com.quoctran.nagoyalhotel.models.User;
import com.quoctran.nagoyalhotel.utils.FirebaseUtils;
import com.quoctran.nagoyalhotel.utils.ValidationUtils;

import io.opencensus.internal.StringUtils;

import static com.quoctran.nagoyalhotel.utils.FirebaseUtils.PROFILES_REF;
import static com.quoctran.nagoyalhotel.utils.ValidationUtils.isEmailValid;
import static com.quoctran.nagoyalhotel.utils.ValidationUtils.isPasswordValid;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText nameInput, phoneInput, emailInput, passwordInput,
            confirmPasswordInput;
    private ProgressDialog loadingBar;
    private TextInputLayout nameLayout, phoneLayout, emailLayout, passwordLayout,
            confirmPasswordLayout;
    private MaterialButton loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        preRegister();
    }

    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void preRegister() {
        loadingBar = new ProgressDialog(this, R.style.DialogStyleApp);
        nameLayout= findViewById(R.id.name_text_input);
        phoneLayout = findViewById(R.id.phone_text_input);
        emailLayout = findViewById(R.id.email_text_input);
        passwordLayout = findViewById(R.id.password_text_input);
        confirmPasswordLayout = findViewById(R.id.confirm_password_text_input);
        nameInput = findViewById(R.id.name_edit_text);
        phoneInput = findViewById(R.id.phone_edit_text);
        emailInput = findViewById(R.id.email_edit_text);
        passwordInput = findViewById(R.id.password_edit_text);
        confirmPasswordInput = findViewById(R.id.confirm_password_edit_text);

        loginButton = findViewById(R.id.re_login_button);
        registerButton = findViewById(R.id.re_register_button);

        actionListener();
    }

    private void clearError() {
        nameLayout.setError(null);
        phoneLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);
    }

    private void actionListener() {
        final boolean[] isClicked = {false};
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isClicked[0]) {
                    isClicked[0] = true;
                    registerAction();
                    isClicked[0] = false;
                }
            }
        });

        confirmPasswordLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                clearError();
                return false;
            }
        });

        passwordLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                clearError();
                return false;
            }
        });

        emailInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                clearError();
                return false;
            }
        });

        phoneInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                clearError();
                return false;
            }
        });

        nameInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                clearError();
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerAction() {
        boolean isError = false;
        Editable name, email, phone, password, confirmPassword;
        name = nameInput.getText();
        phone = phoneInput.getText();
        email = emailInput.getText();
        password = passwordInput.getText();
        confirmPassword = confirmPasswordInput.getText();

        if (!ValidationUtils.isValidText(name)) {
            isError = true;
            nameLayout.setError(getString(R.string.error_name));
        }

        if (!ValidationUtils.isValidPhoneNumber(phone.toString())) {
            isError = true;
            phoneLayout.setError(getString(R.string.error_phone));
        }

        if (!ValidationUtils.isEmailValid(email)) {
            isError = true;
            emailLayout.setError(getString(R.string.error_email));
        }

        if (!ValidationUtils.isPasswordValid(password)) {
            isError = true;
            passwordLayout.setError(getString(R.string.error_password));
        }

        if (!ValidationUtils.isCorrectVerifyPassword(password, confirmPassword)) {
            isError = true;
            confirmPasswordLayout.setError(getString(R.string.error_password_verify));
        }

        if (!isError) {
            User user = new User();
            user.setName(name.toString());
            user.setPhone(phone.toString());
            user.setEmail(email.toString());
            user.setPassword(password.toString());
            user.setAvatar("https://cdn2.iconfinder.com/data/icons/avatar-profile/421/avatar_contact_account_user_default_portait-512.png");
            register(user);
        }
    }
    @Override
    public void onBackPressed() {

        return;
    }
    private void register(final User user) {
        loadingBar.setTitle(getString(R.string.registerTitle));
        loadingBar.setMessage(getString(R.string.please_wait_msg));
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);
        FirebaseUtils.FIREBASE_AUTH
                .createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            FirebaseUser currentUser = FirebaseUtils.getCurrentUser();
                            Profiles profiles = new Profiles();
                            profiles.setPhone(user.getPhone());
                            profiles.setName(user.getName());
                            profiles.setAvatar(user.getAvatar());
                            FirebaseUtils.getDocument(PROFILES_REF, currentUser.getUid()).set(profiles);
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(RegisterActivity.this,
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            loginButton.setEnabled(true);
                            registerButton.setEnabled(true);
                        }
                    }
                });
    }
}
