package com.monash.paindiary.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.monash.paindiary.R;
import com.monash.paindiary.activities.AppActivity;
import com.monash.paindiary.activities.MainActivity;
import com.monash.paindiary.databinding.FragmentSignInBinding;
import com.monash.paindiary.enums.FragmentEnums;
import com.monash.paindiary.helper.Helper;
import com.monash.paindiary.helper.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {
    private final static String THIS_CLASS = "SignInFragment";
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
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.btnSignIn.setEnabled(true);

        binding.btnCreateAccount.setOnClickListener(v -> ((MainActivity) getActivity()).changeFragment(FragmentEnums.SignUp));

        binding.btnSignIn.setOnClickListener(this::btnSignInOnClicked);

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).ShowProgress(false);
    }

    private void btnSignInOnClicked(View view) {
        ((MainActivity) getActivity()).ShowProgress(true);
        hideKeyboard();
        binding.btnSignIn.setEnabled(false);
        if (inputValidationCheck()) {
            try {
                auth.signInWithEmailAndPassword(binding.editEmail.getText().toString(), binding.editPassword.getText().toString())
                        .addOnSuccessListener(authResult -> {
                            Toast.makeText(getContext(), "Login Success!", Toast.LENGTH_SHORT).show();
                            UserInfo.setINSTANCE(binding.editEmail.getText().toString(), true);
                            Intent intent = new Intent(getActivity(), AppActivity.class);
                            startActivity(intent);
                            ((MainActivity) getActivity()).ShowProgress(false);
                            binding.btnSignIn.setEnabled(true);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        })
                        .addOnFailureListener(e -> {
                            ((MainActivity) getActivity()).ShowProgress(false);
                            binding.btnSignIn.setEnabled(true);
                            if (e.getMessage().contains("network error"))
                                Toast.makeText(getContext(), "SignIn un-successful! Please check your internet connectivity and try again", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), "Invalid username or password. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.println(Log.ERROR, "EXCEPTION", e.getMessage());
                        });
            } catch (Exception e) {
                Toast.makeText(getContext(), "SignIn un-successful! Please check your internet connectivity and try again", Toast.LENGTH_SHORT).show();
                Log.e(THIS_CLASS, "EXCEPTION: btnSignInOnClicked: " + e.getMessage());
                ((MainActivity) getActivity()).ShowProgress(false);
                binding.btnSignIn.setEnabled(true);
            }
        } else {
            binding.btnSignIn.setEnabled(true);
            ((MainActivity) getActivity()).ShowProgress(false);
        }
    }

    private boolean inputValidationCheck() {
        boolean isAllValid = true;
        String email = binding.editEmail.getText().toString();
        String password = binding.editPassword.getText().toString();

        if (email.isEmpty()) {
            binding.inputLayoutEmail.setEndIconVisible(false);
            binding.inputLayoutEmail.setError("Email cannot be blank");
            isAllValid = false;
        } else if (!Helper.isEmailValid(email)) {
            binding.inputLayoutEmail.setEndIconVisible(false);
            binding.inputLayoutEmail.setError("Not valid email id");
            isAllValid = false;
        }
        if (password.isEmpty()) {
            binding.inputLayoutPassword.setEndIconVisible(false);
            binding.inputLayoutPassword.setError("Password cannot be blank");
            isAllValid = false;
        }
        return isAllValid;
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}