package com.example.smartcare_group4.ui.main.profile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.data.User;
import com.example.smartcare_group4.databinding.FragmentProfileBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;

    private ImageButton editPictureButton;

    String currentPhotoPath;
    ImageView profilePic;
    private static final int REQUEST_CODE = 14;
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profilePic = root.findViewById(R.id.profilePicture);
        if (profileViewModel.imageTaken()) {
            profileViewModel.getProfilePicture().observe(getViewLifecycleOwner(), new Observer<byte[]>() {
                @Override
                public void onChanged(byte[] img) {
                    if (img.length > 0) {

                        Bitmap imageBitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                        profilePic.setImageBitmap(imageBitmap);
                        profilePic.setBackgroundResource(R.color.transparent);

                    } else {

                    }
                }
            });

        }


        //declaration
        final TextView nameValue = binding.textNameValue;
        final TextView emailValue = binding.textEmailValue;
        final TextView HWValue = binding.textHwValue;
        final TextView roleValue = binding.textRoleValue;

        //ask firebase for data
        profileViewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (!user.getEmail().equals(getString(R.string.ERROR))) {
                    profileViewModel.setNameValue(user.getUsername());
                    profileViewModel.setEmailValue(user.getEmail());
                    profileViewModel.setWDIdValue(user.getHardwareId());
                    profileViewModel.setRoleValue(user.isPatient());


                } else {
                    //READ USER INFO FAILS
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.ERROR_USER)
                            .setTitle(R.string.ERROR_MSG)
                            .setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }
            }
        });

        //update UI
        profileViewModel.getText(1).observe(getViewLifecycleOwner(), nameValue::setText);
        profileViewModel.getText(2).observe(getViewLifecycleOwner(), emailValue::setText);
        profileViewModel.getText(3).observe(getViewLifecycleOwner(), HWValue::setText);
        profileViewModel.getText(4).observe(getViewLifecycleOwner(), roleValue::setText);


        editPictureButton = root.findViewById(R.id.editPictureProfile);
        editPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ask for permission to take photo
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.CAMERA_MSG)
                        .setTitle(R.string.CAMERA_TITLE)
                        .setIcon(android.R.drawable.ic_menu_camera)
                        .setCancelable(true)
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //make sure the user has camera on the device
                                PackageManager packageManager = getActivity().getPackageManager();
                                if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == false) {

                                    Toast.makeText(getActivity(), R.string.CAMERA_NO, Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    //Ask for permissions
                                    if (requestPermissions()) {
                                        //take photo
                                        dispatchTakePictureIntent();

                                    }

                                }
                            }
                        });
                builder.show();

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Check if permissions needed to take a photo are given or not and ask for them
    private boolean requestPermissions() {

        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(getActivity(), permissions[1]) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(getActivity(), permissions[2]) == PackageManager.PERMISSION_DENIED) {

            requestPermissions(permissions, REQUEST_CODE);

            return false;

        }

        return true;
    } //request permissions


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {

            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
            byte[] img = baos.toByteArray();
            profileViewModel.storeProfilePicture(img).observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {

                    if (s.equals(getString(R.string.SUCCESS))) {
                        profilePic.setImageBitmap(imageBitmap);
                        profilePic.setBackgroundResource(R.color.transparent);

                    } else if (s.equals(getString(R.string.ERROR))) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.VALUE_NOT_SAVED_MSG)
                                .setTitle(R.string.ERROR_MSG)
                                .setPositiveButton(android.R.string.ok, null);
                        builder.show();

                    }
                }
            });
        }
    }


}