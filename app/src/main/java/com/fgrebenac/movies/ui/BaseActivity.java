package com.fgrebenac.movies.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public static ProgressDialog progressDialog;

    public static void showProgress(Context context, String message) {
        progressDialog = ProgressDialog.show(context, null, message, true, false);
    }

    public static void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
