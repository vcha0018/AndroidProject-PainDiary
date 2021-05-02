package com.monash.paindiary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monash.paindiary.R;
import com.monash.paindiary.activities.MainActivity;
import com.monash.paindiary.databinding.FragmentSignUpBinding;
import com.monash.paindiary.enums.FragmentEnums;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding binding;

    public SignUpFragment() {
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
//        return inflater.inflate(R.layout.fragment_sign_up, container, false);
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnClose.setOnClickListener(v -> {
            ((MainActivity) getActivity()).changeFragment(FragmentEnums.SIGNIN);
        });

        binding.btnSignIn.setOnClickListener(v -> {
            ((MainActivity) getActivity()).changeFragment(FragmentEnums.SIGNIN);
        });

        binding.btnNext.setOnClickListener(v -> {
            if (binding.btnNext.getText().equals("NEXT")) {
                binding.btnNext.setText(R.string.button_signup);
                binding.btnBack.setEnabled(true);
                binding.viewFlipper.setInAnimation(getContext(), R.anim.slide_in_right);
                binding.viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_left);
                binding.viewFlipper.showNext();
            } else {
                //TODO: Code for Sign up process
            }

        });

        binding.btnBack.setOnClickListener(v -> {
            binding.btnNext.setText(R.string.button_next);
            binding.btnBack.setEnabled(false);
            binding.viewFlipper.setInAnimation(getContext(), R.anim.slide_in_left);
            binding.viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_right);
            binding.viewFlipper.showPrevious();
        });

        binding.inputEmailAddress.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.inputEmailAddress.setHint("abc@xyz.com");
            } else {
                binding.inputEmailAddress.setHint("");
            }
        });

        binding.inputBirthDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.inputBirthDate.setHint("dd/mm/yyyy");
            } else {
                binding.inputBirthDate.setHint("");
            }
        });

        binding.inputPhoneNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.inputPhoneNumber.setHint("+61000000000");
            } else {
                binding.inputPhoneNumber.setHint("");
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}