package com.monash.paindiary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.monash.paindiary.enums.FragmentEnums;
import com.monash.paindiary.R;
import com.monash.paindiary.databinding.ActivityMainBinding;
import com.monash.paindiary.fragments.SignInFragment;
import com.monash.paindiary.fragments.SignUpFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private static SignInFragment signInFragment = new SignInFragment();
    private static SignUpFragment signUpFragment = new SignUpFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        changeFragment(FragmentEnums.SIGNIN);
    }

    public void changeFragment(FragmentEnums replaceFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragmentFromStack = fragmentManager.findFragmentByTag(replaceFragment.name());
        if (fragmentFromStack != null) {
            fragmentManager.popBackStack();
        } else {
            switch (replaceFragment) {
                case SIGNIN:
                    transaction.replace(binding.fragmentContainerView.getId(), signInFragment, FragmentEnums.SIGNIN.name());
                    break;
                case SIGNUP:
                    transaction.setCustomAnimations(R.anim.slide_up, R.anim.fade_out, R.anim.fade_out, R.anim.slide_down);
                    transaction.replace(binding.fragmentContainerView.getId(), signUpFragment, FragmentEnums.SIGNUP.name());
                    transaction.addToBackStack(null);
                    break;
            }
            transaction.commit();
        }
    }
}