package com.example.smartcare_group4.ui.init.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcare_group4.R;

public class SignupFragment extends Fragment {

    private SignupViewModel signupViewModel;
    private TextView textView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        bindViews(v);

        //binding = FragmentSignupBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();

        //textView = binding.textSignup;
        //signupViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void bindViews(View v) {
        textView = v.findViewById(R.id.text_signup);
        textView.setText("-----");

        /*button1 = v.findViewById(R.id.button1);
        button1.setText("LOGIN");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.checkInputsAndLogin(PreferenceUtil.getInstance(requireContext()), "NAME", "EMAIL", "PASSWORD", "PASSWORD").observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        //TODO process errors or advance
                    }
                });
            }
        });

        button2 = v.findViewById(R.id.button2);
        button2.setText("STOP");*/
    }
}