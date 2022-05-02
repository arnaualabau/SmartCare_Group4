/*
package com.example.smartcare_group4.view;

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
import com.example.smartcare_group4.utils.PreferenceUtil;
import com.example.smartcare_group4.viewmodel.MainViewModel;
*/

/*
public class MainFragment extends Fragment {

    private TextView textView;
    private Button button1, button2;

    private MainViewModel viewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        bindViews(v);
        initViewModel();
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void bindViews(View v) {

        textView = v.findViewById(R.id.textview);
        textView.setText("-----");

        button1 = v.findViewById(R.id.button1);
        button1.setText("LOGIN");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.checkInputsAndLogin(PreferenceUtil.getInstance(requireContext()), "NAME", "EMAIL", "PASSWORD", "PASSWORD").observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        //TODO process errors or advance
                    }
                });
            }
        });

        button2 = v.findViewById(R.id.button2);
        button2.setText("STOP");
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.initRepositories(requireContext());

    }
}
*/