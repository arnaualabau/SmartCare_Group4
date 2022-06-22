package com.example.smartcare_group4.ui.main.sensors;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.data.Device;
import com.example.smartcare_group4.databinding.FragmentSensorsBinding;
import com.google.android.material.slider.Slider;


public class SensorsFragment extends Fragment {

    private FragmentSensorsBinding binding;
    private Button addTapButton;
    private Button removeTapButton;
    private Slider lightSlider;

    private SensorsViewModel sensorsViewModel;

    TextView lightValue;
    TextView tapValue;
    TextView presenceValue;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sensorsViewModel =
                new ViewModelProvider(this).get(SensorsViewModel.class);
        sensorsViewModel.subscribeValues().observe(getViewLifecycleOwner(), new Observer<Device>() {
            @Override
            public void onChanged(Device device) {
                sensorsViewModel.setLight(device.getLightSensor());
                sensorsViewModel.setTap(device.getTap());
                sensorsViewModel.setPresence(device.getPresenceSensor());
                sensorsViewModel.setTemperature(device.getTemperature());
                sensorsViewModel.setHumidity(device.getHumidity());
                lightSlider.setValue((float) device.getLightSensor());
            }
        });

        binding = FragmentSensorsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bindViews(root);

        //declaration
        lightValue = binding.textLightValue;
        presenceValue = binding.textPresenceValue;
        tapValue = binding.textTapValue;

        sensorsViewModel.getDeviceInfo().observe(getViewLifecycleOwner(), new Observer<Device>() {
            @Override
            public void onChanged(Device device) {
                if (!device.getHardwareId().equals(getString(R.string.ERROR))) {
                    sensorsViewModel.setLight(device.getLightSensor());
                    sensorsViewModel.setTap(device.getTap());
                    sensorsViewModel.setPresence(device.getPresenceSensor());
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.ERROR_SENSORS)
                            .setTitle(R.string.ERROR_MSG)
                            .setPositiveButton(android.R.string.ok, null);
                    builder.show();

                }
            }
        });

        //update UI
        sensorsViewModel.getText(1).observe(getViewLifecycleOwner(), lightValue::setText);
        sensorsViewModel.getText(2).observe(getViewLifecycleOwner(), tapValue::setText);
        sensorsViewModel.getText(3).observe(getViewLifecycleOwner(), presenceValue::setText);

        return root;
    }

    private void bindViews(View v) {

        lightSlider = v.findViewById(R.id.lightSlider);
        lightSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {

                int value = (int) slider.getValue();

                sensorsViewModel.changeLightValue((int)value).observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.equals(getString(R.string.SUCCESS))) {

                            //values will be automatically changed
                        } else {
                            //error
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(R.string.VALUE_NOT_SAVED_MSG)
                                    .setTitle(R.string.ERROR_MSG)
                                    .setPositiveButton(android.R.string.ok, null);
                            builder.show();
                        }
                    }
                });

            }
        });

        addTapButton = v.findViewById(R.id.addBtn);
        addTapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sensorsViewModel.isPatient()) {
                    int step = Integer.parseInt(tapValue.getText().toString());
                    if (sensorsViewModel.checkStep(step, 0, 4)) {
                        //increase value
                        step = step + 1;

                        sensorsViewModel.changeTapValue(step).observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if (s.equals(getString(R.string.SUCCESS))) {
                                    //values will be automatically changed
                                } else {
                                    //error
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
        });


        removeTapButton = v.findViewById(R.id.removeBtn);
        removeTapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sensorsViewModel.isPatient()) {
                    int step = Integer.parseInt(tapValue.getText().toString());

                    if (sensorsViewModel.checkStep(step, 1, 5)) {
                        //decrease value
                        step = step - 1;

                        sensorsViewModel.changeTapValue(step).observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if (s.equals(getString(R.string.SUCCESS))) {
                                    //values will be automatically changed
                                } else {
                                    //error
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
        });
        //control which buttons should be shown
        if (sensorsViewModel.isPatient()) {
            addTapButton.setVisibility(View.GONE);
            removeTapButton.setVisibility(View.GONE);
            lightSlider.setVisibility(View.GONE);

        } else {
            addTapButton.setVisibility(View.VISIBLE);
            removeTapButton.setVisibility(View.VISIBLE);
            lightSlider.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}