package com.unware.mediaapp.ui.bindingadapter

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.unware.mediaapp.R
import java.io.File

@BindingAdapter("imageFromFilePath")
fun bindImageFromPath(view: ImageView, imagePath: String?) {
    Glide.with(view.context)
        .load(if (imagePath.isNullOrEmpty()) R.drawable.bg_media_placeholder else File(imagePath))
        .error(R.drawable.bg_media_placeholder)
        .placeholder(R.drawable.bg_media_placeholder)
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply(RequestOptions.bitmapTransform(RoundedCorners(view.context.resources.getDimensionPixelOffset(R.dimen.gap_s))))
        .into(view)
}