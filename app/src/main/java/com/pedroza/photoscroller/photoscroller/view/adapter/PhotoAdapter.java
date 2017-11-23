package com.pedroza.photoscroller.photoscroller.view.adapter;

/**
 * Created by pedroza on 11/22/17.
 */

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.pedroza.photoscroller.photoscroller.R;
import com.pedroza.photoscroller.photoscroller.databinding.PhotoItemBinding;
import com.pedroza.photoscroller.photoscroller.model.response.Photo.Photo;
import com.pedroza.photoscroller.photoscroller.viewmodel.PhotoItemViewModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;



public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private List<Photo> mPhotoItems;
    private final RequestManager mGlide;

    public PhotoAdapter(List<Photo> photoItems, RequestManager glide) {
        mPhotoItems = photoItems;
        mGlide = glide;
    }
    @Override
    public PhotoAdapter.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        PhotoItemBinding photoItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                        R.layout.photo_item,
                        parent,
                        false);
        return new PhotoHolder(photoItemBinding);
    }

    @Override
    public void onBindViewHolder(PhotoAdapter.PhotoHolder holder, int position) {
        Photo photo = mPhotoItems.get(position);
        holder.bind(photo);
        mGlide.load(photo.getUrl()).into(holder.mBinding.photoGalleryItemImageView);
    }

    public void setPhotoItems(List<Photo> photos) {
        this.mPhotoItems = photos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPhotoItems.size();
    }

    @Override
    public void onViewRecycled(PhotoAdapter.PhotoHolder holder) {
        super.onViewRecycled(holder);
        mGlide.clear(holder.mBinding.photoGalleryItemImageView);
    }

    public static class PhotoHolder extends RecyclerView.ViewHolder {

        private PhotoItemBinding mBinding;
        private Call<ResponseBody> mDownloadRequest;


        public PhotoHolder(PhotoItemBinding binding) {
            super(binding.photoCardView);
            this.mBinding = binding;
            this.mBinding.setViewModel(new PhotoItemViewModel());
        }

        public void bind(Photo photo) {
            mBinding.getViewModel().setPhoto(photo);
        }
    }

}
