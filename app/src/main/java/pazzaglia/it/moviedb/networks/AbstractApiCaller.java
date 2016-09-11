package pazzaglia.it.moviedb.networks;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static pazzaglia.it.moviedb.utils.Constant.BASE_URL;


/**
 * Created by IO on 18/07/2016.
 */

public abstract class AbstractApiCaller<T> {

    //define callback interface
     public interface MyCallbackInterface<S> {

        void onDownloadFinishedOK(S result);
        void onDownloadFinishedKO(S result);
    }

    protected  TheMovieDBInterface getApiService(){

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final TheMovieDBInterface mInterfaceService = retrofit.create(TheMovieDBInterface.class);
        return mInterfaceService;
    }
    public void doApiCall(Context context, String loadingMessage, int movieId, final MyCallbackInterface<T> callback){
        Call<T> mService = specificApiCall(movieId);
        mService.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                T mObject = response.body();
                boolean responseHasError = response.errorBody()!= null && mObject==null;
                if(!responseHasError){
                    callback.onDownloadFinishedOK(mObject);
                }else {
                    callback.onDownloadFinishedKO(mObject);
                }
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                call.cancel();
                doApiCallOnFailure();
            }
        });
    }


    public void doApiCallOnFailure(){};
    public abstract Call<T> specificApiCall(int movieId);

}
