package com.example.smartcare_group4.ui.main.planning;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.data.Event;
import com.example.smartcare_group4.data.EventDAO;
import com.example.smartcare_group4.data.constants.Generic;
import com.example.smartcare_group4.databinding.FragmentPlanningBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PlanningFragment extends Fragment implements CalendarAdapter.OnItemListener, AdapterView.OnItemSelectedListener {

    private FragmentPlanningBinding binding;

    private Spinner spinner;

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private LinearLayout addMedLayout;
    private Button addMedButton;
    private Button takeMedButton;
    private String medSelected;

    private Button nextWeek;
    private Button lastWeek;

    private PlanningViewModel planningViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        planningViewModel =
                new ViewModelProvider(this).get(PlanningViewModel.class);

        planningViewModel.subscribePlanning().observe(getViewLifecycleOwner(), new Observer<EventDAO>() {
            @Override
            public void onChanged(EventDAO eventDAO) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
                LocalDate date = LocalDate.parse(eventDAO.getDate(), formatter);

                Event newMed = new Event(eventDAO.getName(),date);
                Log.d("PLANNING", newMed.getName() +" "+ newMed.getDate().toString());

            }
        });

        binding = FragmentPlanningBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CalendarUtils.selectedDate = LocalDate.now();

        bindViews(root);
        setWeekView();

        return root;
    }

    private void bindViews(View v) {

        addMedLayout = v.findViewById(R.id.addMedLayout);
        addMedButton = v.findViewById(R.id.addMedButton);
        addMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                planningViewModel.saveEvent(medSelected, CalendarUtils.selectedDate).observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.equals("success")) {
                            Toast.makeText(getActivity(), medSelected, Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("value not saved")
                                    .setTitle(Generic.ERROR);
                            builder.show();
                        }
                    }
                });
            }
        });

        takeMedButton = v.findViewById(R.id.takeMedButton);

        spinner = (Spinner) v.findViewById(R.id.medSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.meds_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Specify Listener
        spinner.setOnItemSelectedListener(this);

        calendarRecyclerView = v.findViewById(R.id.calendarRecyclerView);
        monthYearText = v.findViewById(R.id.monthYearTV);
        eventListView = v.findViewById(R.id.eventListView);

        lastWeek = v.findViewById(R.id.lastWeek);
        lastWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
                setWeekView();
            }
        });

        nextWeek = v.findViewById(R.id.nextWeek);
        nextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
                setWeekView();
            }
        });
    }

    private void setWeekView() {

        if (planningViewModel.isPatient()) {
            takeMedButton.setVisibility(View.VISIBLE);
            addMedLayout.setVisibility(View.GONE);
        } else {
            takeMedButton.setVisibility(View.GONE);
            addMedLayout.setVisibility(View.VISIBLE);
        }

        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdpater();
    }

    private void setEventAdpater() {

        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getActivity(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        medSelected = adapterView.getItemAtPosition(i).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    // Calendar
    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }
}