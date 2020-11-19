package com.alex_podolian.npuzzle.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alex_podolian.npuzzle.app.NPuzzleApp;
import com.alex_podolian.npuzzle.presenter.Presenter;

import java.util.Objects;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    protected Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NPuzzleApp myApp = (NPuzzleApp) Objects.requireNonNull(getApplicationContext());
        presenter = myApp.getPresenter();
    }
}
