package com.monash.paindiary.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.monash.paindiary.R;
import com.monash.paindiary.activities.AppActivity;
import com.monash.paindiary.activities.MainActivity;
import com.monash.paindiary.databinding.FragmentSignInBinding;
import com.monash.paindiary.enums.FragmentEnums;

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
            auth.signInWithEmailAndPassword(binding.editEmail.getText().toString(), binding.editPassword.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        Toast.makeText(getContext(), "Login Success!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), AppActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    })
                    .addOnFailureListener(e -> {
                        // TODO: Find a way to detect username or password is incorrect or both.
                        Toast.makeText(getContext(), "Login Fail!!", Toast.LENGTH_SHORT).show();
                        Log.println(Log.ERROR, "EXCEPTION", e.getMessage());
                    });
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}