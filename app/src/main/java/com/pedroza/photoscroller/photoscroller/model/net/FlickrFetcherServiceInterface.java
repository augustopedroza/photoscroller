package com.pedroza.photoscroller.photoscroller.model.net;


import com.pedroza.photoscroller.photoscroller.model.response.Photo.PhotosResponse;
import com.pedroza.photoscroller.photoscroller.model.response.User.UsernameResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface FlickrFetcherServiceInterface {

    @GET("/services/rest/?method=flickr.people.findByUsername")
    Call<UsernameResponse> getUserIdFromName(@Query("username") String username);

    @GET("/services/rest/?method=flickr.people.getPhotos")
    Call<PhotosResponse> getPhotosByUserId(@Query("user_id") String userId, @Query("per_page") String numPhotosPerPage, @Query("page") String pageNumber);

    @GET("/services/rest/?method=flickr.photos.getRecent")
    Call<PhotosResponse> getRecentPhotos(@Query("per_page") String numPhotosPerPage, @Query("page") String pageNumber);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrl(@Url String fileUrl);
}
