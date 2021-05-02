package com.monash.paindiary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monash.paindiary.R;
import com.monash.paindiary.activities.MainActivity;
import com.monash.paindiary.databinding.FragmentSignInBinding;
import com.monash.paindiary.enums.FragmentEnums;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {
    FragmentSignInBinding binding;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_sign_in, container, false);
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnCreateAccount.setOnClickListener(v -> {
            ((MainActivity) getActivity()).changeFragment(FragmentEnums.SIGNUP);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}