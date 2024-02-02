package com.example.csci571;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class VenueFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private  WebView mWebView;
    private WebSettings webSettings;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String venue_name;

    private LatLng mLatLng;

    private GoogleMap mMap;

    private MapView mapView;

    private MapView mMapView;
    private TextView mOpenHoursTextView;
    private TextView mGeneralRulesTextView;
    private TextView mChildRulesTextView;



    public VenueFragment() {
        // Required empty public constructor
    }

    private void toggleEllipsize(TextView textView) {
        if (textView.getLayout() != null && textView.getLayout().getEllipsisCount(textView.getLineCount() - 1) > 0) {
            textView.setMaxLines(Integer.MAX_VALUE);
            textView.setEllipsize(null);
//            isEllipsized = false;
        } else {
            textView.setMaxLines(3);
            textView.setEllipsize(TextUtils.TruncateAt.END);
//            isEllipsized = true;
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();





        View view = inflater.inflate(R.layout.fragment_venue, container, false);


        // Get the MapView from the layout
//        mMapView = (MapView) view.findViewById(R.id.map_view);
//
//        // Initialize the MapView
//        mMapView.onCreate(savedInstanceState);

//        https://csci571-210323.wl.r.appspot.com/eventdetails?input=vvG1IZ9pNeVNoR
        Intent intent = getActivity().getIntent();
        Bundle event = intent.getExtras();
        String venue_name = event.getString("venue_name");
//        getEventDetails(eventId);
        this.venue_name = venue_name;
        return inflater.inflate(R.layout.fragment_venue, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        WebView mapWebView = view.findViewById(R.id.mapWebView);
        String apiKey = "AIzaSyD383pD67sfa7BvAOmBx2cnkiA2KKwum7o";
        String lat = "37.7749";
        String lng = "-122.4194";

        mWebView = view.findViewById(R.id.mapWebView);
        webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

//
//        mapWebView.getSettings().setJavaScriptEnabled(true);
//        mapWebView.loadData("<html><body><div id=\"map\" style=\"width: 100%; height: 100%;\"></div>\n" +
//                "<script async defer src=\"https://maps.googleapis.com/maps/api/js?key=" + apiKey + "&callback=initMap\"></script>\n" +
//                "<script>\n" +
//                "function initMap() {\n" +
//                "var mapDiv = document.getElementById('map');\n" +
//                "var map = new google.maps.Map(mapDiv, {\n" +
//                "center: {lat: " + lat + ", lng: " + lng + "},\n" +
//                "zoom: 8\n" +
//                "});\n" +
//                "var marker = new google.maps.Marker({\n" +
//                "position: {lat: " + lat + ", lng: " + lng + "},\n" +
//                "map: map,\n" +
//                "title: 'Marker title'\n" +
//                "});\n" +
//                "}\n" +
//                "</script></body></html>", "text/html", "utf-8");


        // Get the GoogleMap object
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap map) {
//                mMap = map;
//
//
//                // Add a marker
//                LatLng location = new LatLng(37.7749, -122.4194);
//                MarkerOptions markerOptions = new MarkerOptions()
//                        .position(location)
//                        .title("San Francisco");
//                mMap.addMarker(markerOptions);
//            }
//        });
        // Call the API to get event details
        try {
            getVenueDetails(this.venue_name);

//            mMapView = view.findViewById(R.id.map_view);
//       mMapView.onCreate(savedInstanceState);



        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void getVenueDetails(String venue_name) throws UnsupportedEncodingException {
        // Create the URL for the API endpoint
        String vname = URLEncoder.encode(venue_name, "UTF-8");
        String url = "https://csci571-210323.wl.r.appspot.com/venuedetails?input="+venue_name;

        // Set up the POST request body with the event ID parameter

        Log.d("PAIIIII", "API response: " + url);
        // Make the POST request with Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject res) {
                        // Handle the API response
                        Log.d("PAIIIII", "API response: " + res.toString());

                        try {
                            JSONObject response = res.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);
                            // Parse the JSON response to get the detail
//                            String time = response.getString("time");
//                            String genres = response.getString("genres");
//                            String priceRange = response.getString("priceRange");
//                            String ticketStatus = response.getString("ticketStatus");
//                            String buyTicketsAt = response.getString("buyTicketsAt");
//                            String imageUrl = response.getString("imageUrl");
                            if(!response.has("boxOfficeInfo") || !response.has("generalInfo")){
                                getView().findViewById(R.id.yellow_venue_bottom).setVisibility(View.GONE);
                            }

                            // Set the details in the fragment
                            setVenueName(response);
                            setVenueAddress(response);
                            setVenueContact(response);
                            setVenueChildRules(response);
                            setVenueOpenHours(response);
                            setVenueGeneralRules(response);
                            setVenueCity(response);
//                            setMap(response); // Check this why not working
//                            setPrice(response);
//                            setStatus(response);
//                            setUrl(response);
//                            setSeatMapImage(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Save the event details to a local variable or database
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the API error
                        Log.e("EEEEEEE", "API error: " + error.toString());
                    }
                });
        Volley.newRequestQueue(getContext()).add(request);
    }


    public void setVenueName(JSONObject response) throws JSONException {
        Log.e("vonme", "in venue: "+response);

        String vname = response.getString("name");

        if (getView() != null) {
            TextView venueTextView = getView().findViewById(R.id.vname_value);
            venueTextView.setText(vname);
//            venueArtistTextView.setSelected(true);
        }
    }

    public void setVenueAddress(JSONObject response) throws JSONException {
        Log.e("vonme", "in venue: "+response);

        String vadd = response.getJSONObject("address").getString("line1");

        if (getView() != null) {
            TextView venueTextView = getView().findViewById(R.id.vaddress_value);
            venueTextView.setText(vadd);
            venueTextView.setSelected(true);
        }
    }

    public void setVenueCity(JSONObject response) throws JSONException {
        Log.e("vonme", "in venue: "+response);

        String city = response.getJSONObject("city").getString("name");
        String state = response.getJSONObject("state").getString("name");

        if (getView() != null) {
            TextView venueTextView = getView().findViewById(R.id.city_value);
            venueTextView.setText(city+", "+state);
            venueTextView.setSelected(true);
        }
    }

    public void setVenueContact(JSONObject response) throws JSONException {
        Log.e("vonme", "in venue: "+response);



        String contact = "";
        if(response.has("boxOfficeInfo")){
            contact = response.getJSONObject("boxOfficeInfo").getString("phoneNumberDetail");
        }

        if(contact==null || contact==""){
            getView().findViewById(R.id.contact_label).setVisibility(View.GONE);
        }

        if (getView() != null) {
            TextView venueTextView = getView().findViewById(R.id.contact_value);
            venueTextView.setText(contact);
            venueTextView.setSelected(true);
        }
    }

    public void setVenueOpenHours(JSONObject response) throws JSONException {
        Log.e("vonme", "in venue: "+response);

        String op ="";
        if(response.has("boxOfficeInfo")){
            op = response.getJSONObject("boxOfficeInfo").getString("openHoursDetail");
        }
        if (getView() != null) {
            TextView venueTextView = getView().findViewById(R.id.open_hours_value);
            venueTextView.setText(op);
//            venueTextView.setSelected(true);
        }
    }

    public void setVenueChildRules(JSONObject response) throws JSONException {
        Log.e("vonme", "in venue: "+response);
        String op ="";
        if(response.has("generalInfo") && response.getJSONObject("generalInfo").has("childRule")){
            op = response.getJSONObject("generalInfo").getString("childRule");
        }
        if (getView() != null) {
            TextView venueTextView = getView().findViewById(R.id.child_rules_value);
            venueTextView.setText(op);
//            venueTextView.setSelected(true);
        }

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                double latitude;
////              double longitude = 0.0;
                double longitude;
                try {
                    latitude = response.getJSONObject("location").getDouble("latitude");
                    longitude = response.getJSONObject("location").getDouble("longitude");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                super.onPageFinished(view, url);
                // Inject JavaScript to add a marker to the map
                mWebView.loadUrl("javascript:initialize(" + latitude + "," + longitude + ")");
            }
        });
        mWebView.loadUrl("file:///android_res/raw/map.html");

