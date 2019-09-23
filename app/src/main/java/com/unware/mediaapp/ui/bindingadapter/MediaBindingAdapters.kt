package com.unware.mediaapp.ui.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.io.File

@BindingAdapter("imageFromFilePath")
fun bindImageFromPath(view: ImageView, imagePath: String?) {
    if (!imagePath.isNullOrEmpty()) {
        Glide.with(view.context)
                .load(File(imagePath))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
    }
}