package com.monash.paindiary.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.monash.paindiary.R;
import com.monash.paindiary.activities.AppActivity;
import com.monash.paindiary.activities.MainActivity;
import com.monash.paindiary.databinding.FragmentSignInBinding;
import com.monash.paindiary.enums.FragmentEnums;
import com.monash.paindiary.helper.UserInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO 1. Incorrect fields error messages
// TODO 2. Background theme change

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {
    private FragmentSignInBinding binding;
    private FirebaseAuth auth;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_sign_in, container, false);
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnCreateAccount.setOnClickListener(v -> {
            ((MainActivity) getActivity()).changeFragment(FragmentEnums.SignUp);
        });

        binding.btnSignIn.setOnClickListener(v -> {
            if (inputValidationCheck()) {
                auth.signInWithEmailAndPassword(binding.editEmail.getText().toString(), binding.editPassword.getText().toString())
                        .addOnSuccessListener(authResult -> {
                            Toast.makeText(getContext(), "Login Success!", Toast.LENGTH_SHORT).show();
                            UserInfo.setINSTANCE(binding.editEmail.getText().toString(), binding.editPassword.getText().toString(), true);
                            Intent intent = new Intent(getActivity(), AppActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        })
                        .addOnFailureListener(e -> {
                            // TODO: Find a way to detect username or password is incorrect or both.
                            Toast.makeText(getContext(), "Invalid username or password. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.println(Log.ERROR, "EXCEPTION", e.getMessage());
                        });
            }
        });

        binding.editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.inputLayoutPassword.setEndIconVisible(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputLayoutPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.inputLayoutEmail.setEndIconVisible(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean inputValidationCheck() {
        boolean isAllValid = true;
        String email = binding.editEmail.getText().toString();
        String password = binding.editPassword.getText().toString();

        if (email.isEmpty()) {
            binding.inputLayoutEmail.setEndIconVisible(false);
            binding.inputLayoutEmail.setError("Email cannot be blank");
            isAllValid = false;
        } else if (!isEmailValid(email)) {
            binding.inputLayoutEmail.setEndIconVisible(false);
            binding.inputLayoutEmail.setError("Not valid email id.");
            isAllValid = false;
        }
        if (password.isEmpty()) {
            binding.inputLayoutPassword.setEndIconVisible(false);
            binding.inputLayoutPassword.setError("Password cannot be blank");
            isAllValid = false;
        }
        return isAllValid;
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isPasswordValid(String password) {
        //complete pattern that should force the user to use digits, lower case, upper case and special characters.
        // ^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,20}$
        String expression = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\\\S+$).{4,12}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}