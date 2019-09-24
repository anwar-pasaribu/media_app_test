package com.unware.mediaapp.ui.gallerydetail

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.unware.mediaapp.databinding.GalleryDetailFragmentBinding


class GalleryDetailFragment : Fragment() {

    private val args: GalleryDetailFragmentArgs by navArgs()
    private lateinit var binding: GalleryDetailFragmentBinding

    private lateinit var viewModel: GalleryDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = GalleryDetailFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.gallery = args.gallery

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GalleryDetailViewModel::class.java)


        binding.floatingActionButton.setOnClickListener {

            binding.gallery?.largeImageURL?.let { largeWebUrl ->

                val filename = URLUtil.guessFileName(
                    largeWebUrl,
                    System.currentTimeMillis().toString(),
                    getExtensionFromPath(largeWebUrl)
                )
                val request = DownloadManager.Request(
                    Uri.parse(largeWebUrl)
                )

                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)  // Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
                val dm =
                    requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                dm.enqueue(request)

            }


        }
    }

    fun getExtensionFromPath(imagePath: String): String {
        return imagePath.substring(imagePath.lastIndexOf(".") + 1, imagePath.length)
    }

}
