package com.example.smartcare_group4.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcare_group4.data.Device;
import com.example.smartcare_group4.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseRepository {
    private DatabaseReference mDatabase;

    public FirebaseRepository(){
        //init firebase libraries
        // Initialize Firebase Auth

        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    public LiveData<String> signUpFirebase(String email, String password) {

        MutableLiveData<String> observable = new MutableLiveData<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SIGNUP", "signUpWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("SIGNUP", user.getUid());
                            //observable.setValue("success");
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGIN", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
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
        try {
            FirebaseAuth.getInstance().signOut();
            result = "success";
        } catch (Exception e) {
            e.printStackTrace();
            result = "error";
        }

        return result;
    }

    public LiveData<String> subscribeToValues() {

        MutableLiveData<String> observable = new MutableLiveData<>();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                //Post post = dataSnapshot.getValue(Post.class);
                observable.setValue("new value");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };

        return observable;
        
    }

    /*public LiveData<ResponseItem> calculateResult(){

        MutableLiveData<ResponseItem> observable = new MutableLiveData<>();

        //crida a firebase o on sigui per obtenir el resultat de forma asincrona

        if(false) { //si error
            observable.setValue(ResponseItem.error("Missatge de error"));
        }else { //si no error
            observable.setValue(ResponseItem.success(new UserItem())); //s'utilitza objecte genéric per retornar la resposta
        }

        return observable;
    }*/

    public LiveData<String> registerUser(String name, String email, String password, String id, String hardwareId, boolean patient){

        MutableLiveData<String> observable = new MutableLiveData<>();

        //firebase method to register user with callback
        Log.d("SIGNUP", "register user FB");
        User user = new User(name, email, password, hardwareId, patient);
        mDatabase.child("users").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Device device = new Device(hardwareId);
                    if (!isHadwareID(hardwareId)) {
                        mDatabase.child("devices").child(hardwareId).setValue(device).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    observable.setValue("success register");
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
        Log.d("HW_ID", "before");
        mDatabase.child("devices")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Device dev = snapshot.getValue(Device.class);
                            System.out.println(dev.hardwareId);
                            if (dev.hardwareId.equals(hardwareId)) {
                                found[0] = true;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        Log.d("HW_ID", "after");
        return found[0];
    }


    public LiveData<User> getUserInfo(String id) {

        MutableLiveData<User> observable = new MutableLiveData<>();

        mDatabase.child("users").child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    User user = new User();
                    user.setEmail("error");
                    observable.setValue(user);
                }
                else {
                    Log.d("firebase", "Read info in Login");
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    User user = (User) task.getResult().getValue(User.class);
                    observable.setValue(user);
                }
            }
        });

        return observable;
    }



    public LiveData<Device> getDeviceInfo(String id) {

        MutableLiveData<Device> observable = new MutableLiveData<>();

        mDatabase.child("devices").child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting DEVICE data", task.getException());
                    Device device = new Device();
                    device.setHardwareId("error");
                    observable.setValue(device);
                }
                else {
                    Log.d("firebase", "Read info in Login DEVICE");
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    Device device = (Device) task.getResult().getValue(Device.class);
                    observable.setValue(device);
                }
            }
        });

        return observable;
    }
}
