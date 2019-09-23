package com.unware.mediaapp.ui.gallerylist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.unware.mediaapp.R

class GalleryListFragment : Fragment() {

    companion object {
        fun newInstance() = GalleryListFragment()
    }

    private lateinit var viewModel: GalleryListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.gallery_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GalleryListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
