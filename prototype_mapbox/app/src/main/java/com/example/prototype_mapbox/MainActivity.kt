package com.example.prototype_mapbox

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location

class MainActivity : ComponentActivity() {
    private lateinit var mapView: MapView
    private lateinit var permissionsManager: PermissionsManager

    /**
     * FusedLocationClient to get the user's current location for centering camera.
     * MapBox has this functionality built-in, but I couldn't figure out how to do it without
     * listeners.
     *
     * Could add button to "follow my location" to attach/detach
     *
     * https://developer.android.com/develop/sensors-and-location/location/retrieve-current
     */
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * https://developer.android.com/reference/android/util/Log
     */
    private val logTag = "PROTOTYPE_MAPBOX_LOG"

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(logTag, "HELLO WORLD!")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // https://docs.mapbox.com/android/maps/guides/user-location/permissions/
        // prompts the user for location
        // copied from:
        // https://github.com/mapbox/mapbox-maps-android/blob/v11.3.1/app/src/main/java/com/mapbox/maps/testapp/utils/LocationPermissionHelper.kt
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Permission sensitive logic called here, such as activating the Maps SDK's LocationComponent to show the device's location
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {

                override fun onExplanationNeeded(permissionsToExplain: List<String>) {
                    val message = "You need to accept location permissions."
                    val toast = Toast.makeText(
                        applicationContext,
                        message,
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }

                override fun onPermissionResult(granted: Boolean) {
//                    activityRef.get()?.let {
//                        if (granted) {
//                            onMapReady()
//                        } else {
//                            it.finish()
//                        }
//                    }
                }
            })
            permissionsManager.requestLocationPermissions(this)
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Retrieves the mapbox token from developer-config.xml resources
        MapboxOptions.accessToken = getString(R.string.mapbox_access_token_public)
//        setContent {
////            Prototype_mapboxTheme {
////                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
////                    Greeting(
////                            name = "Android",
////                            modifier = Modifier.padding(innerPadding)
////                    )
////                }
////            }
//            MapboxMap(
//                Modifier.fillMaxSize(),
//                mapViewportState = MapViewportState().apply {
//                    setCameraOptions {
//                        zoom(2.0)
//                        center(Point.fromLngLat(-98.0, 39.5))
//                        pitch(0.0)
//                        bearing(0.0)
//                    }
//                },
//                style = { Style.MAPBOX_STREETS }
//            )
//        }

        // Create a map programmatically and set the initial camera
        mapView = MapView(this)
        mapView.mapboxMap.loadStyle(Style.MAPBOX_STREETS)
        // Add the map view to the activity (you can also add it to other views as a child)
        setContentView(mapView)
        // Add the user device location puck
        with(mapView) {
            location.locationPuck = createDefault2DPuck(withBearing = false)
            location.enabled = true
            // adds the little arrow on the puck
//            location.puckBearing = PuckBearing.COURSE
            // makes the camera follow the device location
//            viewport.transitionTo(
//                targetState = viewport.makeFollowPuckViewportState(),
//                transition = viewport.makeImmediateViewportTransition()
//            )
        }

//        val locationProvider = mapView.location.getLocationProvider() as DefaultLocationProvider
        // Android Studio pulled this in to check for permissions before calling
        // fusedLocationClient.getLastLocation()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Center the camera on the user's location
            fusedLocationClient.lastLocation.addOnSuccessListener {
                mapView.mapboxMap.setCamera(
                    CameraOptions.Builder()
                        //                .center(Point.fromLngLat(-98.0, 39.5))
                        .center(Point.fromLngLat(it.longitude, it.latitude))
                        .pitch(0.0)
                        .zoom(11.5)
                        .bearing(0.0)
                        .build()
                )

                Log.d(logTag, "it.longitude: ${it.longitude}, it.latitude: ${it.latitude}")
            }
        } else {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return
        }


        // Follows the user device location or centers it on start. I'm not sure if this is following or initially setting.
//        mapView.location.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
//        mapView.location.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
    }

    // https://docs.mapbox.com/android/maps/guides/user-location/permissions/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // https://docs.mapbox.com/android/maps/guides/camera-and-animation/camera/#set-after-map-initialization
    // Set camera based on device location
    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .zoom(12.0)
                .center(it)
                .build()
        )
        mapView.gestures.focalPoint = mapView.mapboxMap.pixelForCoordinate(it)
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    Prototype_mapboxTheme {
//        Greeting("Android")
//    }
//}