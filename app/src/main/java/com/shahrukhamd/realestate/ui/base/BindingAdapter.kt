package com.shahrukhamd.realestate.ui.base

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

@BindingAdapter(value = ["loadImage", "placeholder"], requireAll = false)
fun ImageView.loadImage(url: String?, placeholder: Drawable?) {
    Glide.with(context).load(url).also { glide ->
        placeholder?.let {
            glide.placeholder(it)
            glide.error(it)
        }
    }.into(this)
}