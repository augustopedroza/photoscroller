package com.pedroza.photoscroller.photoscroller.di;

import com.pedroza.photoscroller.photoscroller.api.FlickrService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.pedroza.photoscroller.photoscroller.api.FlickrService.API_KEY;
import static com.pedroza.photoscroller.photoscroller.api.FlickrService.SERVER_URL;


@Module
public class AppModule {

    @Singleton
    @Provides
    FlickrService provideFlickrService() {

        //
        //  OkHttp3 injector allows me to add the api_key paramter to every request
        //

        return new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .readTimeout(20_000, TimeUnit.MILLISECONDS)
                        .writeTimeout(20_000, TimeUnit.MILLISECONDS)
                        .connectTimeout(20_000, TimeUnit.MILLISECONDS)
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
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
                .build()
                .create(FlickrService.class);
    }
}
