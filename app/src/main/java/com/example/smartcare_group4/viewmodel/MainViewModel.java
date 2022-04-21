package com.example.smartcare_group4.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.repository.DatabaseRepository;
import com.example.smartcare_group4.data.repository.FirebaseRepository;
import com.example.smartcare_group4.data.user.UserItem;
import com.example.smartcare_group4.utils.PreferenceUtil;

public class MainViewModel extends ViewModel {

    //our repository
    private FirebaseRepository firebaseRepository;
    private DatabaseRepository databaseRepository;

    public MainViewModel(Context context) {
        initFirebaseRepository();
        initDatabaseRepository(context);
    }

    public void initFirebaseRepository() {
        firebaseRepository = new FirebaseRepository();
    }

    public void initDatabaseRepository(Context context) {
        databaseRepository = new DatabaseRepository(context);
    }

    //TODO change to LiveData<ResponseItem>
    public LiveData<String> checkInputsAndLogin(PreferenceUtil preferenceUtil, String name, String email, String password, String passwordRepeated) {

        MediatorLiveData<String> observable = new MediatorLiveData<>();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            observable.setValue("error #1"); //TODO string resource
        } else {
            if (password.equals(passwordRepeated)) {
                observable.setValue("error #2"); //TODO string resource
            } else {
                LiveData<UserItem> registerUserObservable = firebaseRepository.registerUser(name, email, password);
                observable.addSource(registerUserObservable, new Observer<UserItem>() {
                    @Override
                    public void onChanged(UserItem userItem) {
                        if (userItem != null) {
                            //store in preferences
                            observable.removeSource(registerUserObservable);
                            LiveData<String> storeUserObservable = databaseRepository.saveUser(userItem);
                            observable.addSource(storeUserObservable, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    if (s.equals("success")) {
                                        preferenceUtil.setString("autologin", "yes");
                                        observable.setValue("success");
                                    } else {
                                        observable.setValue("error #4");
                                    }
                                }
                            });
                        } else {
                            observable.setValue("error #3");
                        }
                    }
                });
            }
        }
        return observable;
    }
}
