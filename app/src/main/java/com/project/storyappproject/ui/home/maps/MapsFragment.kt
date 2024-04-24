package com.project.storyappproject.ui.home.maps

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.project.storyappproject.R
import com.project.storyappproject.databinding.FragmentMapsBinding

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private val mapsViewModel: MapsViewModel by activityViewModels()
    private var isMapLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (!isMapLoaded) {
            showLoading(true)
        }

        return root
    }

    override fun onResume() {
        super.onResume()

        reloadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun reloadData() {
        mapsViewModel.listStories.observe(viewLifecycleOwner) { stories ->
            mapsViewModel.listStories.removeObservers(viewLifecycleOwner)

            for (story in stories) {
                val location = LatLng(story.lat, story.lon)
                addCustomMarkerFromURL(story.photoUrl, location, story.name, story.description)
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        try {
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(), R.raw.map_style
                )
            )
            if (!success) {
                Log.e("MapsFragmentStyle", "Failed Load Style")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("MapsFragmentStyle", "Style Not Found", e)
        }

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mapsViewModel.listStories.observe(viewLifecycleOwner) { stories ->
            for (story in stories) {
                val location = LatLng(story.lat, story.lon)
                addCustomMarkerFromURL(story.photoUrl, location, story.name, story.description)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
            }
            showLoading(false)
            isMapLoaded = true
        }
    }

    private fun addCustomMarkerFromURL(url: String, location: LatLng, title: String, snippet: String) {
        Glide.with(requireContext())
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val scaledBitmap = Bitmap.createScaledBitmap(resource, 100, 100, false)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(location)
                            .icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap))
                            .title(title)
                            .snippet(snippet)
                    )
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    mMap.addMarker(
                        MarkerOptions()
                            .position(location)
                            .title(title)
                            .snippet(snippet)
                    )
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(location)
                            .title(title)
                            .snippet(snippet)
                    )
                }
            })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingDescTv.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
            binding.mapContainer.visibility = View.INVISIBLE
        } else {
            binding.loadingDescTv.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.INVISIBLE
            binding.mapContainer.visibility = View.VISIBLE
        }
    }
}