//        mMapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                mMap = googleMap;
//                // Add marker to map
//                double latitude;
////              double longitude = 0.0;
//                double longitude;
//                try {
//                    latitude = response.getJSONObject("location").getDouble("latitude");
//                    longitude = response.getJSONObject("location").getDouble("longitude");
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//
//              LatLng venueLocation = new LatLng(latitude, longitude);
//                mMap.addMarker(new MarkerOptions().position(venueLocation).title("Name"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venueLocation, 15));
//            }
//        });

    }

    public void setVenueGeneralRules(JSONObject response) throws JSONException {
        Log.e("vonme", "in venue: "+response);

        String op ="";
        if(response.has("generalInfo")){
            op = response.getJSONObject("generalInfo").getString("generalRule");
        }
        if (getView() != null) {
            TextView venueTextView = getView().findViewById(R.id.general_rules_value);
            venueTextView.setText(op);
//            venueTextView.setSelected(true);
        }

        mGeneralRulesTextView = getView().findViewById(R.id.general_rules_value);
        mChildRulesTextView =  getView().findViewById(R.id.child_rules_value);
        mOpenHoursTextView = getView().findViewById(R.id.open_hours_value);


        mOpenHoursTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEllipsize(mOpenHoursTextView);
            }
        });

        mGeneralRulesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEllipsize(mGeneralRulesTextView);
            }
        });

        mChildRulesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEllipsize(mChildRulesTextView);
            }
        });
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        mMapView.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mMapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mMapView.onLowMemory();
//    }

//    public void setMap(JSONObject response) throws JSONException {
//
////        MapView mapView = getView().findViewById(R.id.venue_map);
//        Log.e("mappppp", "in venue: "+response);
//        SupportMapFragment mapFragment = (SupportMapFragment)  getChildFragmentManager().findFragmentById(R.id.venue_map);
//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                double latitude = 0.0;
//                double longitude = 0.0;
//            mMap = googleMap;
//                try {
//                    latitude = response.getJSONObject("location").getDouble("latitude");
//                    longitude = response.getJSONObject("location").getDouble("longitude");
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
////
//
//            // Add a marker at the venue location
//            if (mLatLng != null) {
//                mMap.addMarker(new MarkerOptions().position(mLatLng).title(venue_name));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
//            }
//        }});
////        double latitude = 0.0;
////        double longitude = 0.0;
////        latitude = response.getJSONObject("location").getDouble("latitude");
////        longitude = response.getJSONObject("location").getDouble("longitude");
////        mLatLng = new LatLng(latitude, longitude);
////        mapView.getMapAsync(new OnMapReadyCallback() {
////            @Override
////            public void onMapReady(GoogleMap googleMap) {
////                // Set the GoogleMap object and add a marker
////                mMap = googleMap;
////                mMap.addMarker(new MarkerOptions().position(mLatLng).title("Venue Location"));
////
////                // Move the camera to the marker and zoom in
////                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
////            }
////        });
//    }
}
