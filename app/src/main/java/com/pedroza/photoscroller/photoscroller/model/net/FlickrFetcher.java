package com.pedroza.photoscroller.photoscroller.model.net;

import com.pedroza.photoscroller.photoscroller.model.response.Photo.Photo;
import com.pedroza.photoscroller.photoscroller.model.response.Photo.PhotosResponse;
import com.pedroza.photoscroller.photoscroller.model.response.User.UsernameResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by pedroza on 7/25/17.
 */

public class FlickrFetcher {

    private FlickrFetcherService mService;
    private FlickrFetcherServiceInterface mFetcher;

    private static final FlickrFetcher instance = new FlickrFetcher();

    public static FlickrFetcher getInstance(){
        return instance;
    }
    private FlickrFetcher() {
        mService = new FlickrFetcherService();
        mFetcher = mService.getFlickrFetchrServiceInterface();
    }

    public void getUserNdId(String username, Callback<UsernameResponse> cb) {
        Call<UsernameResponse> userCall = mFetcher.getUserIdFromName(username);
        userCall.enqueue(cb);
    }

    public void getListPicturesForUser(String userNsId, Callback<PhotosResponse> cb) {
        Call<PhotosResponse> photosCall = mFetcher.getPhotosByUserId(userNsId, null, null);
        photosCall.enqueue(cb);
    }

    public void getListOfRecentPictures(Callback<PhotosResponse> cb) {
        Call<PhotosResponse> photosCall = mFetcher.getRecentPhotos(null, null);
        photosCall.enqueue(cb);
    }

    public void loadImage(Photo photo, Callback<ResponseBody> downloadCallback ) {

        Call<ResponseBody> downloadCall = mFetcher.downloadFileWithDynamicUrl(photo.getUrl());
        downloadCall.enqueue(downloadCallback);
    }
}
