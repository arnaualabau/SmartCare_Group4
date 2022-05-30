package com.example.smartcare_group4.ui.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
        final TextView textView = binding.textProfile;
        final TextView nameValue = binding.textNameValue;
        final TextView emailValue = binding.textEmailValue;
        final TextView HWValue = binding.textHwValue;
        final TextView roleValue = binding.textRoleValue;

        //ask firebase for data
        profileViewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (!user.getEmail().equals("error")) {
                    profileViewModel.setNameValue(user.getUsername());
                    profileViewModel.setEmailValue(user.getEmail());
                    profileViewModel.setWDIdValue(user.getHardwareId());
                    profileViewModel.setRoleValue(user.isPatient());


                }
            }
        });

        profileViewModel.getText(0).observe(getViewLifecycleOwner(), textView::setText);
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