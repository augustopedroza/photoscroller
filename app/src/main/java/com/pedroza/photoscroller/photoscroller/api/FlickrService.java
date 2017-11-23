package com.pedroza.photoscroller.photoscroller.api;


import com.pedroza.photoscroller.photoscroller.model.response.Photo.PhotosResponse;
import com.pedroza.photoscroller.photoscroller.model.response.User.UsernameResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrService {


    String SERVER_URL = "https://api.flickr.com";
    String API_KEY = "7de612d5d2b9999e7dda085b6eb2cd3b";

    @GET("/services/rest/?method=flickr.people.findByUsername")
    Call<UsernameResponse> getUserIdFromName(@Query("username") String username);

    @GET("/services/rest/?method=flickr.people.getPhotos")
    Call<PhotosResponse> getPhotosByUserId(@Query("user_id") String userId, @Query("per_page") String numPhotosPerPage, @Query("page") String pageNumber);

    @GET("/services/rest/?method=flickr.photos.getRecent")
    Call<PhotosResponse> getRecentPhotos(@Query("per_page") String numPhotosPerPage, @Query("page") String pageNumber);

}
