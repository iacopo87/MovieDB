package pazzaglia.it.moviedb.service;

import android.app.Activity;
import android.app.ProgressDialog;

import pazzaglia.it.moviedb.shared.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static pazzaglia.it.moviedb.shared.Constant.BASE_URL;


/**
 * Created by IO on 18/07/2016.
 */

public abstract class AbstractApiCaller<T> {


    protected  TheMovieDBInterface getApiService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final TheMovieDBInterface mInterfaceService = retrofit.create(TheMovieDBInterface.class);
        return mInterfaceService;
    }
    public void doApiCall(Activity activity, String loadingMessage){
        final ProgressDialog progressDialog = Util.showDialog(activity, loadingMessage);
        Call<T> mService = specificApiCall();
        mService.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                T mObject = response.body();
                boolean responseHasError = response.errorBody()!= null && mObject==null;
                if(!responseHasError){
                    doApiCallOK(mObject);
                }else {
                    doApiCallKO(mObject);
                }
                Util.dismissDialog(progressDialog);
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                call.cancel();
                doApiCallOnFailure();
            }
        });
    }

    public abstract void doApiCallOK(T mObject);
    public abstract void doApiCallKO(T mObject);
    public void doApiCallOnFailure(){};
    public abstract Call<T> specificApiCall();

}
