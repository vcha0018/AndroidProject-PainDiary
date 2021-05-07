package com.monash.paindiary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.monash.paindiary.R;
import com.monash.paindiary.databinding.ActivityAppBinding;
import com.monash.paindiary.enums.FragmentEnums;
import com.monash.paindiary.enums.NavigationItem;
import com.monash.paindiary.fragments.MapViewFragment;
import com.monash.paindiary.fragments.PainDataEntryFragment;
import com.monash.paindiary.fragments.PainRecordViewFragment;
import com.monash.paindiary.fragments.ReportViewFragment;

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

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

    public void ManualSelectNavigationItem(@NonNull NavigationItem navigationItem) {
        switch (navigationItem) {
            case RecordView:
                binding.navView.setCheckedItem(R.id.nav_pain_record_view_fragment);
                break;
            case ReportView:
                binding.navView.setCheckedItem(R.id.nav_report_view_fragment);
                break;
            case MapView:
                binding.navView.setCheckedItem(R.id.nav_map_view_fragment);
                break;
            case DataEntry:
                binding.navView.setCheckedItem(R.id.nav_pain_data_entry_fragment);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.nav_pain_record_view_fragment) {
            finishAffinity();
        }
        super.onBackPressed();
    }
}