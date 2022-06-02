package com.example.smartcare_group4.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcare_group4.data.Device;
import com.example.smartcare_group4.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class FirebaseRepository {
    public static FirebaseRepository firebaseInstance = new FirebaseRepository();
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private String idUser;
    private String idHardware;

    private User userInfo;
    private Device device;

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

    public void setHWId(String hardwareId) {
        idHardware = hardwareId;
    }

}
