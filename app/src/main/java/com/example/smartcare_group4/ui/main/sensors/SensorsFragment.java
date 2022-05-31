package com.example.smartcare_group4.ui.main.sensors;

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

import com.example.smartcare_group4.data.Device;
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

        //declaration
        final TextView title = binding.textSensors;
        final TextView lightValue = binding.textLightValue;
        final TextView presenceValue = binding.textPresenceValue;
        final TextView tapValue = binding.textTapValue;

        sensorsViewModel.getDeviceInfo().observe(getViewLifecycleOwner(), new Observer<Device>() {
            @Override
            public void onChanged(Device device) {
                if (!device.getHardwareId().equals("error")) {
                    sensorsViewModel.setLight(device.getLightSensor());
                    sensorsViewModel.setTap(device.getTap());
                    sensorsViewModel.setPresence(device.getPresenceSensor());
                } else {
                    Log.d("SENSORS", "error");
                }
            }
        });

        sensorsViewModel.getText(0).observe(getViewLifecycleOwner(), title::setText);
        sensorsViewModel.getText(1).observe(getViewLifecycleOwner(), lightValue::setText);
        sensorsViewModel.getText(2).observe(getViewLifecycleOwner(), tapValue::setText);
        sensorsViewModel.getText(3).observe(getViewLifecycleOwner(), presenceValue::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}