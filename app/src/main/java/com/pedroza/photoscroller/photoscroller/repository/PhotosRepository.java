package com.pedroza.photoscroller.photoscroller.repository;

import android.arch.lifecycle.MutableLiveData;

import com.pedroza.photoscroller.photoscroller.api.FlickrService;
import com.pedroza.photoscroller.photoscroller.model.response.Photo.Photo;
import com.pedroza.photoscroller.photoscroller.model.response.Photo.PhotosResponse;
import com.pedroza.photoscroller.photoscroller.model.response.User.User;
import com.pedroza.photoscroller.photoscroller.model.response.User.UsernameResponse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pedroza on 11/22/17.
 */

@Singleton
public class PhotosRepository {
    private FlickrService mFlickrService;

    private MutableLiveData<List<Photo>> mPhotos;

    private LinkedHashMap<String, Photo> mPhotosInternal;

    @Inject
    public PhotosRepository(FlickrService flickrService) {
        mFlickrService = flickrService;
        mPhotos = new MutableLiveData<>();
        mPhotosInternal = new LinkedHashMap<>();
    }

    public MutableLiveData<List<Photo>> getPhotos() {
        return mPhotos;
    }

    public Photo getPhoto(String photoId) {
       return mPhotosInternal.get(photoId);
    }

    public void fetchRecentPhotos() {
        mFlickrService.getRecentPhotos(null, null).enqueue(new Callback<PhotosResponse>() {
            @Override
            public void onResponse(Call<PhotosResponse> call, Response<PhotosResponse> response) {

                mPhotosInternal.clear();
                List<Photo> photos = response.body().getPhotoQueryInfo().getPhotos();
                for (Photo photo : photos) {
                    mPhotosInternal.put(photo.getId(), photo);
                }
                mPhotos.setValue(photos);
            }

            @Override
            public void onFailure(Call<PhotosResponse> call, Throwable t) {

            }
        });
    }

    public void fetchUserPhotos(String userName) {
        mFlickrService.getUserIdFromName(userName).enqueue(new Callback<UsernameResponse>() {
            @Override
            public void onResponse(Call<UsernameResponse> call, Response<UsernameResponse> response) {
                User user = response.body().getUser();
                if(user == null) {
                    mPhotos.setValue(new ArrayList<>());
                    return;
                }

                mFlickrService.getPhotosByUserId(user.getId(), null, null).enqueue(new Callback<PhotosResponse>() {
                    @Override
                    public void onResponse(Call<PhotosResponse> call, Response<PhotosResponse> response) {
                        mPhotosInternal.clear();
                        List<Photo> photos = response.body().getPhotoQueryInfo().getPhotos();
                        for (Photo photo : photos) {
                            mPhotosInternal.put(photo.getId(), photo);
                        }
                        mPhotos.setValue(photos);
                    }

                    @Override
                    public void onFailure(Call<PhotosResponse> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<UsernameResponse> call, Throwable t) {

            }
        });
    }


}
