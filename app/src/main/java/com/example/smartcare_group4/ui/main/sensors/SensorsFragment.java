package com.example.smartcare_group4.ui.main.sensors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.data.Device;
import com.example.smartcare_group4.data.constants.Generic;
import com.example.smartcare_group4.databinding.FragmentSensorsBinding;
import com.example.smartcare_group4.ui.main.sensors.SensorsViewModel;


public class SensorsFragment extends Fragment {

    private FragmentSensorsBinding binding;
    private Button lightButton;
    private SensorsViewModel sensorsViewModel;

    TextView lightValue;


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
            }
        });

        binding = FragmentSensorsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bindViews(root);

        //declaration
        final TextView title = binding.textSensors;
        lightValue = new TextView(getActivity());
        lightValue = binding.textLightValue;
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

    private void bindViews(View v) {
        lightButton = v.findViewById(R.id.buttonLight);
        lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sensorsViewModel.isPatient()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Insert value between 0 - 255");
                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setRawInputType(Configuration.KEYBOARD_12KEY);
                    alert.setView(input);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Put actions for OK button here
                            int value = Integer.parseInt(input.getText().toString());
                            if (sensorsViewModel.checkValue(value)){

                                //canviar valor del sensor
                                sensorsViewModel.changeLightValue(value).observe(getViewLifecycleOwner(), new Observer<String>() {
                                    @Override
                                    public void onChanged(String s) {
                                        if (s.equals("success")) {
                                            //canviar text
                                            //sensorsViewModel.setLight(value);
                                        } else {
                                            //controlar error
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setMessage("Could not save value.")
                                                    .setTitle(Generic.ERROR);
                                            builder.show();
                                        }
                                    }
                                });

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(R.string.action_settings)
                                        .setTitle(Generic.ERROR);
                                builder.show();
                            }
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Put actions for CANCEL button here, or leave in blank
                        }
                    });
                    alert.show();
                    
                } else {
                    //change message to you cannot modify
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(Generic.ERROR_OLDPSW)
                            .setTitle(Generic.ERROR);
                    builder.show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}