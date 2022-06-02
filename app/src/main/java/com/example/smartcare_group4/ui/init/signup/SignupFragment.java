package com.example.smartcare_group4.ui.init.signup;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.data.constants.Generic;
import com.example.smartcare_group4.ui.main.MainActivity;

public class SignupFragment extends Fragment {

    private SignupViewModel signupViewModel;

    private Button SignUpButton;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private ImageButton editPictureButton;

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

    public void bindViews(View v) {

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

                if (! (signupViewModel.emptyTexts(passwdText.getText().toString()) || signupViewModel.emptyTexts(passwd2Text.getText().toString()) || signupViewModel.emptyTexts(emailText.getText().toString()) || signupViewModel.emptyTexts(nameText.getText().toString()) ||  signupViewModel.emptyTexts(hardwareIdText.getText().toString())) ) {
                    //CHECK PASSWORDS
                    if (!signupViewModel.checkPSW(passwdText.getText().toString(), passwd2Text.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(Generic.ERROR_PASSWORDS)
                                .setTitle(Generic.ERROR);
                        builder.show();
                    } else {
                        //CALL FIREBASE
                        Log.d("SIGNUP", "entra a call firebase");
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
                                    signupViewModel.registerUser(name, email, result, hardwareId, patient).observe(getViewLifecycleOwner(), new Observer<String>() {
                                        @Override
                                        public void onChanged(String s) {
                                            if (s.equals("success register")) {
                                                //Log.d("SIGNUP", "register DB success");
                                                Intent loginToProfile = new Intent(getActivity(), MainActivity.class);
                                                loginToProfile.putExtra("email", email);
                                                loginToProfile.putExtra("name", name);
                                                loginToProfile.putExtra("hardwareId", hardwareId);
                                                loginToProfile.putExtra("patient", patient);
                                                //password no se si cal

                                                startActivity(loginToProfile);

                                            } else if (s.equals("error register")) {
                                                //ERROR IN REGISTERING IN FIREBASE
                                                //Log.d("SIGNUP", "register DB error");
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setMessage(Generic.ERROR_REGISTER)
                                                        .setTitle(Generic.ERROR);
                                                builder.show();
                                                //TODO: quines son les consequencies d'aquest error?
                                            }
                                        }
                                    });


                                } else if (s.equals("error")) {
                                    //ERROR IN SIGN UP IN FIREBASE
                                    //Log.d("USER", "error");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage(Generic.ERROR_SIGNUP)
                                            .setTitle(Generic.ERROR);
                                    builder.show();
                                }
                            }
                        });
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(Generic.ERROR_EMPTYTEXT)
                            .setTitle(Generic.ERROR);
                    builder.show();
                }


            }
        });


        editPictureButton = v.findViewById(R.id.editPictureSignup);
        editPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(Generic.CAMERA_MSG)
                        .setTitle(Generic.CAMERA_TITLE)
                        .setIcon(android.R.drawable.ic_menu_camera)
                        .setCancelable(true)
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                PackageManager packageManager = getActivity().getPackageManager();
                                if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == false) {

                                    Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                                            .show();
                                } else {

                                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                                            == PackageManager.PERMISSION_DENIED) {

                                        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSIONS_REQUEST);
                                    } else {

                                        //el permiso de la camara ya ha sido aceptado
                                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                == PackageManager.PERMISSION_DENIED) {

                                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSIONS_REQUEST);
                                        } else {
                                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                                    == PackageManager.PERMISSION_DENIED) {

                                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSIONS_REQUEST);
                                            } else {

                                                dispatchTakePictureIntent();
                                            }

                                        }


                                    }

                                }
                            }
                        });
                builder.show();

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case CAMERA_PERMISSIONS_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {

                    new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                            .setTitle(Generic.CAMERA_TITLE)
                            .setMessage(Generic.CAMERA_DENIED)
                            .setNegativeButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                return;
            case READ_PERMISSIONS_REQUEST:
            case WRITE_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {

                    new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                            .setTitle(Generic.STORAGE_TITLE)
                            .setMessage(Generic.STORAGE_DENIED)
                            .setNegativeButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                return;

        }
    }

    private void dispatchTakePictureIntent() {

        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        // Ensure that there's a camera activity to handle the intent
        CameraActivity activity = (CameraActivity)getActivity();
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast toast = Toast.makeText(activity, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri fileUri = Uri.fromFile(photoFile);
                activity.setCapturedImageURI(fileUri);
                activity.setCurrentPhotoPath(fileUri.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        activity.getCapturedImageURI());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }*/
    }

}