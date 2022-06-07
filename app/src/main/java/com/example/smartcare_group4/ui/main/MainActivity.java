package com.example.smartcare_group4.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.databinding.ActivityMainBinding;
import com.example.smartcare_group4.data.Device;
import com.example.smartcare_group4.data.User;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    TextView navigationTitle;
    TextView navigationSubTitle;

    private User user = new User();
    private Device device = new Device();


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
        //NavController navController = Navigation.findNavController(navigationView).navigate(R.id.nav_host_fragment_content_main, bundle);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        navigationTitle = (TextView) headerView.findViewById(R.id.NavigationTitle);
        navigationTitle.setText("user.getUsername()");
        navigationSubTitle = (TextView) headerView.findViewById(R.id.NavigationSubTitle);
        navigationSubTitle.setText("user.getEmail()");


        //navigationTitle = (TextView) binding.drawerLayout.findViewById(R.id.NavigationTitle);
        //navigationTitle = (TextView) findViewById(R.id.NavigationTitle);
        //navigationSubTitle = (TextView) findViewById(R.id.NavigationSubTitle);
        //navigationTitle.setText("AAAAA");

        /*
        user.setUsername(getIntent().getStringExtra("name"));
        user.setEmail(getIntent().getStringExtra("email"));
        user.setPatient(getIntent().getBooleanExtra("patient", false));
        user.setHardwareId(getIntent().getStringExtra("hardwareId"));

        device.setHardwareId(getIntent().getStringExtra("hardwareId"));
        device.setLightSensor(getIntent().getIntExtra("light", 0));
        device.setTap(getIntent().getIntExtra("tap", 0));
        device.setPresenceSensor(getIntent().getIntExtra("presence", 0));
        */

        //no se com es fa per posar el text
        //navigationTitle.setText(user.getUsername());
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