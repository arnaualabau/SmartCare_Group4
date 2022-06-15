package com.example.smartcare_group4.ui.main.profile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.data.User;
import com.example.smartcare_group4.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
                            .setTitle(R.string.ERROR_MSG);
                    builder.show();
                }
            }
        });

        //update UI
        profileViewModel.getText(1).observe(getViewLifecycleOwner(), nameValue::setText);
        profileViewModel.getText(2).observe(getViewLifecycleOwner(), emailValue::setText);
        profileViewModel.getText(3).observe(getViewLifecycleOwner(), HWValue::setText);
        profileViewModel.getText(4).observe(getViewLifecycleOwner(), roleValue::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}