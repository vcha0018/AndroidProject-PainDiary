package com.monash.paindiary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.monash.paindiary.R;
import com.monash.paindiary.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}