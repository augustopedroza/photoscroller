package com.pedroza.photoscroller.photoscroller.model.net;


import com.pedroza.photoscroller.photoscroller.model.response.Photo.PhotosResponse;
import com.pedroza.photoscroller.photoscroller.model.response.User.UsernameResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface FlickrFetcherServiceInterface {
    //https://api.flickr.com/services/rest/?method=flickr.people.findByUsername&username=Thiago%20Sigrist&api_key=7de612d5d2b9999e7dda085b6eb2cd3b&format=rest

    //URL: https://api.flickr.com/services/rest/?method=flickr.people.findByUsername&api_key=83ec95ab24bb1fe886b6c9d43c45fab4&username=Thiago+Sigrist&format=rest

//    <?xml version="1.0" encoding="utf-8" ?>
//<rsp stat="ok">
//  <user id="36377306@N00" nsid="36377306@N00">
//    <username>Thiago Sigrist</username>
//  </user>
//</rsp>
    @GET("/services/rest/?method=flickr.people.findByUsername")
    Call<UsernameResponse> getUserIdFromName(@Query("username") String username);

    @GET("/services/rest/?method=flickr.people.getPhotos")
    Call<PhotosResponse> getPhotosByUserId(@Query("user_id") String userId, @Query("per_page") String numPhotosPerPage, @Query("page") String pageNumber);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrl(@Url String fileUrl);
}
