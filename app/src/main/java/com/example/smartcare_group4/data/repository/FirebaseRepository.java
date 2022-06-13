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

    public static FirebaseRepository firebaseInstance = new FirebaseRepository();
    private DatabaseReference mDatabase;

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

    public LiveData<String> signUpFirebase(String email, String password) {

        MutableLiveData<String> observable = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SIGNUP", "signUpWithEmail:success");
                            user = mAuth.getCurrentUser();
                            Log.d("SIGNUP", user.getUid());
                            idUser = user.getUid();
                            idHardware = "123456";
                            observable.setValue(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("SIGNUP", "signUpWithEmail:failure"+ task.getException().getLocalizedMessage());
                            observable.setValue("error");

                        }
                    }
                });
        return observable;
    } //signUp Firebase

    public LiveData<String> loginFirebase(String email, String password) {

        MutableLiveData<String> observable = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGIN", "signInWithEmail:success");
                            user = mAuth.getCurrentUser();
                            idUser = user.getUid();
                            MutableLiveData<String> data = new MutableLiveData<>();

                            observable.setValue(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("LOGIN", "signInWithEmail:failure", task.getException());
                            observable.setValue("error");

                        }
                    }
                });
        return observable;
    } //loginFirebase

    public String signOut() {
        String result = "";
        Log.d("SIGNOUT", idUser);
        try {
            FirebaseAuth.getInstance().signOut();
            result = "success";
        } catch (Exception e) {
            e.printStackTrace();
            result = "error";
        }

        return result;
    }

    public MutableLiveData<String> checkCredentials(String oldPass) {
        MutableLiveData<String> observable = new MutableLiveData<>();

        AuthCredential credential = EmailAuthProvider
                .getCredential(userInfo.getEmail(), oldPass);

        Log.d("check creds", oldPass + userInfo.getEmail());

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
    }

    public MutableLiveData<String> changePSW(String newPass) {
        MutableLiveData<String> observable = new MutableLiveData<>();

        user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    observable.setValue("success");
                    Log.d("changePSW", "Password updated");

                } else {
                    Log.d("changePSW", "Error password not updated");
                    observable.setValue("error");

                }
            }
        });

        return observable;
    }

    public LiveData<Device> subscribeToValues() {

        MutableLiveData<Device> observable = new MutableLiveData<>();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                //Post post = dataSnapshot.getValue(Post.class);

                //Log.d("SUBSCRIBE TO VALUES", dataSnapshot.getValue(Device.class).toString());
                Device device = new Device();
                device = dataSnapshot.getValue(Device.class);
                observable.setValue(device);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };

        mDatabase.child("devices").child(idHardware).addValueEventListener(postListener);

        return observable;

    }

    public LiveData<String> registerUser(String name, String email, String id, String hardwareId, boolean patient){

        MutableLiveData<String> observable = new MutableLiveData<>();

        //firebase method to register user with callback
        Log.d("SIGNUP", "register user FB");
        userInfo = new User(name, email, hardwareId, patient);
        mDatabase.child("users").child(id).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    device = new Device(hardwareId);
                    if (!isHadwareID(hardwareId)) {
                        mDatabase.child("devices").child(hardwareId).setValue(device).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    observable.setValue("success register");
                                    idHardware = hardwareId;
                                    Log.d("USER", "success in register user DB");
                                } else {
                                    observable.setValue("error register");
                                    Log.d("USER", "failure in register user DB:"+task.getException().getLocalizedMessage());
                                }
                            }
                        });
                    } else {
                        observable.setValue("success register");
                        Log.d("USER", "success in register user DB");
                    }

                } else {
                    observable.setValue("error register");
                    Log.d("USER", "failure in register user DB:"+task.getException().getLocalizedMessage());
                }

            }
        });

        return observable;
    }

    private boolean isHadwareID(String hardwareId) {
        final boolean[] found = {false};
        mDatabase.child("devices")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Device dev = snapshot.getValue(Device.class);
                            if (dev.hardwareId.equals(hardwareId)) {
                                found[0] = true;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        return found[0];
    }

    public LiveData<String> getHWid () {
        MutableLiveData<String> observable = new MutableLiveData<>();
        mDatabase.child("users").child(idUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    observable.setValue("error");
                }
                else {
                    Log.d("firebase", "Read info in Login");
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    User user = (User) task.getResult().getValue(User.class);
                    observable.setValue(user.getHardwareId());
                }
            }
        });

        return observable;
    }

    public LiveData<User> getUserInfo() {

        MutableLiveData<User> observable = new MutableLiveData<>();

        mDatabase.child("users").child(idUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    userInfo = new User();
                    userInfo.setEmail("error");
                    observable.setValue(userInfo);
                }
                else {
                    Log.d("firebase", "Read info in Login");
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    userInfo = (User) task.getResult().getValue(User.class);
                    observable.setValue(userInfo);
                }
            }
        });

        return observable;
    }

    public LiveData<Device> getDeviceInfo() {

        MutableLiveData<Device> observable = new MutableLiveData<>();
        mDatabase.child("devices").child(idHardware).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting DEVICE data", task.getException());
                    device = new Device();
                    device.setHardwareId("error");
                    observable.setValue(device);
                }
                else {
                    Log.d("firebase", "Read info in Login DEVICE");
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    device = (Device) task.getResult().getValue(Device.class);
                    observable.setValue(device);
                }
            }
        });

        return observable;
    }


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
    }

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
    }

    public MutableLiveData<String> setValuesEmergency() {
        MutableLiveData<String> observable = new MutableLiveData<>();

        mDatabase.child("devices").child(idHardware).child("tap").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mDatabase.child("devices").child(idHardware).child("lightSensor").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    }

    public boolean isPatient() {

        return userInfo.isPatient();
    }

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
    }

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
    }

    public LiveData<String> storeProfilePicture(String email, byte[] img) {

        MutableLiveData<String> observable = new MutableLiveData<>();

        StorageReference storageRef = firabaseStorage.getReference();
        StorageReference profilePicRef = storageRef.child("profile/"+user.getUid()+".jpg");

        Log.d("SINGUP", "storeProfilePicture: 1");

        UploadTask uploadTask = profilePicRef.putBytes(img);
        uploadTask.addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SIGNUP", "storeProfilePicture: SUCCESS");

                observable.setValue("error");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("SIGNUP", "storeProfilePicture: FAIL");

                observable.setValue("success");
                //storageRef.getDownloadUrl()
            }
        });

        return observable;
    }
}
