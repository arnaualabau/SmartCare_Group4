package com.example.smartcare_group4.ui.init.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.smartcare_group4.data.Device;
import com.example.smartcare_group4.data.User;
import com.example.smartcare_group4.ui.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginFragment extends Fragment {


    private LoginViewModel loginViewModel;

    private Button loginButton;
    private EditText emailText;
    String email = "";
    private EditText passwdText;
    String password = "";
    private String result;

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

        emailText = v.findViewById(R.id.emailLogin);
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

        passwdText = v.findViewById(R.id.passwordLogin);
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
                //print.debug("Pass", password);
            }
        });

        loginButton = v.findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (! (loginViewModel.emptyTexts(passwdText.getText().toString()) || loginViewModel.emptyTexts(emailText.getText().toString()) ) ) {
                    //CALL FIREBASE
                    loginViewModel.login(emailText.getText().toString(),
                            passwdText.getText().toString()
                    ).observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            result = s;
                            if (!s.equals(getString(R.string.ERROR))) {

                                loginViewModel.getUserInfo(result).observe(getViewLifecycleOwner(), new Observer<User>() {
                                    @Override
                                    public void onChanged(User user) {
                                        if (!user.getEmail().equals(getString(R.string.ERROR))) {

                                            //GET DEVICE INFO

                                            loginViewModel.setHWId(user.getHardwareId());
                                            loginViewModel.getDeviceInfo().observe(getViewLifecycleOwner(), new Observer<Device>() {
                                                @Override
                                                public void onChanged(Device device) {

                                                    if (!device.getHardwareId().equals(getString(R.string.ERROR))) {
                                                        //CHANGE ACTIVITY

                                                        Intent loginToProfile = new Intent(getActivity(), MainActivity.class);

                                                        loginToProfile.putExtra("email", user.getEmail());
                                                        loginToProfile.putExtra("name", user.getUsername());
                                                        loginToProfile.putExtra("hardwareId", user.getHardwareId());
                                                        loginToProfile.putExtra("patient", user.isPatient());
                                                        loginToProfile.putExtra("imageBool", user.isImageTaken());

                                                        loginToProfile.putExtra("light", device.getLightSensor());
                                                        loginToProfile.putExtra("tap", device.getTap());
                                                        loginToProfile.putExtra("presence", device.getPresenceSensor());


                                                        FirebaseMessaging.getInstance().subscribeToTopic(user.getHardwareId())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        String msg = "Subscribed";
                                                                        if (!task.isSuccessful()) {
                                                                            msg = "Subscribe failed";
                                                                        } else {
                                                                            startActivity(loginToProfile);
                                                                        }
                                                                    }
                                                                });

                                                        //Empty text fields
                                                        emailText.setText("");
                                                        passwdText.setText("");

                                                    } else {
                                                        //READ DEVICE INFO FAILS
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                        builder.setMessage(R.string.ERROR_DEVICE)
                                                                .setTitle(R.string.ERROR_MSG)
                                                                .setPositiveButton(android.R.string.ok, null);
                                                        builder.show();

                                                        //Empty text fields
                                                        emailText.setText("");
                                                        passwdText.setText("");
                                                    }
                                                }
                                            });

                                        } else {
                                            //READ USER INFO FAILS
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setMessage(R.string.ERROR_USER)
                                                    .setTitle(R.string.ERROR_MSG)
                                                    .setPositiveButton(android.R.string.ok, null);
                                            builder.show();

                                            //Empty text fields
                                            emailText.setText("");
                                            passwdText.setText("");

                                        }
                                    }
                                });

                            } else if (s.equals(getString(R.string.ERROR))) {
                                //LOGIN FAILS
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(R.string.ERROR_CREDENTIALS)
                                        .setTitle(R.string.ERROR_MSG)
                                        .setPositiveButton(android.R.string.ok, null);
                                builder.show();

                            }
                        }
                    });
                } else {
                    //User has not filled in all fields
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.ERROR_EMPTYTEXT)
                            .setTitle(R.string.ERROR_MSG)
                            .setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }

            }
        });
    }
}