package com.threel.apptest;

import android.app.Application;



public class component extends Application {
    public MainActivity.component component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerMainActivity_component.builder()
                .jsonObjects(new JsonObjects())
                .build();
    }

    public MainActivity.component getComponent() {
        return component;
    }
}
