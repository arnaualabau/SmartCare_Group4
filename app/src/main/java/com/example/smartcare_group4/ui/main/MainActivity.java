package com.example.smartcare_group4.ui.main;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.data.User;
import com.example.smartcare_group4.data.repository.FirebaseRepository;
import com.example.smartcare_group4.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    TextView navigationTitle;
    TextView navigationSubTitle;
    ImageView navigationImage;

    private User user = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Help is on the way", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                FirebaseRepository.firebaseInstance.setValuesEmergency().observe(MainActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.equals(getString(R.string.SUCCESS))) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(R.string.EMERGENCY_MSG)
                                    .setTitle(R.string.SOS_MSG);
                            builder.show();

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(R.string.VALUE_NOT_SAVED_MSG)
                                    .setTitle(R.string.ERROR_MSG);
                            builder.show();

                        }
                    }
                });
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.drawerNavView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_sensors, R.id.nav_planning, R.id.nav_settings, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        user.setUsername(getIntent().getStringExtra("name"));
        user.setEmail(getIntent().getStringExtra("email"));
        View headerView = navigationView.getHeaderView(0);
        navigationTitle = (TextView) headerView.findViewById(R.id.NavigationTitle);
        navigationTitle.setText(user.getUsername());
        navigationSubTitle = (TextView) headerView.findViewById(R.id.NavigationSubTitle);
        navigationSubTitle.setText(user.getEmail());
        navigationImage = (ImageView) headerView.findViewById(R.id.NavigationImage);

        if (getIntent().getBooleanExtra("imageBool", false)) {
            byte[] img = getIntent().getByteArrayExtra("profilePic");
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            navigationImage.setImageBitmap(imageBitmap);
        }
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}