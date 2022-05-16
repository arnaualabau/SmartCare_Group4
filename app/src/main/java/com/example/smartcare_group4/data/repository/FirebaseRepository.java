package com.example.smartcare_group4.data.repository;


import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcare_group4.data.ResponseItem;
import com.example.smartcare_group4.data.user.UserItem;
import com.example.smartcare_group4.ui.init.InitActivity;
import com.example.smartcare_group4.ui.init.login.LoginFragment;
import com.example.smartcare_group4.ui.main.MainActivity;
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

import java.util.concurrent.Executor;

public class FirebaseRepository {

    public FirebaseRepository(){
        //init firebase libraries
        // Initialize Firebase Auth

    }

    public LiveData<String> loginFirebase(String email, String password) {

        MutableLiveData<String> observable = new MutableLiveData<>();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            observable.setValue("success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            //Toast.makeText(LoginFragment.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
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

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("devices").addValueEventListener(postListener);
        
        
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

    public LiveData<UserItem> registerUser(String name, String email, String password){

        MutableLiveData<UserItem> observable = new MutableLiveData<>();
        UserItem userItem = new UserItem();

        //firebase method to regiter user with callback
        if(true){
            userItem.setName(name);
            userItem.setEmail(email);
            userItem.setPassword(password);
        }else{
            userItem.setError("error with firebase...");
        }
        observable.setValue(userItem);

        return observable;
    }


}
