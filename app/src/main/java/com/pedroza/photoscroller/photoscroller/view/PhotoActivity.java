package com.pedroza.photoscroller.photoscroller.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pedroza.photoscroller.photoscroller.R;
import com.pedroza.photoscroller.photoscroller.databinding.ActivityPhotoBinding;
import com.pedroza.photoscroller.photoscroller.model.response.Photo.Photo;
import com.pedroza.photoscroller.photoscroller.viewmodel.PhotoViewModel;

/**
 * Created by pedroza on 7/25/17.
 */

public class PhotoActivity extends AppCompatActivity {

    static final String PHOTO_INTENT_TAG = "PHOTO";
    private PhotoViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPhotoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_photo);
        Bundle bundle = getIntent().getExtras();

        mViewModel = new PhotoViewModel(this, (Photo) bundle.getSerializable(PHOTO_INTENT_TAG));
        mViewModel.onStart();
        binding.setViewModel(mViewModel);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewModel.onStop();
    }
}

