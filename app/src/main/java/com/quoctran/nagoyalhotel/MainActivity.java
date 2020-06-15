package com.quoctran.nagoyalhotel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.quoctran.nagoyalhotel.activities.LoginActivity;
import com.quoctran.nagoyalhotel.activities.ProfileActivity;
import com.quoctran.nagoyalhotel.models.User;
import com.quoctran.nagoyalhotel.utils.FirebaseUtils;
import com.squareup.picasso.Picasso;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private TextView nameTextView, emailTextView;
    private CircleImageView imageView;
    private MaterialButton logoutButton;
    private MaterialButton editProfileButton;
    private boolean doubleBackToExitPressedOnce = false;
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentUser = FirebaseUtils.getCurrentUser();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialDrawer();
    }



    private void initialDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        loadNavHeader(navigationView.getHeaderView(0));
    }

    private void loadNavHeader(View view) {
        nameTextView = view.findViewById(R.id.display_name);
        emailTextView = view.findViewById(R.id.display_email);
        imageView = view.findViewById(R.id.display_image);
        logoutButton = view.findViewById(R.id.logout_button);
        editProfileButton = view.findViewById(R.id.edit_profile);

        try {
            FirebaseUtils.getDocument(FirebaseUtils.PROFILES_REF, currentUser.getUid())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            if (snapshot != null && snapshot.exists()) {
                                Map<String, Object> userMaper= snapshot.getData();
                                User user = new User();
                                user.setName(userMaper.get("name") != null ? userMaper.get("name").toString() : "");
                                user.setPhone(userMaper.get("phone") != null ? userMaper.get("phone").toString() : "");
                                user.setAvatar(userMaper.get("avatar") != null ? userMaper.get("avatar").toString(): "");
                                user.setEmail(currentUser.getEmail());
                                setNavHeaderData(user);
                            }
                        }
                    });
        } catch (Exception e) {
            //
        }
        actionListener();
    }

    private void setNavHeaderData(User user) {
        nameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            Picasso.get()
                    .load(user.getAvatar())
                    .error(R.mipmap.ic_launcher_round)
                    .into(imageView);
        }
    }

    private void actionListener() {

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtils.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                intentProfile.putExtra("idUser", currentUser.getUid());
                startActivity(intentProfile);

            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.exitMsg), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
