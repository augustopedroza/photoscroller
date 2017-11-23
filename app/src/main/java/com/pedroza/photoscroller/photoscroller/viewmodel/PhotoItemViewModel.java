package com.pedroza.photoscroller.photoscroller.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.pedroza.photoscroller.photoscroller.model.response.Photo.Photo;
import com.pedroza.photoscroller.photoscroller.view.PhotoActivity;

import static com.pedroza.photoscroller.photoscroller.view.PhotoActivity.PHOTO_ID;

/**
 * Created by pedroza on 11/22/17.
 */

public class PhotoItemViewModel {
    Photo mPhoto;

    public void setPhoto(Photo photo) {
        mPhoto = photo;
    }
    public View.OnClickListener onPhotoClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent();
                intent.putExtra(PHOTO_ID, mPhoto.getId());
                intent.setClass(context, PhotoActivity.class);
                context.startActivity(intent);
            }
        };
    }
}
