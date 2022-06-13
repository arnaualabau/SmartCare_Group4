package com.example.smartcare_group4.ui.init.signup;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.ui.main.MainActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignupFragment extends Fragment {

    private SignupViewModel signupViewModel;

    private Button SignUpButton;
    private RadioButton patientButton;
    private RadioButton relativeButton;

    private ImageButton editPictureButton;

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

    private Boolean imgTaken = false;
    String currentPhotoPath;
    ImageView profilePic;
    private static final int REQUEST_CODE = 14;
    private static final int READ_PERMISSIONS_REQUEST = 15;
    private static final int WRITE_PERMISSIONS_REQUEST = 16;
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        bindViews(v);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void bindViews(View v) {

        patientButton = v.findViewById(R.id.patientSignup);
        relativeButton = v.findViewById(R.id.relativeSignup);

        emailText = v.findViewById(R.id.emailSignup);
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

                if (! (!(relativeButton.isChecked() ^ patientButton.isChecked()) || signupViewModel.emptyTexts(passwdText.getText().toString()) || signupViewModel.emptyTexts(passwd2Text.getText().toString()) || signupViewModel.emptyTexts(emailText.getText().toString()) || signupViewModel.emptyTexts(nameText.getText().toString()) ||  signupViewModel.emptyTexts(hardwareIdText.getText().toString())) ) {
                    //CHECK PASSWORDS
                    if (!signupViewModel.checkPSW(passwdText.getText().toString(), passwd2Text.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.ERROR_PASSWORDS)
                                .setTitle(R.string.ERROR_MSG);
                        builder.show();
                    } else {
                        //CALL FIREBASE
                        signupViewModel.signUp(emailText.getText().toString(),
                                passwdText.getText().toString()
                        ).observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                result = s;
                                if (!s.equals(getString(R.string.ERROR))) {
                                    boolean patient = patientButton.isChecked();
                                    email = emailText.getText().toString();
                                    password = passwdText.getText().toString();
                                    name = nameText.getText().toString();
                                    hardwareId = hardwareIdText.getText().toString();

                                    signupViewModel.registerUser(name, email, result, hardwareId, patient).observe(getViewLifecycleOwner(), new Observer<String>() {
                                        @Override
                                        public void onChanged(String s) {
                                            if (s.equals(getString(R.string.SUCCESS_REGISTER))) {
                                                Intent loginToProfile = new Intent(getActivity(), MainActivity.class);
                                                loginToProfile.putExtra("email", email);
                                                loginToProfile.putExtra("name", name);
                                                loginToProfile.putExtra("hardwareId", hardwareId);
                                                loginToProfile.putExtra("patient", patient);

                                                if (imgTaken) {
                                                    Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
                                                    signupViewModel.storeProfilePicture(email, imageBitmap).observe(getViewLifecycleOwner(), new Observer<String>() {
                                                        @Override
                                                        public void onChanged(String s) {

                                                            Log.d("SIGNUP", "SUCCESS??"+s);

                                                            if (s.equals(getString(R.string.SUCCESS))) {


                                                            } else if (s.equals(getString(R.string.ERROR))) {
                                                            }
                                                        }
                                                    });
                                                }

                                                startActivity(loginToProfile);

                                            } else if (s.equals(getString(R.string.ERROR_REGISTER))) {
                                                //ERROR IN REGISTERING IN FIREBASE
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setMessage(R.string.ERROR_REGISTER_MSG)
                                                        .setTitle(R.string.ERROR_MSG);
                                                builder.show();
                                            }
                                        }
                                    });

                                } else if (s.equals(getString(R.string.ERROR))) {
                                    //ERROR IN SIGN UP IN FIREBASE
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage(R.string.ERROR_SIGNUP_MSG)
                                            .setTitle(R.string.ERROR_MSG);
                                    builder.show();
                                }
                            }
                        });
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.ERROR_EMPTYTEXT)
                            .setTitle(R.string.ERROR_MSG);
                    builder.show();
                }


            }
        });


        profilePic = v.findViewById(R.id.pictureSignup);
        editPictureButton = v.findViewById(R.id.editPictureSignup);
        editPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.CAMERA_MSG)
                        .setTitle(R.string.CAMERA_TITLE)
                        .setIcon(android.R.drawable.ic_menu_camera)
                        .setCancelable(true)
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                PackageManager packageManager = getActivity().getPackageManager();
                                if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == false) {

                                    Toast.makeText(getActivity(), R.string.CAMERA_NO, Toast.LENGTH_SHORT)
                                            .show();
                                } else {

                                    if (requestPermissions()) {

                                        dispatchTakePictureIntent();

                                    }

                                }
                            }
                        });
                builder.show();

            }
        });

    }

    private boolean requestPermissions() {

        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(getActivity(), permissions[1]) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(getActivity(), permissions[2]) == PackageManager.PERMISSION_DENIED) {

            requestPermissions(permissions, REQUEST_CODE);

            return false;

        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {

                    new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                            .setTitle(R.string.PERMISSIONS_TITLE)
                            .setMessage(R.string.PERMISSIONS_DENIED)
                            .setNegativeButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {

            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
            profilePic.setImageBitmap(imageBitmap);
            profilePic.setBackgroundColor(Color.rgb(0,0,0));
            imgTaken = true;
        }
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast toast = Toast.makeText(getActivity(), "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),"com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}