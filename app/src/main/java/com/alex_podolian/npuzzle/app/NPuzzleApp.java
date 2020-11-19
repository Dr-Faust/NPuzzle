package com.alex_podolian.npuzzle.app;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.alex_podolian.npuzzle.model.Model;
import com.alex_podolian.npuzzle.presenter.Presenter;

public class NPuzzleApp extends Application {

//	static {
//		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//	}

    protected Presenter presenter;
    private static NPuzzleApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mInstance = this;
        Model model = new Model();
        presenter = new Presenter(model);
    }

    public Presenter getPresenter() {
        return presenter;
    }

    public static synchronized NPuzzleApp getInstance() {
        return mInstance;
    }
}
