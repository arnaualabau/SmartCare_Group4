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

        final TextView textView = binding.textSensors;
        sensorsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}