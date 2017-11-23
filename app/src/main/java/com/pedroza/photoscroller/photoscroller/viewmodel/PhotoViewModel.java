package com.pedroza.photoscroller.photoscroller.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;

import com.pedroza.photoscroller.photoscroller.model.response.Photo.Photo;
import com.pedroza.photoscroller.photoscroller.repository.PhotosRepository;

import javax.inject.Inject;

/**
 * Created by pedroza on 7/25/17.
 */

public class PhotoViewModel extends ViewModel {

    //
    //  Observable fields handle automatically inform the UI when to update.
    //

    public ObservableField<Drawable> photoDrawable = new ObservableField<>();
    public ObservableField<String> photoId = new ObservableField<>();
    public ObservableField<String> photoTitle = new ObservableField<>();

    //
    // A boolean can be easily converted to visibily by using a conversert. See activity_photo.xml
    // for its usage.
    //
    public ObservableField<Boolean> loadInProgress = new ObservableField<>();

    private final String TAG = "PVM";
    private PhotosRepository mPhotoRepository;
    private Photo mPhoto;

    @Inject
    public PhotoViewModel(PhotosRepository photosRepository) {
        mPhotoRepository = photosRepository;
    }

    public void setPhotoId(String id) {
        mPhoto = mPhotoRepository.getPhoto(id);
        photoId.set(id);
        photoTitle.set(mPhoto.getTitle());

        loadInProgress.set(true);
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public void stopProgressBar() {
        loadInProgress.set(false);
    }
}
