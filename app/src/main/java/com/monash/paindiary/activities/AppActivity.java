package com.monash.paindiary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.monash.paindiary.R;
import com.monash.paindiary.databinding.ActivityAppBinding;
import com.monash.paindiary.enums.FragmentEnums;

public class AppActivity extends AppCompatActivity {
    private ActivityAppBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBar.toolbar);
        appBarConfiguration = new AppBarConfiguration
                .Builder(
                R.id.nav_pain_record_view_fragment,
                R.id.nav_pain_data_entry_fragment,
                R.id.nav_report_view_fragment,
                R.id.nav_map_view_fragment)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        //Sets up a NavigationView for use with a NavController.
        NavigationUI.setupWithNavController(binding.navView, navController);
        //Sets up a Toolbar for use with a NavController.
        NavigationUI.setupWithNavController(binding.appBar.toolbar, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
        });

    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.nav_pain_record_view_fragment) {
            finishAffinity();
        }
        super.onBackPressed();
    }
}