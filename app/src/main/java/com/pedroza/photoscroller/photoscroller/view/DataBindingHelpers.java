package com.pedroza.photoscroller.photoscroller.view;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by pedroza on 7/25/17.
 */

public class DataBindingHelpers {

    //
    // Binding adapters are very powerful but are very obscure. The method below can be put anywhere
    // and it will be found during compilation time. This particular adapter is used to create a
    // binding to drawable.
    //

    @BindingAdapter("android:src")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }
}
