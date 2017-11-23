package com.pedroza.photoscroller.photoscroller.viewmodel.factories;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.pedroza.photoscroller.photoscroller.viewmodel.PhotoGalleryViewModel;

import javax.inject.Inject;

/**
 * The default ViewModelFactory does cannot provide viewModels with non-empty contructors.
 * This custom factory solves the problem.
 */

public class PhotoGalleryViewModelFactory implements ViewModelProvider.Factory {
    private PhotoGalleryViewModel mViewModel;

    @Inject
    public PhotoGalleryViewModelFactory(PhotoGalleryViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PhotoGalleryViewModel.class)) {
            return (T) mViewModel;
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
