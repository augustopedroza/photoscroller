package com.pedroza.photoscroller.photoscroller.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.pedroza.photoscroller.photoscroller.R;
import com.pedroza.photoscroller.photoscroller.model.net.FlickrFetcher;
import com.pedroza.photoscroller.photoscroller.model.response.Photo.Photo;
import com.pedroza.photoscroller.photoscroller.view.PhotoActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pedroza on 7/25/17.
 */

public class PhotoViewModel extends BaseViewModel{

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
    private FlickrFetcher mFetcher;
    private Photo mPhoto;
    private Context mContext;
    private File mFile = null;

    public PhotoViewModel(Context context, Photo photo) {
        mFetcher = FlickrFetcher.getInstance();
        mPhoto = photo;
        mContext = context;
    }

    @Override
    public void onStart() {

        photoId.set("Id: " + mPhoto.getId());
        photoTitle.set("Title: " + mPhoto.getTitle());

        Callback<ResponseBody> downloadCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    InputStream stream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    Drawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
                    loadInProgress.set(false);
                    photoDrawable.set(drawable);
                    saveLocalBitmap(drawable);
                    ((PhotoActivity) mContext).updateShareIntent(mFile);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loadInProgress.set(false);
                Toast.makeText(mContext, R.string.photo_download_failure, Toast.LENGTH_SHORT)
                .show();

                Log.e(TAG, "Unable to download image");
            }
        };
        loadInProgress.set(true);
        mFetcher.loadImage(mPhoto, downloadCallback);
    }

    public void saveLocalBitmap(Drawable drawable) {
        // Extract Bitmap from ImageView drawable

        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) drawable).getBitmap();
        }
        if (drawable == null)
            return;
        // Store image to default external storage directory
        try {
            mFile = new File(mContext.getExternalFilesDir(Context.DOWNLOAD_SERVICE), "share_image_" + System.currentTimeMillis() + ".png");
            mFile.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(mFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {
        mFile.delete();
    }
}
