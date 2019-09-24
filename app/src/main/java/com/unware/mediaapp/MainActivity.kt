package com.unware.mediaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.unware.mediaapp.extensions.setupWithNavController
import com.unware.mediaapp.ui.gallerydetail.GalleryDetailFragmentArgs
import com.unware.mediaapp.ui.mediadetail.MediaDetailFragmentArgs

class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    private fun setupBottomNavigationBar() {

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)

        val navGraphIds = listOf(
            R.navigation.navigation_media,
            R.navigation.navigation_gallery
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            println("TOTO cont: re: $navController, cur dest: ${navController?.currentDestination}, cur dest lbl: ${navController?.currentDestination?.arguments}")

            setupActionBarWithNavController(navController)

            navController?.addOnDestinationChangedListener { _, destination, arguments ->
                var toolbarTile = " "
                when (destination.id) {
                    R.id.fragment_media_list -> {
                        toolbarTile = getString(R.string.title_media_list)
                    }
                    R.id.fragment_media_detail -> {
                        arguments?.let {
                            val name = MediaDetailFragmentArgs.fromBundle(it).mediaItem.name
                            toolbarTile = name
                        }
                    }
                    R.id.fragment_gallery_list -> {
                        toolbarTile = getString(R.string.title_gallery_list)
                    }
                    R.id.fragment_gallery_detail -> {
                        arguments?.let {
                            toolbarTile = GalleryDetailFragmentArgs.fromBundle(it).gallery.tags
                        }
                    }
                }
                title = toolbarTile
            }
        })
        currentNavController = controller

    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

}
