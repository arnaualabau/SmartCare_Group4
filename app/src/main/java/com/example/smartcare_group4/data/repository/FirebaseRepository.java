package com.example.smartcare_group4.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcare_group4.data.Device;
import com.example.smartcare_group4.data.EventDAO;
import com.example.smartcare_group4.data.User;
import com.example.smartcare_group4.ui.main.planning.CalendarUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.util.ArrayList;

public class FirebaseRepository {

    //constants
    final int LIGHT_SOS = 255;
    final int TAP_SOS = 0;

    //Create Singleton
    public static FirebaseRepository firebaseInstance = new FirebaseRepository();

    //Access to Firebase Database
    private DatabaseReference mDatabase;
    //Access to Firebase Storage
    private FirebaseStorage firabaseStorage = FirebaseStorage.getInstance();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private String idUser;
    private String idHardware;
    private User userInfo;
    private Device device;
    private ArrayList<EventDAO> planning;

    public FirebaseRepository(){
        // Initialize Firebase Auth
        mDatabase = FirebaseDatabase.getInstance().getReference();
        idUser = "";
    }

    //Sign Up method in Authentication Firebase
    public LiveData<String> signUpFirebase(String email, String password) {

        MutableLiveData<String> observable = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            user = mAuth.getCurrentUser();
                            idUser = user.getUid();
                            //idHardware = "123456";
                            observable.setValue(user.getUid());
                        } else {
                            // If sign up fails, display a message to the user.
                            observable.setValue("error");
                        }
                    }
                });
        return observable;
    } //signUp Firebase

    //Log In method in Authentication Firebase
    public LiveData<String> loginFirebase(String email, String password) {

        MutableLiveData<String> observable = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            user = mAuth.getCurrentUser();
                            idUser = user.getUid();
                            observable.setValue(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            observable.setValue("error");
                        }
                    }
                });
        return observable;
    } //loginFirebase

    //Sign Out method in Authentication Firebase
    public String signOut() {

        String result = "";
        try {
            FirebaseAuth.getInstance().signOut();
            result = "success";
        } catch (Exception e) {
            e.printStackTrace();
            result = "error";
        }
        return result;
    } //Sign Out

    //Re-Authenticate user with credentials
    public MutableLiveData<String> checkCredentials(String oldPass) {

        MutableLiveData<String> observable = new MutableLiveData<>();
        AuthCredential credential = EmailAuthProvider
                .getCredential(userInfo.getEmail(), oldPass);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            observable.setValue("success");
                        } else {
                            observable.setValue("error");
                        }
                    }
                });
        return observable;
    } //check credentials

    //Change Password in Firebase Authentication
    public MutableLiveData<String> changePSW(String newPass) {

        MutableLiveData<String> observable = new MutableLiveData<>();
        user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    observable.setValue("success");

                } else {
                    observable.setValue("error");

                }
            }
        });

        return observable;
    } //Change password


    public void deleteUser(String email, String password) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        // Prompt the user to re-provide their sign-in credentials
        if (user != null) {
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            /*
                                            if (task.isSuccessful()) {

                                                startActivity(new Intent(DeleteUser.this, StartActivity.class));
                                                Toast.makeText(DeleteUser.this, "Deleted User Successfully,", Toast.LENGTH_LONG).show();
                                            }
                                            */
                                        }
                                    });
                        }
                    });
        }
    }

    //Subscribe to Values of Sensors in Firebase Database
    public LiveData<Device> subscribeToValues() {

        MutableLiveData<Device> observable = new MutableLiveData<>();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Device device = new Device();
                device = dataSnapshot.getValue(Device.class);
                observable.setValue(device);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        //constantly checking the user's sensors values
        mDatabase.child("devices").child(idHardware).addValueEventListener(postListener);

        return observable;

    } //subscribe to values

    //Register User info and Device Info in Firebase Database
    public LiveData<String> registerUser(String name, String email, String id, String hardwareId, boolean patient, boolean imgTaken){

        MutableLiveData<String> observable = new MutableLiveData<>();

        //firebase method to register user with callback
        userInfo = new User(name, email, hardwareId, patient, imgTaken);
        mDatabase.child("users").child(id).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Log.d("HWID", "onComplete: "+ hardwareId);

                    //before registering the device, make sure it is not already registered
                    if (!isHadwareID(hardwareId)) {

                        device = new Device(hardwareId);

                        mDatabase.child("devices").child(hardwareId).setValue(device).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    observable.setValue("success register");
                                    idHardware = hardwareId;
                                } else {
                                    observable.setValue("error register");
                                }
                            }
                        });
                    } else {
                        observable.setValue("success register");
                    }

                } else {
                    observable.setValue("error register");
                }

            }
        });

        return observable;
    } //Register User

    //Find if the Hardware is already registered
    private boolean isHadwareID(String hardwareId) {
        final boolean[] found = {false};
        mDatabase.child("devices")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Device dev = snapshot.getValue(Device.class);
                            assert dev != null;

                            if (dev.getHardwareId().equals(hardwareId)) {
                                found[0] = true;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        return found[0];
    } //isHardwareID

    //Get User information from Firebase Database
    public LiveData<User> getUserInfo() {

        MutableLiveData<User> observable = new MutableLiveData<>();

        mDatabase.child("users").child(idUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    userInfo = new User();
                    userInfo.setEmail("error");
                    observable.setValue(userInfo);
                }
                else {
                    userInfo = (User) task.getResult().getValue(User.class);
                    observable.setValue(userInfo);
                }
            }
        });

        return observable;
    } //get user info

    //Get Device Info from Firebase Database
    public LiveData<Device> getDeviceInfo() {

        MutableLiveData<Device> observable = new MutableLiveData<>();
        mDatabase.child("devices").child(idHardware).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    device = new Device();
                    device.setHardwareId("error");
                    observable.setValue(device);
                }
                else {
                    device = (Device) task.getResult().getValue(Device.class);
                    observable.setValue(device);
                }
            }
        });

        return observable;
    } //get device info

    //Subscribe to Values of Planning in Firebase Database
    public LiveData<ArrayList<EventDAO>> subscribeToPlanning() {

        MutableLiveData<ArrayList<EventDAO>> observable = new MutableLiveData<>();

        mDatabase.child("planning").child(idHardware).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                planning = new ArrayList<EventDAO>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    planning.add(dataSnapshot.getValue(EventDAO.class));
                }
                if (planning.size()<1) {
                    planning.add(new EventDAO("empty", "empty"));
                }

                observable.setValue(planning);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return observable;
    }

    public void setHWId(String hardwareId) {
        idHardware = hardwareId;
    }

    //Change Light Value in Firebase Database
    public MutableLiveData<String> changeLightValue(int value) {
        MutableLiveData<String> observable = new MutableLiveData<>();

        mDatabase.child("devices").child(idHardware).child("lightSensor").setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                   observable.setValue("success");
                } else {
                   observable.setValue("error");
                }
            }
        });
        return observable;
    } //change light value

    //Change Tap Value in Firebase Database
    public MutableLiveData<String> changeTapValue(int value) {
        MutableLiveData<String> observable = new MutableLiveData<>();

        mDatabase.child("devices").child(idHardware).child("tap").setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    observable.setValue("success");
                } else {
                    observable.setValue("error");
                }
            }
        });
        return observable;
    } //change tap value

    //Set values to indicate there is an emergency in Firebase
    public MutableLiveData<String> setValuesEmergency() {
        MutableLiveData<String> observable = new MutableLiveData<>();

        mDatabase.child("devices").child(idHardware).child("tap").setValue(TAP_SOS).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mDatabase.child("devices").child(idHardware).child("lightSensor").setValue(LIGHT_SOS).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                observable.setValue("success");

                            } else {
                                observable.setValue("error");
                            }
                        }
                    });

                } else {
                    observable.setValue("error");
                }
            }
        });
        return observable;
    } //set values emergency

    public boolean imageTaken() {

        return userInfo.isImageTaken();
    }

    public boolean isPatient() {

        return userInfo.isPatient();
    }

    //Save Event of Planning to Firebase
    public MutableLiveData<String> saveEvent(String medSelected, LocalDate selectedDate) {

        MutableLiveData<String> observable = new MutableLiveData<>();

        String formattedDate = CalendarUtils.formattedDate(selectedDate);
        EventDAO eventDAO = new EventDAO(medSelected, formattedDate);

        mDatabase.child("planning").child(idHardware).child(formattedDate).setValue(eventDAO).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    observable.setValue("success");
                } else {
                    observable.setValue("error");
                }

            }
        });

        return observable;
    } //save event

    //Delete Event of Planning from Firebase
    public MutableLiveData<String> deleteEvent(LocalDate selectedDate) {

        MutableLiveData<String> observable = new MutableLiveData<>();

        String formattedDate = CalendarUtils.formattedDate(selectedDate);

        mDatabase.child("planning").child(idHardware).child(formattedDate).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    observable.setValue("success");
                } else {
                    observable.setValue("error");
                }

            }
        });

        return observable;
    } //delete event

    //Change medicine status on firebase
    public MutableLiveData<String> takeMedicine(LocalDate selectedDate) {

        MutableLiveData<String> observable = new MutableLiveData<>();

        String formattedDate = CalendarUtils.formattedDate(selectedDate);

        mDatabase.child("planning").child(idHardware).child(formattedDate).child("taken").setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    observable.setValue("success");
                } else {
                    observable.setValue("error");
                }

            }
        });

        return observable;

    } //take medicine

    //Save image in Firebase Storage
    public LiveData<String> storeProfilePicture(byte[] img) {

        MutableLiveData<String> observable = new MutableLiveData<>();

        StorageReference storageRef = firabaseStorage.getReference();
        StorageReference profilePicRef = storageRef.child("profile/"+user.getUid()+".jpg");



        UploadTask uploadTask = profilePicRef.putBytes(img);
        uploadTask.addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {

                observable.setValue("error");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mDatabase.child(user.getUid()).child("imageTaken").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        observable.setValue("success");
                    }
                });

            }
        });

        return observable;
    } //storage profile picture

    //Get image from Firebase Storage
    public LiveData<byte[]> getProfilePicture() {

        MutableLiveData<byte[]> observable = new MutableLiveData<>();

        StorageReference storageRef = firabaseStorage.getReference();
        StorageReference profilePicRef = storageRef.child("profile/"+user.getUid()+".jpg");

        final long ONE_MEGABYTE = 1024*1024;
        profilePicRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                observable.setValue(bytes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        return observable;

    } // get profile picture

}
