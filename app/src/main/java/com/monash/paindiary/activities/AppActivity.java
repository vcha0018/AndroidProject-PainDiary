package com.monash.paindiary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.BackoffPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.monash.paindiary.R;
import com.monash.paindiary.adapter.RecyclerViewAdapter;
import com.monash.paindiary.apis.weather.RetrofitClient;
import com.monash.paindiary.apis.weather.RetrofitInterface;
import com.monash.paindiary.apis.weather.WeatherResponse;
import com.monash.paindiary.databinding.ActivityAppBinding;
import com.monash.paindiary.entity.PainRecord;
import com.monash.paindiary.enums.FragmentEnums;
import com.monash.paindiary.enums.NavigationItem;
import com.monash.paindiary.fragments.MapViewFragment;
import com.monash.paindiary.fragments.PainDataEntryFragment;
import com.monash.paindiary.fragments.PainRecordViewFragment;
import com.monash.paindiary.fragments.ReportViewFragment;
import com.monash.paindiary.viewmodel.PainRecordViewModel;
import com.monash.paindiary.workmanagers.WeatherFetchWork;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppActivity extends AppCompatActivity {

    private ActivityAppBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private NavHostFragment navHostFragment;
    private NavController navController;
    private WorkManager workManager;

    private RecyclerView.LayoutManager layoutManager;
    private PainRecordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        setSupportActionBar(binding.appBar.toolbar);
        appBarConfiguration = new AppBarConfiguration
                .Builder(
                R.id.nav_home_fragment,
                R.id.nav_pain_record_view_fragment,
                R.id.nav_pain_data_entry_fragment,
                R.id.nav_report_view_fragment,
                R.id.nav_map_view_fragment)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        //Sets up a NavigationView for use with a NavController.
        NavigationUI.setupWithNavController(binding.navView, navController);
        //Sets up a Toolbar for use with a NavController.
        NavigationUI.setupWithNavController(binding.appBar.toolbar, navController, appBarConfiguration);
//        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//        });

    }

    private void SetWorkForWeatherInfo() {
        PeriodicWorkRequest weatherFetchRequest =
                new PeriodicWorkRequest
                        .Builder(WeatherFetchWork.class, 1, TimeUnit.HOURS)
                        .setBackoffCriteria(
                                BackoffPolicy.LINEAR,
                                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
                                TimeUnit.MILLISECONDS)
                        .addTag("WeatherFetchWork")
                        .build();

        workManager = WorkManager.getInstance(this);
        workManager.enqueue(weatherFetchRequest);
    }

    public void ManualSelectNavigationItem(@NonNull NavigationItem navigationItem) {
        switch (navigationItem) {
            case HomeView:
                binding.navView.setCheckedItem(R.id.nav_home_fragment);
                break;
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
        if (navController.getCurrentDestination().getId() == R.id.nav_home_fragment) {
//            workManager.cancelAllWorkByTag("WeatherFetchWork");
            finishAffinity();
        }
        super.onBackPressed();
    }
}