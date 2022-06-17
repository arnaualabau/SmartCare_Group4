package com.example.smartcare_group4.ui.main.settings;

import android.app.AlertDialog;
import android.os.Bundle;
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
import com.example.smartcare_group4.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private EditText oldPassword;
    private EditText newPassword;
    private EditText newPassword2;

    private String oldPSWtext;
    private String newPSWtext;
    private String newPSW2text;

    private Button changePSWButton;

    private SettingsViewModel settingsViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bindViews(root);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void bindViews(View v) {
        changePSWButton = v.findViewById(R.id.buttonChangePassword);
        oldPassword = v.findViewById(R.id.oldPasswordSettings);
        newPassword = v.findViewById(R.id.newPasswordSettings);
        newPassword2 = v.findViewById(R.id.newPasswordSettings2);


        changePSWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //obtain text from fields
                oldPSWtext = oldPassword.getText().toString();
                newPSWtext = newPassword.getText().toString();
                newPSW2text = newPassword2.getText().toString();

                //check if fields are empty or not
                if (! (settingsViewModel.emptyTexts(oldPSWtext) || settingsViewModel.emptyTexts(newPSWtext) || settingsViewModel.emptyTexts(newPSW2text)) ) {
                    //check old password
                    settingsViewModel.checkOldPassword(oldPSWtext).observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s1) {
                            if (s1.equals(getString(R.string.SUCCESS))) {
                                //check new passwords
                                if (settingsViewModel.checkNewPasswords(newPSWtext, newPSW2text)) {
                                    //change password
                                    settingsViewModel.changePassword(newPSWtext).observe(getViewLifecycleOwner(), new Observer<String>() {
                                        @Override
                                        public void onChanged(String s2) {
                                            if (s2.equals(getString(R.string.ERROR))) {
                                                //error
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setMessage(R.string.PSW_NOTCHANGED)
                                                        .setTitle(R.string.ERROR)
                                                        .setPositiveButton(android.R.string.ok, null);
                                                builder.show();
                                                oldPassword.setText("");
                                                newPassword.setText("");
                                                newPassword2.setText("");
                                            } else if (s2.equals(getString(R.string.SUCCESS))) {
                                                //success
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setMessage(R.string.PSW_CHANGED)
                                                        .setTitle(R.string.SUCCESS)
                                                        .setPositiveButton(android.R.string.ok, null);
                                                builder.show();
                                                oldPassword.setText("");
                                                newPassword.setText("");
                                                newPassword2.setText("");
                                            }
                                        }
                                    });
                                } else {
                                    //error message: new passwords do not match or less 6 chars
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage(R.string.ERROR_PASSWORDS)
                                            .setTitle(R.string.ERROR_MSG)
                                            .setPositiveButton(android.R.string.ok, null);
                                    builder.show();
                                }
                            } else if (s1.equals(getString(R.string.ERROR))) {
                                //error message: old password does not match
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(R.string.ERROR_OLDPSW)
                                        .setTitle(R.string.ERROR_MSG)
                                        .setPositiveButton(android.R.string.ok, null);
                                builder.show();
                            }
                        }
                    });
                }
            }
        });

    }
}