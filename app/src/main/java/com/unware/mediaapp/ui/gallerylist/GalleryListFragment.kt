package com.unware.mediaapp.ui.gallerylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.unware.mediaapp.R
import com.unware.mediaapp.model.RequestStatus
import kotlinx.android.synthetic.main.gallery_list_fragment.*

class GalleryListFragment : Fragment() {

    private lateinit var viewModel: GalleryListViewModel
    private lateinit var adapter: GalleryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.gallery_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GalleryListViewModel::class.java)

        adapter = GalleryListAdapter(object : GalleryListAdapter.OnGalleryItemSelectedListener {
            override fun onLoadMoreRetryRequested(position: Int) {
                viewModel.retry()
            }

        })
        rv_gallery_list_fragment.adapter = adapter

        srl_gallery_list_fragment.setOnRefreshListener {
            viewModel.refresh()
        }

        observeGalleryListData()
    }

    private fun observeGalleryListData() {

        viewModel.initialState.observe(viewLifecycleOwner, Observer {
            srl_gallery_list_fragment.isRefreshing = it.state == RequestStatus.LOADING
        })

        // Observe Actual Paged List
        viewModel.galleryList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        // Paging / Load More Indicator
        viewModel.loadMoreState.observe(viewLifecycleOwner, Observer {
            adapter.setNetworkState(it)
        })

    }

}
