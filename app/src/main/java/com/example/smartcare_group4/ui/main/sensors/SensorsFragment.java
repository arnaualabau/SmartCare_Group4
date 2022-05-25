package com.example.smartcare_group4.ui.main.sensors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcare_group4.databinding.FragmentSensorsBinding;
import com.example.smartcare_group4.ui.main.sensors.SensorsViewModel;


public class SensorsFragment extends Fragment {

    private FragmentSensorsBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SensorsViewModel sensorsViewModel =
                new ViewModelProvider(this).get(SensorsViewModel.class);

        binding = FragmentSensorsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView title = binding.textSensors;
        sensorsViewModel.getText().observe(getViewLifecycleOwner(), title::setText);

        TextView lightValue = binding.textLightValue;
        sensorsViewModel.setLight().observe(getViewLifecycleOwner(), lightValue::setText);


        /*
        final TextView lightValue = binding.textLightValue;
        sensorsViewModel.getText().observe(getViewLifecycleOwner(), lightValue::setText);

        final TextView tapValue = binding.textTapValue;
        sensorsViewModel.getText().observe(getViewLifecycleOwner(), tapValue::setText);

        final TextView presenceValue = binding.textPresenceValue;
        sensorsViewModel.getText().observe(getViewLifecycleOwner(), presenceValue::setText);
        */
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}