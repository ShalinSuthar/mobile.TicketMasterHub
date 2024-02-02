package com.example.csci571;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bumptech.glide.Glide;

public class DetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String urlBig;

    private TextView eventIdTextView;

    private String event_id;

    private LinearLayout dtPg;

    private FrameLayout dtFrame;

    private String getUrl(){
        return this.urlBig;
    }

    private boolean isFavEvent = false;

    public DetailsFragment() {
        // Required empty public constructor
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

//        https://csci571-210323.wl.r.appspot.com/eventdetails?input=vvG1IZ9pNeVNoR
        Intent intent = getActivity().getIntent();
        Bundle event = intent.getExtras();
        String eventId = event.getString("event_id");
        isFavEvent = event.getBoolean("is_favorite_event");
//        getEventDetails(eventId);
        this.event_id = eventId;


        String eventName = event.getString("event_name");
        Log.i("maxxxx: " ,""+eventName );
        TextView eventText = getActivity().findViewById(R.id.second_event_name);
        eventText.setText(eventName);
        eventText.setSelected(true);
//        if (bundle != null) {
//            Log.i("Eventtttta: " ,""+bundle);
//            String eventId = bundle.getString("event_id");
//            Log.i("Eventtttta: " ,""+eventId);
//        }
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        dtPg = view.findViewById(R.id.detailsprogress);
        dtPg.setVisibility(View.VISIBLE);

        // Call the API to get event details
        getEventDetails(this.event_id);
    }

    private void getEventDetails(String eventId) {

//        dtFrame.setVisibility(View.GONE);
//        recyclerFrameLayout.setVisibility(View.GONE);


        // Create the URL for the API endpoint
        String url = "https://csci571-210323.wl.r.appspot.com/eventdetails?input="+eventId;

        // Set up the POST request body with the event ID parameter

        Log.d("PAIIIII", "API response: " + url);
        // Make the POST request with Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        dtPg.setVisibility(View.GONE);
                        // Handle the API response
                        Log.d("PAIIIII", "API response: " + response.toString());
                        try {
                            // Parse the JSON response to get the details
//                            String venueName = response.getString("venueName");
//                            String date = response.getString("date");
//                            String time = response.getString("time");
//                            String genres = response.getString("genres");
//                            String priceRange = response.getString("priceRange");
//                            String ticketStatus = response.getString("ticketStatus");
//                            String buyTicketsAt = response.getString("buyTicketsAt");
//                            String imageUrl = response.getString("imageUrl");

                            // Set the details in the fragment
                            setArtist(response);
                            setVenue(response);
                            setDate(response);
                            setTime(response);
                            setGenres(response);
                            setPrice(response);
                            setStatus(response);
                            setUrl(response);
                            setSeatMapImage(response);


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

    public void setArtist(JSONObject response) throws JSONException {
        Log.e("mineeeee", "in attrection: " );
        String attrList = "";

        JSONArray attractions = response.getJSONObject("_embedded").getJSONArray("attractions");
        String artist = attractions.getJSONObject(0).getString("name");
        Log.e("mineeeee", "API error: " + attractions + attractions.toString());
        if (attractions.length() > 0) {
            for (int i = 0; i < attractions.length(); i++) {
                String name = attractions.getJSONObject(i).getString("name");
                attrList += name + " | ";
            }
            attrList = attrList.substring(0, attrList.length() - 3);
        }
        if (getView() != null) {
            TextView venueArtistTextView = getView().findViewById(R.id.artist_value);
            venueArtistTextView.setText(attrList);
            venueArtistTextView.setSelected(true);
        }
    }

    public void setVenue(JSONObject response) throws JSONException {
        Log.e("venueeee", "in venue: "+response.getJSONObject("_embedded").getJSONArray("venues") );
        String attrList = "";

        String venue  = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
//        String artist = attractions.getJSONObject(0).getString("name");

        if (getView() != null) {
            TextView venueTextView = getView().findViewById(R.id.venue_value);
            venueTextView.setText(venue);
//            venueArtistTextView.setSelected(true);
        }
    }

    public void setDate(JSONObject response) throws JSONException {
        Log.e("venueeee", "in venue: "+response.getJSONObject("_embedded").getJSONArray("venues") );
        String attrList = "";

        String date  = response.getJSONObject("dates").getJSONObject("start").getString("localDate");
//        String artist = attractions.getJSONObject(0).getString("name");

        if (getView() != null) {
            TextView venueTextView = getView().findViewById(R.id.date_value);
            venueTextView.setText(date);
//            venueArtistTextView.setSelected(true);
        }
    }

    public void setTime(JSONObject response) throws JSONException {
        Log.e("venueeee", "in venue: "+response.getJSONObject("_embedded").getJSONArray("venues") );
        String attrList = "";

        String time  = response.getJSONObject("dates").getJSONObject("start").getString("localTime");
//        String artist = attractions.getJSONObject(0).getString("name");

        if (getView() != null) {
            TextView venueTextView = getView().findViewById(R.id.time_value);
            venueTextView.setText(time);
//            venueArtistTextView.setSelected(true);
        }
    }

    public void setGenres(JSONObject response) throws JSONException {
        Log.e("venueeee", "in venue: "+response.getJSONObject("_embedded").getJSONArray("venues") );
        String genreFinal = "";

        JSONArray classifications = response.getJSONArray("classifications");
        JSONObject classification = classifications.getJSONObject(0);

        String genre = classification.has("genre") ? (classification.getJSONObject("genre") != null ? classification.getJSONObject("genre").getString("name") : ""):"";
        String segment = classification.has("segment") ? (classification.getJSONObject("segment") != null ? classification.getJSONObject("segment").getString("name") : "") : "";
        String subGenre = classification.has("subGenre")? (classification.getJSONObject("subGenre") != null ? classification.getJSONObject("subGenre").getString("name") : "") : "";
        String subType = classification.has("subType") ? (classification.getJSONObject("subType") != null ? classification.getJSONObject("subType").getString("name") : "") : "";
        String type = classification.has("type") ? (classification.getJSONObject("type") != null ? classification.getJSONObject("type").getString("name") : "") : "";

        List<String> details = new ArrayList<>();
        details.add(segment);
        details.add(genre);
        details.add(subGenre);
        details.add(type);
        details.add(subType);

        Log.e("genreee", "in venue: "+details.toString() + type.getClass().getSimpleName() + ("Undefined".equalsIgnoreCase(type)));

        List<String> gcList = new ArrayList<>();
        for (String c : details) {
            if (!"Undefined".equalsIgnoreCase(c)) {
                gcList.add(c);
            }
        }
        String gc = TextUtils.join(" | ", gcList);
        genreFinal = gc;



        if (getView() != null) {
            TextView venueTextView = getView().findViewById(R.id.genre_value);
            venueTextView.setText(genreFinal);
//            venueArtistTextView.setSelected(true);
        }
    }

    public void setPrice(JSONObject response) throws JSONException {
        Log.e("", "in venue: "+response.getJSONObject("_embedded").getJSONArray("venues") );
        String attrList = "";
        String min = "";
        String max = "";

        min = response.has("priceRanges") ? (response.getJSONArray("priceRanges").getJSONObject(0).getString("min")):"";
        max  = response.has("priceRanges") ? (response.getJSONArray("priceRanges").getJSONObject(0).getString("max")!= null ? response.getJSONArray("priceRanges").getJSONObject(0).getString("max"): min) : "";
        String curr = response.has("priceRanges") ? (response.getJSONArray("priceRanges").getJSONObject(0).getString("currency")) : "";
//        String artist = attractions.getJSONObject(0).getString("name");

        if (getView() != null) {
            TextView venueTextView = getView().findViewById(R.id.price_value);
            venueTextView.setText(min+" - "+max+" ("+curr+")");
//            venueArtistTextView.setSelected(true);
        }
    }

    public void setStatus(JSONObject response) throws JSONException {

        Map<String, String> statusMapColor = new HashMap<>();
        statusMapColor.put("onsale", "green");
        statusMapColor.put("offsale", "red");
        statusMapColor.put("cancelled", "black");
        statusMapColor.put("postponed", "orange");
        statusMapColor.put("rescheduled", "orange");
        statusMapColor.put("canceled", "black");

        Map<String, String> statusMapText = new HashMap<>();
        statusMapText.put("onsale", "On Sale");
        statusMapText.put("offsale", "Off Sale");
        statusMapText.put("cancelled", "Cancelled");
        statusMapText.put("postponed", "Postponed");
        statusMapText.put("rescheduled", "Rescheduled");
        statusMapText.put("canceled", "Canceled");

        Log.e("", "in venue: "+response.getJSONObject("_embedded").getJSONArray("venues") );
        String attrList = "";

        String status = response.getJSONObject("dates").getJSONObject("status").getString("code");

        if (getView() != null) {
            TextView venueTextView = getView().findViewById(R.id.ticket_value);
            venueTextView.setText(statusMapText.get(status));
//            venueArtistTextView.setSelected(true);
        }
    }


    public void setUrl(JSONObject response) throws JSONException {
        Log.e("venueeee", "in venue: "+response.getJSONObject("_embedded").getJSONArray("venues") );
        String attrList = "";

        String url  = response.getString("url");
//        String artist = attractions.getJSONObject(0).getString("name");
        this.urlBig = url;
        if (getView() != null) {
            TextView venueTextView = getView().findViewById(R.id.buy_value);
            venueTextView.setText(url);
            venueTextView.setSelected(true);
        }

        ImageButton facebookButton = getActivity().findViewById(R.id.facebook_button);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urln = "https://www.facebook.com/sharer/sharer.php?u=" + urlBig.replace(" ", "%20") ;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urln));
                startActivity(intent);
            }
        });

        boolean isFav = this.isFavEvent;
        if(isFav){
            ToggleButton heartButton = getActivity().findViewById(R.id.heart_button);
            heartButton.setButtonDrawable(R.drawable.heart);
        }

        ImageButton twitterButton = getActivity().findViewById(R.id.twitter_button);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urln = "https://twitter.com/intent/tweet?text=" + urlBig.replace(" ", "%20") ;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urln));
                startActivity(intent);
            }
        });
    }

    public void setSeatMapImage(JSONObject response) throws JSONException {
        String attrList = "";

        Log.e("seatmappppp", "in venue: " +response.getJSONObject("seatmap").toString());
        String url  = response.getJSONObject("seatmap").getString("staticUrl");


//        String artist = attractions.getJSONObject(0).getString("name");

        if (getView() != null) {
            ImageView seatmap = getView().findViewById(R.id.seatmap_image);
//            seatmap.setText(url);
            Picasso.get().load(url).into(seatmap);
//            Glide.with(this)
//                    .load(url)
//                    .placeholder(R.drawable.placeholder_event_image)
//                    .into(seatmap);
        }
    }

}
