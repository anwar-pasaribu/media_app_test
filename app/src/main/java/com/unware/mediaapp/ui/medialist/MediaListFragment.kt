package com.unware.mediaapp.ui.medialist

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.unware.mediaapp.databinding.MediaListFragmentBinding
import com.unware.mediaapp.model.Audio


class MediaListFragment : Fragment(), MediaListAdapter.MediaListItemListener {

    companion object {
        private const val REQUEST_PERMISSIONS: Int = 100
    }

    private lateinit var viewModel: MediaListViewModel

    private val adapter by lazy { MediaListAdapter(this) }

    private val IMAGE_LOADER_ID = 1
    private val listOfAllImages = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = MediaListFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.rvAudioVideoFragment.adapter = adapter

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MediaListViewModel::class.java)

        observeAudioFileList()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            beginRequestPermissions()
        else loadFiles()
    }

    private fun loadFiles() {
        viewModel.getAllAudio()
    }

    private fun observeAudioFileList() {
        viewModel.getAudioList().observe(this, Observer {
            it?.let { audioList ->
                println("All audio files: ${audioList.size}")

                adapter.submitList(audioList)
            }
        })
    }

    override fun playMedia(audio: Audio, position: Int) {
        println("Play: $audio, pos: $position")
    }

    private fun beginRequestPermissions() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                && ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                println("Rationale permission request message")
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    REQUEST_PERMISSIONS
                )
            }
        } else {
            println("Permission ")
            loadFiles()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        println("onRequestPermissionsResult: ${grantResults.joinToString()}, permissions: ${permissions.joinToString()}")

        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                var allPermissionGranted = false
                for (i in grantResults.indices) {
                    if (grantResults.isNotEmpty() && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        allPermissionGranted = true
                    } else {
                        allPermissionGranted = false
                        Toast.makeText(
                            requireActivity(),
                            "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                if (allPermissionGranted) loadFiles()
            }
        }
    }
}
