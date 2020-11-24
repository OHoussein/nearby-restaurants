package dev.ohoussein.restos.ui.feature.venues.fragment

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mapbox.android.core.location.*
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import dagger.hilt.android.AndroidEntryPoint
import dev.ohoussein.restos.R
import dev.ohoussein.restos.databinding.FragmentMapBinding
import dev.ohoussein.restos.ui.core.model.UiResource
import dev.ohoussein.restos.ui.core.model.UiVenue
import dev.ohoussein.restos.ui.core.util.MapUtils.toLatLng
import dev.ohoussein.restos.ui.core.util.MapUtils.toUiCoordinates
import dev.ohoussein.restos.ui.feature.venues.viewmodel.NearbyRestaurantsViewModel
import dev.ohoussein.restos.ui.feature.venues.viewmodel.ShareRestaurantsViewModel
import timber.log.Timber

@AndroidEntryPoint
@SuppressWarnings("TooManyFunctions")
class RestaurantsMapFragment : MapBoxFragment() {

    companion object {
        const val DEFAULT_INTERVAL_IN_MILLISECONDS = 60L
        const val DEFAULT_ZOOM_ON_LOCATION = 16.0
        const val RESTAURANT_ICON_NAME = "restaurant-15"
        const val RESTAURANT_ICON_SIZE = 2F
    }

    private var _binding: FragmentMapBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    private var mapboxMap: MapboxMap? = null
    private var locationEngine: LocationEngine? = null
    private var lastLocation: Location? = null
    private var locationUpdateListener: LocationEngineCallback<LocationEngineResult>? = null
    private val askLocationPermission =
            registerForActivityResult(
                    ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    Timber.d("Permission granted")
                    mapboxMap?.getStyle {
                        enableLocationComponentWithPermission(it)
                    }
                } else {
                    showRationaleDialogForLocationPermission()
                }
            }
    private var symbolManager: SymbolManager? = null

    private val viewModel: NearbyRestaurantsViewModel by viewModels()
    private val sharedViewModel: ShareRestaurantsViewModel by activityViewModels()
    private val dicSymbolVenue = mutableMapOf<Symbol, UiVenue>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        symbolManager?.onDestroy()
        symbolManager = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.mapView

        mapView?.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap

            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                enableLocationComponentWithPermission(style)
                symbolManager = initSymbloManager(binding.mapView, mapboxMap, style)
                observeData()
            }
            mapboxMap.addOnCameraMoveListener {
                mapboxMap.cameraPosition.padding

                val newCoordinates = mapboxMap.projection.visibleRegion.latLngBounds.toUiCoordinates()
                viewModel.updateViewPort(newCoordinates)
                Timber.d("bounds: $newCoordinates")
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // MapBox
    ///////////////////////////////////////////////////////////////////////////
    private fun enableLocationComponentWithPermission(loadedMapStyle: Style) {
        when {
            checkSelfPermission(requireContext(), locationPermission) == PermissionChecker.PERMISSION_GRANTED -> {
                Timber.d("Location permission enabled")
                enableLocationComponent(loadedMapStyle)
            }
            shouldShowRequestPermissionRationale(locationPermission) -> {
                showRationaleDialogForLocationPermission()
            }
            else -> {
                askLocationPermission.launch(locationPermission)
            }
        }
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun enableLocationComponent(loadedMapStyle: Style) {
        val context = requireContext()
        mapboxMap?.locationComponent?.let { locationComponent ->
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions
                            .builder(context, loadedMapStyle)
                            .build()
            )

            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            initLocationEngine()
        }
    }

    private fun createLocationUpdateListener() = object : LocationEngineCallback<LocationEngineResult> {
        override fun onSuccess(result: LocationEngineResult) {
            result.lastLocation?.let {
                if (lastLocation == null)
                    zoomTo(it)
                lastLocation = it
            }
        }

        override fun onFailure(exception: Exception) {
            Timber.e(exception, "Error durring getting use location")
        }

    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(requireContext())
        val request = LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .build()
        val locationListener = createLocationUpdateListener()
        locationEngine?.requestLocationUpdates(request, locationListener, Looper.getMainLooper())
        locationEngine?.getLastLocation(locationListener)
        this.locationUpdateListener = locationListener
    }

    private fun initSymbloManager(mapView: MapView, mapboxMap: MapboxMap, style: Style): SymbolManager {
        return SymbolManager(mapView, mapboxMap, style)
                .apply {
                    addClickListener {
                        val venue = dicSymbolVenue[it] ?: return@addClickListener false
                        onSelectVenue(venue)
                        return@addClickListener true
                    }
                }
    }

    override fun onStop() {
        super.onStop()
        locationUpdateListener?.let {
            locationEngine?.removeLocationUpdates(it)
        }
    }

    private fun zoomTo(target: Location) {
        mapboxMap?.cameraPosition = CameraPosition.Builder()
                .zoom(DEFAULT_ZOOM_ON_LOCATION)
                .target(LatLng(target.latitude, target.longitude))
                .build()
    }


    private fun showRationaleDialogForLocationPermission() {
        AlertDialog.Builder(requireContext())
                .setTitle(R.string.permission_location_permission_title)
                .setMessage(R.string.permission_location_permission_message)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    askLocationPermission.launch(locationPermission)
                }
                .create()
                .show()
    }


    private fun addVenueToMap(uiVenue: UiVenue) {
        symbolManager?.let { symbolManager ->
            val symbol = symbolManager.create(
                    SymbolOptions()
                            .withLatLng(uiVenue.location.coordinates.toLatLng())
                            .withIconImage(RESTAURANT_ICON_NAME)
                            .withIconSize(RESTAURANT_ICON_SIZE)
            )
            dicSymbolVenue[symbol] = uiVenue
        }
    }

    private fun showVenuesOnMap(data: List<UiVenue>) {
        symbolManager?.delete(dicSymbolVenue.keys.toList())
        dicSymbolVenue.clear()
        data.forEach { uiVenue ->
            addVenueToMap(uiVenue)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // data
    ///////////////////////////////////////////////////////////////////////////

    private fun observeData() {
        sharedViewModel.selectedVenue.observe(viewLifecycleOwner, { venue: UiVenue? ->
            Timber.d("Selected venue ${venue?.name}")
        })

        viewModel.restaurantList.observe(viewLifecycleOwner, { resource ->
            Timber.d("Resource state  ${resource.javaClass.simpleName}")
            when (resource) {
                is UiResource.Success -> {
                    showVenuesOnMap(resource.data)
                }
                is UiResource.Loading -> {
                    if (resource.data != null) {
                        showVenuesOnMap(resource.data)
                    }
                }
            }
        })
    }

    private fun onSelectVenue(venue: UiVenue) {
        sharedViewModel.selectedVenue.value = venue
    }

}
