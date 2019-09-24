package com.unware.mediaapp.ui.gallerylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.unware.mediaapp.R
import com.unware.mediaapp.databinding.RowGalleryItemBinding
import com.unware.mediaapp.model.Gallery
import com.unware.mediaapp.model.RequestStatus
import kotlinx.android.synthetic.main.row_load_more.view.*

class GalleryListAdapter(
    private val listener: OnGalleryItemSelectedListener
) : PagedListAdapter<Gallery, RecyclerView.ViewHolder>(GALLERY_COMPARATOR) {

    private var networkState: RequestStatus? = null

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {

        return if (hasExtraRow() && position == itemCount - 1) {
            VIEW_TYPE_LOADING
        } else TYPE_GIF_ITEM

    }

    private fun hasExtraRow() = networkState != null
            && networkState!!.state != RequestStatus.FINISHED

    fun setNetworkState(newNetworkState: RequestStatus?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LOADING) {
            LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.row_load_more, parent, false
                )
            )
        } else GalleryItemViewHolder(
            RowGalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == VIEW_TYPE_LOADING) {
            configureLoadMoreView(holder as LoadingViewHolder, position)
        } else {
            val galleryItem = getItem(position) as Gallery
            (holder as GalleryItemViewHolder).bind(galleryItem)
        }


    }

    private fun configureLoadMoreView(holder: LoadingViewHolder, position: Int) {

        if (networkState?.state == RequestStatus.ERROR) {
            holder.vfRowLoadMore?.displayedChild = 1
            holder.parentClickableReload?.setOnClickListener {
                listener.onLoadMoreRetryRequested(position)
            }
        } else {
            holder.vfRowLoadMore?.displayedChild = 0
        }
    }

    companion object {

        const val TYPE_GIF_ITEM = 1
        const val VIEW_TYPE_LOADING = 2

        private val GALLERY_COMPARATOR = object : DiffUtil.ItemCallback<Gallery>() {
            override fun areItemsTheSame(
                oldItem: Gallery,
                newItem: Gallery
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Gallery,
                newItem: Gallery
            ): Boolean = oldItem.id == newItem.id
        }
    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val vfRowLoadMore: ViewFlipper? = itemView.vfRowLoadMore
        val progressBar: ProgressBar? = itemView.pbRowLoadMore
        val parentClickableReload: LinearLayout? = itemView.parentClickableReload
    }

    inner class GalleryItemViewHolder(
        private val binding: RowGalleryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setOpenImage {
                binding.gallery?.let { galleryItem ->
                    openGalleryDetail(galleryItem, it)
                }
            }
        }

        fun bind(galleryItem: Gallery) {
            binding.apply {
                gallery = galleryItem

                // Set Image Aspect Ratio
                // Check image metadata
                if (galleryItem.previewWidth != 0L && galleryItem.previewHeight != 0L) {
                    val dimensionRatio = String.format("%s:%s", galleryItem.previewWidth, galleryItem.previewHeight)
                    val set = ConstraintSet()
                    set.clone(this.parentRowGalleryItem)
                    set.setDimensionRatio(this.ivRowGalleryItem.id, dimensionRatio)
                    set.applyTo(this.parentRowGalleryItem)
                } else {
                    println("IMAGE METADATA NOT FOUND :(")
                }

                executePendingBindings()
            }
        }

        private fun openGalleryDetail(galleryItem: Gallery, view: View) {
            view.findNavController().navigate(
                GalleryListFragmentDirections.actionOpenGalleryDetail(galleryItem)
            )
        }
    }

    interface OnGalleryItemSelectedListener {
        fun onLoadMoreRetryRequested(position: Int)
    }
}