package pazzaglia.it.moviedb.shared;

import android.app.Activity;
import android.app.ProgressDialog;

import pazzaglia.it.moviedb.service.TheMovieDBInterface;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static pazzaglia.it.moviedb.shared.Constant.BASE_URL;

/**
 * Created by IO on 13/07/2016.
 */

public class Util {

    public static TheMovieDBInterface getApiService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final TheMovieDBInterface mInterfaceService = retrofit.create(TheMovieDBInterface.class);
        return mInterfaceService;
    }

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
