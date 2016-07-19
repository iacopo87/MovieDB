package pazzaglia.it.moviedb.service;

import android.app.Activity;
import android.app.ProgressDialog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import pazzaglia.it.moviedb.model.Movie;
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

    //define callback interface
     public interface MyCallbackInterface<S> {

        void onDownloadFinishedOK(S result);
        void onDownloadFinishedKO(S result);
    }

    protected  TheMovieDBInterface getApiService(){

        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Movie.class, new CustomDeserialize())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
                    //.addConverterFactory(MoshiConverterFactory.create())//                .build();

        final TheMovieDBInterface mInterfaceService = retrofit.create(TheMovieDBInterface.class);
        return mInterfaceService;
    }
    public void doApiCall(Activity activity, String loadingMessage, final MyCallbackInterface<T> callback){
        final ProgressDialog progressDialog = Util.showDialog(activity, loadingMessage);
        Call<T> mService = specificApiCall();
        mService.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                T mObject = response.body();
                boolean responseHasError = response.errorBody()!= null && mObject==null;
                if(!responseHasError){
                    callback.onDownloadFinishedOK(mObject);//doApiCallOK(mObject);
                }else {
                    callback.onDownloadFinishedKO(mObject);//doApiCallKO(mObject);
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

    //public abstract void doApiCallOK(T mObject);
    //public abstract void doApiCallKO(T mObject);
    public void doApiCallOnFailure(){};
    public abstract Call<T> specificApiCall();

}

 class CustomDeserialize implements JsonDeserializer<Movie> {

    private final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Movie deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final Movie response = new Movie();
        final JsonObject obj = json.getAsJsonObject();

        // Try parse date.
        try {
            response.setPosterPath(obj.get("poster_path").getAsString()); // parse created-at

            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return response;
    }
}