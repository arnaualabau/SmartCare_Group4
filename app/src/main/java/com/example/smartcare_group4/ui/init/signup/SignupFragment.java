package com.example.smartcare_group4.ui.init.signup;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.ui.main.MainActivity;
import com.example.smartcare_group4.utils.PrintLog;
import com.example.smartcare_group4.viewmodel.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupFragment extends Fragment {

    private SignupViewModel signupViewModel;
    private DatabaseReference mDatabase;
    private TextView textView;

    private Button SignUpButton;
    private EditText emailText;
    String email = "";
    private EditText passwdText;
    String password = "";
    //private Button changeButton;
    private String result;

    PrintLog print;

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

        emailText = v.findViewById(R.id.emailSignUp);
        emailText.setText("a@a.com");
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

        passwdText = v.findViewById(R.id.passwordSignUp);
        passwdText.setText("123456");
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
                Log.d("Pass", password);
            }
        });


        SignUpButton = v.findViewById(R.id.SignUp);
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cridar a firebase
                signupViewModel.signUp(emailText.getText().toString(),
                        passwdText.getText().toString()
                ).observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        result = s;
                        if (s.equals("success")) {
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            String name = "hola";
                            User user = new User(name, email, password, false);
                            Log.d("USER", "tot ok abans de create user");
                            mDatabase.child("users").child(name).setValue("PROVA").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("USER", "success");
                                        Intent loginToProfile = new Intent(getActivity(), MainActivity.class);
                                        //loginToProfile.putExtra("email", email);
                                        Log.d("USER", "tot ok despres de create user 1 " + email);
                                        startActivity(loginToProfile);
                                        getActivity().finish();

                                    } else {
                                        Log.d("USER", "failure:"+task.getException().getLocalizedMessage());
                                    }

                                }
                            });


                        } else if (s.equals("error")) {
                            //gestionar que posem aqui
                            Log.d("USER", "error");
                        }
                    }
                });


            }
        });
    }
}