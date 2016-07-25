package pazzaglia.it.moviedb.utils;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by IO on 13/07/2016.
 */

public class Util {


    public static ProgressDialog showDialog(Activity activity, String message){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    public static void dismissDialog(ProgressDialog progressDialog){
        progressDialog.dismiss();
    }


}
