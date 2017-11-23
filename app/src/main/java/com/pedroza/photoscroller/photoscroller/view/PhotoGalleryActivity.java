package com.pedroza.photoscroller.photoscroller.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pedroza.photoscroller.photoscroller.R;
import com.pedroza.photoscroller.photoscroller.model.response.Photo.Photo;
import com.pedroza.photoscroller.photoscroller.view.adapter.PhotoAdapter;
import com.pedroza.photoscroller.photoscroller.viewmodel.PhotoGalleryViewModel;
import com.pedroza.photoscroller.photoscroller.viewmodel.factories.PhotoGalleryViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class PhotoGalleryActivity extends AppCompatActivity {

    @Inject
    PhotoGalleryViewModelFactory mViewModelFactory;

    private static final String TAG = "PGA";
    private static final String LATEST_QUERY = "username";
    private List<Photo> mPhotoItems = new ArrayList<>();

    private String mCurrentUserName;
    private PhotoGalleryViewModel mViewModel;

    //
    // Butterknife greatly simplify acccess to view components.
    //
    
    @BindView(R.id.activity_photo_gallery_recycler_view)
    protected RecyclerView mPhotoRecyclerView;

    @BindView(R.id.load_photos_progress_bar)
    protected ProgressBar mProgressBar;

    private PhotoAdapter mPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PhotoGalleryViewModel.class);

        mViewModel.getPhotos().observe(this, photos -> {
            setupOrUpdateAdapter(photos);
        });

        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        if (savedInstanceState != null)
           mCurrentUserName = savedInstanceState.getString(LATEST_QUERY);

        if (mCurrentUserName != null)
            mViewModel.loadUserPhotos(mCurrentUserName);
        else
            mViewModel.loadRecentPhotos();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo_gallery, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_username_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.user_name));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mViewModel.loadRecentPhotos();
                searchView.onActionViewCollapsed();
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mProgressBar.setVisibility(View.VISIBLE);
                mCurrentUserName = query;
                mViewModel.loadUserPhotos(mCurrentUserName);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LATEST_QUERY, mCurrentUserName);
    }

    private void setupOrUpdateAdapter(List<Photo> photos) {

        if ( mPhotoAdapter!= null) {
            mPhotoAdapter.setPhotoItems(photos);
            mPhotoAdapter.notifyDataSetChanged();

        } else {
            mPhotoAdapter = new PhotoAdapter(photos, Glide.with(this));
            mPhotoRecyclerView.setAdapter(mPhotoAdapter);
        }

        mProgressBar.setVisibility(View.GONE);

        if (photos.size() == 0) {
            Toast.makeText(this, R.string.unable_to_fetch ,Toast.LENGTH_LONG).show();
        }
    }

}
