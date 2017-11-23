package com.pedroza.photoscroller.photoscroller.view;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pedroza.photoscroller.photoscroller.model.response.Photo.Photo;
import com.pedroza.photoscroller.photoscroller.viewmodel.PhotoViewModel;

/**
 * Created by pedroza on 7/25/17.
 */

public class DataBindingHelpers {

    //
    // Binding adapters are very powerful but are somewhat obscure. The method below can be put anywhere
    // and it will be found during compilation time. This particular adapter is used to create a
    // custom binding.
    //

    @BindingAdapter("android:photo")
    public static void setPhotoUrl(ImageView view, PhotoViewModel vm) {
        Photo photo = vm.getPhoto();
        Glide.with(view.getContext()).load(photo.getUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        vm.stopProgressBar();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        vm.stopProgressBar();
                        return false;
                    }
                }).into(view);
    }
}
