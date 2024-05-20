package com.example.prototype_mapbox

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
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
    lateinit var permissionsManager: PermissionsManager

    override fun onCreate(savedInstanceState: Bundle?) {
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
//        enableEdgeToEdge()
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
        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(-98.0, 39.5))
                .pitch(0.0)
                .zoom(2.0)
                .bearing(0.0)
                .build()
        )
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

        // Follows the user device location or centers it on start. I'm not sure if this is following or initially setting.
        mapView.location.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
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
                .zoom(14.0)
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