package com.pedroza.photoscroller.photoscroller.model.net;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pedroza on 7/24/17.
 */

public class FlickrFetcherService {
    private static final String SERVER_URL = "https://api.flickr.com";
    private static final String API_KEY = "7de612d5d2b9999e7dda085b6eb2cd3b";
    private static final String TAG = "FFS";


    public FlickrFetcherServiceInterface getFlickrFetchrServiceInterface() {
        return builRetrofit().create(FlickrFetcherServiceInterface.class);
    }

   private Retrofit builRetrofit() {
       Retrofit retrofit = new Retrofit.Builder()
               .baseUrl(SERVER_URL)
               .addConverterFactory(GsonConverterFactory.create())
               .client(new OkHttpClient.Builder()
                       .readTimeout(60_000, TimeUnit.MILLISECONDS)
                       .writeTimeout(60_000, TimeUnit.MILLISECONDS)
                       .connectTimeout(60_000, TimeUnit.MILLISECONDS)
                       .addInterceptor(createHttpLoggingInterceptor())
                       .addInterceptor(new Interceptor() {
                           @Override
                           public okhttp3.Response intercept(Chain chain) throws IOException {

                               Request original = chain.request();
                               HttpUrl originalHttpUrl = original.url();

                               HttpUrl url = originalHttpUrl.newBuilder()
                                       .addQueryParameter("api_key", API_KEY)
                                       .addQueryParameter("format", "json")
                                       .addQueryParameter("nojsoncallback", "1")
                                       .addQueryParameter("extras", "url_s")
                                       .build();
                               Request.Builder requestBuilder = original.newBuilder()
                                       .url(url);

                               Request request = requestBuilder.build();
                               return chain.proceed(request);
                           }
                       }).build())
               .build();

       return retrofit;
   }

    private Interceptor createHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }
}
