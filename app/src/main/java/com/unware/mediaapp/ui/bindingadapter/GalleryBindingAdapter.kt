package com.unware.mediaapp.ui.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.unware.mediaapp.R
import com.unware.mediaapp.model.Gallery

@BindingAdapter("galleryListImage")
fun bindGalleryListItemImage(view: ImageView, gallery: Gallery) {

    Glide.with(view.context)
        .load(gallery.largeImageURL)
        .error(R.drawable.bg_gallery_placeholder)
        .placeholder(R.drawable.bg_gallery_placeholder)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}

@BindingAdapter("galleryDetailImage")
fun bindGalleryDetailImage(view: ImageView, gallery: Gallery) {

    Glide.with(view.context)
        .load(gallery.largeImageURL)
        .thumbnail(
            Glide.with(view.context)
                .load(gallery.previewURL)
        )
        .error(R.drawable.bg_media_placeholder)
        .placeholder(R.drawable.bg_media_placeholder)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}

@BindingAdapter("galleryDetailUserAvatar")
fun bindGalleryDetailUserAvatar(view: ImageView, gallery: Gallery) {

    Glide.with(view.context)
        .load(gallery.userImageURL)
        .transition(DrawableTransitionOptions.withCrossFade())
        .circleCrop()
        .into(view)
}