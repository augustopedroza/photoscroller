package com.pedroza.photoscroller.photoscroller.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.pedroza.photoscroller.photoscroller.model.response.Photo.Photo;
import com.pedroza.photoscroller.photoscroller.repository.PhotosRepository;

import java.util.List;

import javax.inject.Inject;


public class PhotoGalleryViewModel extends ViewModel {

    private PhotosRepository mPhotosRepository;

    @Inject
    public PhotoGalleryViewModel(PhotosRepository photosRepository) {
        mPhotosRepository = photosRepository;
    }

    public LiveData<List<Photo>> getPhotos() {
        return mPhotosRepository.getPhotos();
    }

    public void loadRecentPhotos() {
        mPhotosRepository.fetchRecentPhotos();
    }

    public void loadUserPhotos(String userName) {
        mPhotosRepository.fetchUserPhotos(userName);
    }
}
