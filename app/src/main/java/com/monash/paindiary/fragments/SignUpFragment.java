package com.monash.paindiary.fragments;

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
import com.monash.paindiary.activities.MainActivity;
import com.monash.paindiary.databinding.FragmentSignUpBinding;
import com.monash.paindiary.enums.FragmentEnums;

// TODO 1. Incorrect fields error messages
// TODO 2. Background theme change
// TODO 3. Find a way to store user data currently firebase support only email and password.
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding binding;
    private FirebaseAuth auth;

    public SignUpFragment() {
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
//        return inflater.inflate(R.layout.fragment_sign_up, container, false);
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnClose.setOnClickListener(v -> {
            ((MainActivity) getActivity()).changeFragment(FragmentEnums.SIGNIN);
        });

        binding.btnSignIn.setOnClickListener(v -> {
            ((MainActivity) getActivity()).changeFragment(FragmentEnums.SIGNIN);
        });

        binding.btnNextSignup.setOnClickListener(v -> {
            if (binding.btnNextSignup.getText().equals("NEXT")) {
                binding.btnNextSignup.setText(R.string.button_signup);
                binding.btnBack.setEnabled(true);
                binding.viewFlipper.setInAnimation(getContext(), R.anim.slide_in_right);
                binding.viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_left);
                binding.viewFlipper.showNext();
            } else {
                auth.createUserWithEmailAndPassword(binding.editEmail.getText().toString(), binding.editCreatePassword.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "New user created!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Fail to create new user", Toast.LENGTH_SHORT).show();
                                Log.d("MESSAGE FROM FIREBASE", task.getResult().toString());
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Login Fail!!", Toast.LENGTH_SHORT).show();
                            Log.println(Log.ERROR, "EXCEPTION", e.getMessage());
                        });
            }

        });

        binding.btnBack.setOnClickListener(v -> {
            binding.btnNextSignup.setText(R.string.button_next);
            binding.btnBack.setEnabled(false);
            binding.viewFlipper.setInAnimation(getContext(), R.anim.slide_in_left);
            binding.viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_right);
            binding.viewFlipper.showPrevious();
        });

        binding.editEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.editEmail.setHint("abc@xyz.com");
            } else {
                binding.editEmail.setHint("");
            }
        });

        binding.editBirthDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.editBirthDate.setHint("dd/mm/yyyy");
            } else {
                binding.editBirthDate.setHint("");
            }
        });

        binding.editPhoneNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.editPhoneNumber.setHint("+61000000000");
            } else {
                binding.editPhoneNumber.setHint("");
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