package com.example.prototype_mapbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style

class MainActivity : ComponentActivity() {
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
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