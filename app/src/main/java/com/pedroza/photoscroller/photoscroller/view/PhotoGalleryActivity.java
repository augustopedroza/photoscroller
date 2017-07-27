package com.pedroza.photoscroller.photoscroller.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pedroza.photoscroller.photoscroller.R;
import com.pedroza.photoscroller.photoscroller.model.net.FlickrFetcher;
import com.pedroza.photoscroller.photoscroller.model.response.Photo.Photo;
import com.pedroza.photoscroller.photoscroller.model.response.Photo.PhotosResponse;
import com.pedroza.photoscroller.photoscroller.model.response.User.User;
import com.pedroza.photoscroller.photoscroller.model.response.User.UsernameResponse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.pedroza.photoscroller.photoscroller.view.PhotoActivity.PHOTO_INTENT_TAG;

public class PhotoGalleryActivity extends AppCompatActivity {

    private static final String TAG = "PGA";
    private List<Photo> mPhotoItems = new ArrayList<>();

    private FlickrFetcher mFlickrFetcher;

    //
    // Butterknife greatly simplify acccess to view components.
    //
    
    @BindView(R.id.activity_photo_gallery_recycler_view)
    protected RecyclerView mPhotoRecyclerView;

    @BindView(R.id.load_photos_progress_bar)
    protected ProgressBar mProgressBar;

    public PhotoGalleryActivity() {
        mFlickrFetcher = FlickrFetcher.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        ButterKnife.bind(this);

        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mPhotoRecyclerView.setAdapter(new PhotoAdapter(mPhotoItems));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo_gallery, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_username_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.user_name));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mProgressBar.setVisibility(View.VISIBLE);
                fetchUserPhotos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPhotoItems.clear();
                mPhotoRecyclerView.getAdapter().notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    private void fetchUserPhotos(final String username) {

        final Callback<PhotosResponse> photosResponseCallback = new Callback<PhotosResponse>() {
            @Override
            public void onResponse(Call<PhotosResponse> call, Response<PhotosResponse> response) {
                if (response.isSuccessful()) {
                    mPhotoItems.addAll(response.body().getPhotos().getPhoto());
                    if (mPhotoItems.size() == 0)
                        Toast.makeText(getApplicationContext(), R.string.user_no_photos, Toast.LENGTH_SHORT)
                                .show();
                    else
                        mPhotoRecyclerView.getAdapter().notifyDataSetChanged();
                    mProgressBar.setVisibility(GONE);
                }
            }

            @Override
            public void onFailure(Call<PhotosResponse> call, Throwable t) {
                mProgressBar.setVisibility(GONE);
                Toast.makeText(getApplicationContext(), R.string.user_photos_failure, Toast.LENGTH_SHORT)
                        .show();
                Log.e(TAG, "Failed to retrieve photos for the specified user");
            }
        };

        Callback<UsernameResponse> usernameResponseCallback = new Callback<UsernameResponse>() {
            @Override
            public void onResponse(Call<UsernameResponse> call, Response<UsernameResponse> response) {
                if (response.isSuccessful()) {

                    User user = response.body().getUser();
                    if (user == null) {
                        mProgressBar.setVisibility(GONE);
                        Toast.makeText(getApplicationContext(), R.string.user_does_not_exist, Toast.LENGTH_SHORT)
                                .show();
                    }
                    else
                        mFlickrFetcher.getListPictures(user.getNsid(), photosResponseCallback);
                }
            }
            @Override
            public void onFailure(Call<UsernameResponse> call, Throwable t) {
                mProgressBar.setVisibility(GONE);
                Toast.makeText(getApplicationContext(), R.string.user_info_failure, Toast.LENGTH_SHORT)
                .show();
                Log.e(TAG, "Failed to retrieve user id for username");
            }
        };

        mFlickrFetcher.getUserNdId(username, usernameResponseCallback);
    }

    private void loadThumbnail(Photo photo, final PhotoHolder holder) {

        if (photo.getUrl() == null)
            return;

        Callback<ResponseBody> downloadCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    // TODO: 7/26/17 Consider saving recent images from recent users in a DB to allow offline usage of the app. 
                    InputStream stream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    holder.bindDrawable(drawable);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mProgressBar.setVisibility(GONE);
            }
        };

        mFlickrFetcher.loadImage(photo, downloadCallback);
    }

    private class PhotoHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private ImageView mPhotoImageView;
        private Photo mPhotoItem;

        public PhotoHolder(View itemView) {
            super(itemView);

            mPhotoImageView = (ImageView) itemView.findViewById(R.id.photo_gallery_item_image_view);
            mPhotoImageView.setOnClickListener(this);
        }

        public void bindDrawable(Drawable drawable) {mPhotoImageView.setImageDrawable(drawable);}

        public void bindPhotoItem(Photo photoItem) {mPhotoItem = photoItem;}

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
            intent.putExtra(PHOTO_INTENT_TAG, mPhotoItem);
            startActivity(intent);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<Photo> mPhotoItems;

        public PhotoAdapter(List<Photo> photoItems) {
            mPhotoItems = photoItems;
        }
        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View view = (ImageView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.photo_item, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            Photo photo = mPhotoItems.get(position);
            Drawable placeholder = getResources().getDrawable(R.mipmap.ic_camera_light);
            holder.bindDrawable(placeholder);
            holder.bindPhotoItem(photo);

            // TODO: 7/26/17 Consider using an image cache to avoid downloading the same images during scrolling
            loadThumbnail(photo, holder);
        }

        @Override
        public int getItemCount() {
            return mPhotoItems.size();
        }
    }
}
