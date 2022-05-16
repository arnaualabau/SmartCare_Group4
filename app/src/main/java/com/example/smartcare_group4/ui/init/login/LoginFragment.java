package com.example.smartcare_group4.ui.init.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.ui.init.InitActivity;
import com.example.smartcare_group4.ui.main.MainActivity;
import com.example.smartcare_group4.utils.PrintLog;

public class LoginFragment extends Fragment {


    private LoginViewModel loginViewModel;

    private Button loginButton;
    private EditText emailText;
    String email = "";
    private EditText passwdText;
    String password = "";
    //private Button changeButton;
    private String result;

    PrintLog print;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        bindViews(v);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void bindViews(View v) {

        emailText = v.findViewById(R.id.email);
        emailText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                email = emailText.getText().toString();
            }
        });

        passwdText = v.findViewById(R.id.password);
        passwdText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                password = passwdText.getText().toString();
                print.debug("Pass", password);
            }
        });

        loginButton = v.findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cridar a firebase
                loginViewModel.login(emailText.getText().toString(),
                        passwdText.getText().toString()
                ).observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        result = s;
                        if (s.equals("success")) {

                            Intent loginToProfile = new Intent(getActivity(), MainActivity.class);
                            //loginToProfile.putExtra("email", email);
                            startActivity(loginToProfile);
                            getActivity().finish();

                        } else if (s.equals("error")) {
                            //gestionar que posem aqui
                        }
                    }
                });


            }
        });
    }
}