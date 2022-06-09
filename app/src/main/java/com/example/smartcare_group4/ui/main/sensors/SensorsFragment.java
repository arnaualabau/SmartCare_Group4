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
import com.example.smartcare_group4.databinding.FragmentSensorsBinding;


public class SensorsFragment extends Fragment {

    private FragmentSensorsBinding binding;
    private Button lightButton;
    private Button tapButton;
    private Button addTapButton;
    private Button removeTapButton;

    private SensorsViewModel sensorsViewModel;

    TextView lightValue;
    TextView tapValue;


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
        lightValue = binding.textLightValue;
        final TextView presenceValue = binding.textPresenceValue;
        tapValue = binding.textTapValue;

        sensorsViewModel.getDeviceInfo().observe(getViewLifecycleOwner(), new Observer<Device>() {
            @Override
            public void onChanged(Device device) {
                if (!device.getHardwareId().equals(R.string.ERROR)) {
                    sensorsViewModel.setLight(device.getLightSensor());
                    sensorsViewModel.setTap(device.getTap());
                    sensorsViewModel.setPresence(device.getPresenceSensor());
                } else {
                    Log.d("SENSORS", "error");
                }
            }
        });


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
                    alert.setTitle(R.string.LIGHT_SENSOR_VALUES);
                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setRawInputType(Configuration.KEYBOARD_12KEY);
                    alert.setView(input);
                    alert.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Put actions for OK button here
                            int value = Integer.parseInt(input.getText().toString());
                            if (sensorsViewModel.checkValue(value, 255)){

                                //canviar valor del sensor
                                sensorsViewModel.changeLightValue(value).observe(getViewLifecycleOwner(), new Observer<String>() {
                                    @Override
                                    public void onChanged(String s) {
                                        if (s.equals(R.string.SUCCESS)) {
                                            //canviar text
                                            //sensorsViewModel.setLight(value);
                                        } else {
                                            //controlar error
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setMessage(R.string.VALUE_NOT_SAVED_MSG)
                                                    .setTitle(R.string.ERROR_MSG);
                                            builder.show();
                                        }
                                    }
                                });

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(R.string.action_settings)
                                        .setTitle(R.string.ERROR_MSG);
                                builder.show();
                            }
                        }
                    });
                    alert.setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Put actions for CANCEL button here, or leave in blank
                        }
                    });
                    alert.show();
                    
                } else {
                    //change message to you cannot modify
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.VALUE_NOT_SAVED_MSG)
                            .setTitle(R.string.ERROR_MSG);
                    builder.show();
                }
            }
        });

        addTapButton = v.findViewById(R.id.addBtn);
        addTapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sensorsViewModel.isPatient()) {
                    int step = Integer.parseInt(tapValue.getText().toString());
                    Log.d("sensors", Integer.toString(step));
                    if (sensorsViewModel.checkStep(step, 0, 4)) {
                        step = step + 1;

                        sensorsViewModel.changeTapValue(step).observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if (s.equals(R.string.SUCCESS)) {
                                    //canviar text
                                } else {
                                    //controlar error
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage(R.string.VALUE_NOT_SAVED_MSG)
                                            .setTitle(R.string.ERROR_MSG);
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
                    Log.d("sensors", Integer.toString(step));

                    if (sensorsViewModel.checkStep(step, 1, 5)) {
                        step = step - 1;

                        sensorsViewModel.changeTapValue(step).observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if (s.equals(R.string.SUCCESS)) {
                                    //canviar text
                                } else {
                                    //controlar error
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage(R.string.VALUE_NOT_SAVED_MSG)
                                            .setTitle(R.string.ERROR_MSG);
                                    builder.show();
                                }
                            }
                        });
                    }
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