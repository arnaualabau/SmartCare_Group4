package com.example.smartcare_group4.ui.init.signup;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.data.constants.Generic;
import com.example.smartcare_group4.ui.main.MainActivity;
import com.example.smartcare_group4.utils.PrintLog;
import com.example.smartcare_group4.viewmodel.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import android.content.pm.PackageManager;


public class SignupFragment extends Fragment {

    private SignupViewModel signupViewModel;

    private Button SignUpButton;
    private RadioButton radioButton;
    private RadioGroup radioGroup;

    int selectedRadioButton;

    private EditText emailText;
    String email = "";
    private EditText passwdText;
    private EditText passwd2Text;
    String password = "";
    String password2 = "";
    private EditText hardwareIdText;
    String hardwareId = "";
    private EditText nameText;
    String name = "";

    private String result;

    private Button setProfilePic;
    String currentPhotoPath;
    ImageView profilePic;
    private static final int CAMERA_PERMISSIONS_REQUEST = 14;
    private static final int READ_PERMISSIONS_REQUEST = 15;
    private static final int WRITE_PERMISSIONS_REQUEST = 16;
    private static final int REQUEST_IMAGE_CAPTURE = 1;


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

        radioGroup = v.findViewById(R.id.userTypeSignup);

        emailText = v.findViewById(R.id.emailSignup);
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

        nameText = v.findViewById(R.id.nameSignup);
        nameText.setText("María");
        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = nameText.getText().toString();
            }
        });

        passwdText = v.findViewById(R.id.passwordSignup);
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
            }
        });

        passwd2Text = v.findViewById(R.id.passwordSignup2);
        passwd2Text.setText("123456");
        passwd2Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                password2 = passwd2Text.getText().toString();
            }
        });

        hardwareIdText = v.findViewById(R.id.hardwareIDSignup);
        hardwareIdText.setText("123456");
        hardwareIdText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                hardwareId = hardwareIdText.getText().toString();
            }
        });

        SignUpButton = v.findViewById(R.id.buttonSignup);
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                selectedRadioButton = radioGroup.getCheckedRadioButtonId();
                radioButton = v.findViewById(selectedRadioButton);

                //CHECK PASSWORDS
                if (!signupViewModel.checkPSW(passwdText.getText().toString(), passwd2Text.getText().toString())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(Generic.ERROR_PASSWORDS)
                            .setTitle(Generic.ERROR);
                    builder.show();
                } else {
                    //CALL FIREBASE
                    signupViewModel.signUp(emailText.getText().toString(),
                            passwdText.getText().toString()
                    ).observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            result = s;
                            if (!s.equals("error")) {
                                Log.d("SIGNUP", "success");
                                boolean patient = false;
                                email = emailText.getText().toString();
                                password = passwdText.getText().toString();
                                name = nameText.getText().toString();
                                hardwareId = hardwareIdText.getText().toString();

                                //Log.d("RADIO BUTTON", radioButton.toString());

                                //Log.d("RADIO BUTTON", Integer.toString(selectedRadioButton));
                                signupViewModel.registerUser(name, email, password, result, hardwareId, patient).observe(getViewLifecycleOwner(), new Observer<String>() {
                                    @Override
                                    public void onChanged(String s) {
                                        if (s.equals("success register")) {
                                            Log.d("SIGNUP", "register DB success");
                                            Intent loginToProfile = new Intent(getActivity(), MainActivity.class);
                                            loginToProfile.putExtra("email", email);
                                            loginToProfile.putExtra("name", name);
                                            loginToProfile.putExtra("hardwareId", hardwareId);
                                            loginToProfile.putExtra("patient", patient);
                                            //password no se si cal
                                            startActivity(loginToProfile);
                                            //getActivity().finish();
                                        } else if (s.equals("error register")) {
                                            Log.d("SIGNUP", "register DB error");
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




            }
        });
    }
}