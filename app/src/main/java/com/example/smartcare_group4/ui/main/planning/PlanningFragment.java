package com.example.smartcare_group4.ui.main.planning;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.data.Event;
import com.example.smartcare_group4.data.EventDAO;
import com.example.smartcare_group4.databinding.FragmentPlanningBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class PlanningFragment extends Fragment implements CalendarAdapter.OnItemListener, AdapterView.OnItemSelectedListener {

    private FragmentPlanningBinding binding;

    private Spinner spinner;

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private LinearLayout addMedLayout;
    private Button addMedButton;
    private Button delMedButton;
    private Button takeMedButton;
    private String medSelected;

    private Button nextWeek;
    private Button lastWeek;

    private String medicinePlanned;

    String currentPhotoPath;
    private static final int REQUEST_CODE = 14;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EventAdapter eventAdapter;
    private CalendarAdapter calendarAdapter;

    private PlanningViewModel planningViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        planningViewModel =
                new ViewModelProvider(this).get(PlanningViewModel.class);

        CalendarUtils.selectedDate = CalendarUtils.now;

        planningViewModel.subscribePlanning().observe(getViewLifecycleOwner(), new Observer<ArrayList<EventDAO>>() {
            @Override
            public void onChanged(ArrayList<EventDAO> planning) {

                planningViewModel.setPlanning(planning);
                setWeekView();

                if (!planningViewModel.isPatient() && planningViewModel.noWeekPlan()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.NO_WEEKPLAN_MSG)
                            .setTitle(R.string.ALERT_MSG)
                            .setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }
            }
        });

        binding = FragmentPlanningBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
                if (CalendarUtils.selectedDate.isBefore(CalendarUtils.now)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.NOT_PAST_MSG)
                            .setTitle(R.string.ERROR_MSG)
                            .setPositiveButton(android.R.string.ok, null);
                    builder.show();

                } else {
                    //save event to firebase
                    planningViewModel.saveEvent(medSelected, CalendarUtils.selectedDate).observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (s.equals(getString(R.string.SUCCESS))) {
                                Toast.makeText(getActivity(), medSelected, Toast.LENGTH_SHORT).show();
                            } else {
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
        });

        takeMedButton = v.findViewById(R.id.takeMedButton);
        takeMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CalendarUtils.selectedDate.equals(CalendarUtils.now)) {
                    ArrayList<Event> events = planningViewModel.eventsForDate(LocalDate.now());

                    if (events.size() > 0) {

                        if (events.get(0).getTaken().equals("yes")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(R.string.MED_ALREADY_TAKEN_MSG)
                                    .setTitle(R.string.ALERT_MSG)
                                    .setPositiveButton(android.R.string.ok, null);
                            builder.show();

                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(R.string.CAMERA_MSG)
                                    .setTitle(R.string.CAMERA_TITLE)
                                    .setIcon(android.R.drawable.ic_menu_camera)
                                    .setCancelable(true)
                                    .setNegativeButton(android.R.string.no, null)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //make sure the user has camera on the device
                                            PackageManager packageManager = getActivity().getPackageManager();
                                            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == false) {

                                                Toast.makeText(getActivity(), R.string.CAMERA_NO, Toast.LENGTH_SHORT)
                                                        .show();
                                            } else {
                                                //Ask for permissions
                                                if (requestPermissions()) {
                                                    //take photo && process image
                                                    medicinePlanned = events.get(0).getName();
                                                    dispatchTakePictureIntent();

                                                }

                                            }
                                        }
                                    });
                            builder.show();
                        }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.NOT_MEDS_MSG)
                                .setTitle(R.string.ALERT_MSG)
                                .setPositiveButton(android.R.string.ok, null);
                        builder.show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.NOT_TODAY_MSG)
                            .setTitle(R.string.ALERT_MSG)
                            .setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }
            }
        });

        delMedButton = v.findViewById(R.id.delMedButton);
        delMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CalendarUtils.selectedDate.isBefore(CalendarUtils.now)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.NOT_PAST_MSG)
                            .setTitle(R.string.ERROR_MSG)
                            .setPositiveButton(android.R.string.ok, null);
                    builder.show();

                } else {
                    ArrayList<Event> events = planningViewModel.eventsForDate(LocalDate.now());

                    if (events.size() > 0) {
                        //delete medicine in firebase
                        planningViewModel.deleteEvent(CalendarUtils.selectedDate).observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if (s.equals(getString(R.string.SUCCESS))) {
                                    Toast.makeText(getActivity(), R.string.delete_med_msg, Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage(R.string.VALUE_NOT_SAVED_MSG)
                                            .setTitle(R.string.ERROR_MSG)
                                            .setPositiveButton(android.R.string.ok, null);
                                    builder.show();
                                }
                            }
                        });
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.NOT_MEDS_MSG)
                                .setTitle(R.string.ERROR_MSG)
                                .setPositiveButton(android.R.string.ok, null);
                        builder.show();
                    }
                }
            }
        });

        //CREATE SPINNER WITH MEDICINES AVAILABLE
        spinner = (Spinner) v.findViewById(R.id.medSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.meds_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Specify Listener
        spinner.setOnItemSelectedListener(this);

        //CALENDAR VIEW
        calendarRecyclerView = v.findViewById(R.id.calendarRecyclerView);
        monthYearText = v.findViewById(R.id.monthYearTV);
        eventListView = v.findViewById(R.id.eventListView);

        //go back in the calendar
        lastWeek = v.findViewById(R.id.lastWeek);
        lastWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
                setWeekView();
            }
        });
        //go forward in the calendar
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

        //view depends on the user's role

        if (planningViewModel.isPatient()) {
            takeMedButton.setVisibility(View.VISIBLE);
            addMedLayout.setVisibility(View.GONE);
            delMedButton.setVisibility(View.GONE);

        } else {
            takeMedButton.setVisibility(View.GONE);
            addMedLayout.setVisibility(View.VISIBLE);
            delMedButton.setVisibility(View.VISIBLE);

        }

        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate);

        calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdpater();
    }

    private void setEventAdpater() {

        ArrayList<Event> dailyEvents = planningViewModel.eventsForDate(CalendarUtils.selectedDate);
        eventAdapter = new EventAdapter(getActivity(), dailyEvents);
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

    //Camera & Permissions
    //Check if permissions needed to take a photo are given or not and ask for them
    private boolean requestPermissions() {

        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(getActivity(), permissions[1]) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(getActivity(), permissions[2]) == PackageManager.PERMISSION_DENIED) {

            requestPermissions(permissions, REQUEST_CODE);

            return false;
        }

        return true;
    } //request permissions


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {

                    new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                            .setTitle(R.string.PERMISSIONS_TITLE)
                            .setMessage(R.string.PERMISSIONS_DENIED)
                            .setNegativeButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                return;
        }
    }


    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast toast = Toast.makeText(getActivity(), "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),"com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {

            int rotation = 0;

            try {
                ExifInterface exif = new ExifInterface(currentPhotoPath);
                exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);

            planningViewModel.processMedPicture(imageBitmap, rotation).observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {

                    if (!s.equals(getString(R.string.ERROR))) {

                        checkMedication(s);

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.MED_NOT_DETECTED_MSG)
                                .setTitle(R.string.ERROR_MSG)
                                .setPositiveButton(android.R.string.ok, null);
                        builder.show();
                    }


                }
            });
        }
    }

    private void checkMedication(String medicineDetected) {

        planningViewModel.takeMedicine(CalendarUtils.selectedDate, medicineDetected, medicinePlanned).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals(getString(R.string.SUCCESS))) {

                    Toast.makeText(getActivity(), R.string.take_med_msg, Toast.LENGTH_SHORT).show();

                    //CalendarUtils.now = CalendarUtils.now.plusDays(1);
                    //CalendarUtils.selectedDate = CalendarUtils.now;

                    setWeekView();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.WRONG_MED_MSG)
                            .setTitle(R.string.ALERT_MSG)
                            .setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }
            }
        });
    }
}