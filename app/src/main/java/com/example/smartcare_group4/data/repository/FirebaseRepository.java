package com.example.smartcare_group4.data.repository;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcare_group4.data.ResponseItem;
import com.example.smartcare_group4.data.user.UserItem;
import com.example.smartcare_group4.ui.main.MainActivity;
import com.example.smartcare_group4.viewmodel.User;
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

    public LiveData<ResponseItem> calculateResult(){

        MutableLiveData<ResponseItem> observable = new MutableLiveData<>();

        //crida a firebase o on sigui per obtenir el resultat de forma asincrona

        if(false) { //si error
            observable.setValue(ResponseItem.error("Missatge de error"));
        }else { //si no error
            observable.setValue(ResponseItem.success(new UserItem())); //s'utilitza objecte genéric per retornar la resposta
        }

        return observable;
    }

    public LiveData<String> registerUser(String name, String email, String password, String id, String hardwareId, boolean patient){

        MutableLiveData<String> observable = new MutableLiveData<>();

        //firebase method to register user with callback
        Log.d("SIGNUP", "register user FB");
        User user = new User(name, email, password, hardwareId, patient);
        mDatabase.child("users").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
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

        return observable;
    }


    public LiveData<User> getUserInfo(String id) {

        MutableLiveData<User> observable = new MutableLiveData<>();

        User user = new User();

        mDatabase.child("users").child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    user.email = "error";
                    observable.setValue(user);
                }
                else {
                    Log.d("firebase", "Read info in Login");
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    observable.setValue((User) task.getResult().getValue());
                }
            }
        });
        
        return observable;
    }
}
