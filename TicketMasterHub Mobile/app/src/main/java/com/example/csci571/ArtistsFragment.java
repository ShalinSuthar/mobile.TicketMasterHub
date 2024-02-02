package com.example.csci571;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ArtistsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout atPg;

    private RecyclerView recyclerView;

    private boolean isDataLoaded = false;

    private static int count = 0;

    private RelativeLayout recyclerFrameLayout;

    public boolean isMusicEvent;

    public String[] attractionsList;

    private LinearLayout noArtistLayout;
    public ArrayList<Artist> artistsBig = new ArrayList<Artist>();

    public void addArtist(Artist a){
        this.artistsBig.add(a);
        Log.i("OOOOOO",Integer.toString(this.artistsBig.size()));
    }

    public ArrayList<Artist> getArtist(){
        return this.artistsBig;
    }
    public ArtistsFragment() {
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        count = 0;
        noArtistLayout = view.findViewById(R.id.no_artist_layout);
        atPg = view.findViewById(R.id.artistprogress);
        atPg.setVisibility(View.VISIBLE);

//        this.artistsBig = new ArrayList<Artist>();
        recyclerFrameLayout = view.findViewById(R.id.recyclerFrameLayout2);
        if(!this.isMusicEvent){
                atPg.setVisibility(View.GONE);
                noArtistLayout.setVisibility(View.VISIBLE);

               return;
        }


        for (String artistName : this.attractionsList) {

            // Construct API URL with artist name as parameter
            String apiUrl = "https://csci571-210323.wl.r.appspot.com/getArtistSpotify?artist=" + artistName.replace(" ", "%20");

            // Make API call using Volley library
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {


                                ArrayList<Artist> aList = new ArrayList<Artist>();
                                String artistID = response.getString("id");
                                String albumsUrl = "https://csci571-210323.wl.r.appspot.com/getArtistAlbums?id=" + artistID;

                                // Make API call for artist albums using Volley library
                                JsonObjectRequest albumsRequest = new JsonObjectRequest(Request.Method.GET, albumsUrl, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response1) {
                                                Log.d("Albums Response", response1.toString());

                                                atPg.setVisibility(View.GONE);

                                                try {
                                                    String artistImageUrl = response.getJSONArray("images").getJSONObject(0).getString("url");
                                                    String artistName = response.getString("name");
                                                    int followers = response.getJSONObject("followers").getInt("total");
                                                    String spotifyUrl = response.getJSONObject("external_urls").getString("spotify");
                                                    int popularity = response.getInt("popularity");
                                                    List<String> albumsUrl = new ArrayList<String>();
                                                    String image1Url  = response1.getJSONArray("items").getJSONObject(0).getJSONArray("images").getJSONObject(0).getString("url");
                                                    String image2Url  = response1.getJSONArray("items").getJSONObject(1).getJSONArray("images").getJSONObject(0).getString("url");
                                                    String image3Url  = response1.getJSONArray("items").getJSONObject(2).getJSONArray("images").getJSONObject(0).getString("url");


                                                    albumsUrl.add(image1Url);
                                                    albumsUrl.add(image2Url);
                                                    albumsUrl.add(image3Url);
                                                    Artist arist = new Artist(artistImageUrl, artistName, followers, spotifyUrl, popularity, albumsUrl);
                                                    Log.i("sheeeeee",artistName);
                                                    aList.add(arist);
                                                    addArtist(arist);
//                                                    artistsBig.add(arist);
                                                    count+=1;
                                                    Log.i("jajajaja",Integer.toString(count) +" "+ Integer.toString(attractionsList.length) );
                                                    if(count== attractionsList.length){
                                                        Log.i("lalala",Integer.toString(aList.size()));
                                                        notifyDataReady(aList);
                                                    }
//                                                    latch.countDown();
                                                } catch (JSONException e) {
                                                    throw new RuntimeException(e);

                                                }


                                            }

                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e("Albums Error", error.getMessage(), error);
                                            }
                                        });

                                // Add the albums API request to the Volley request queue
                                Volley.newRequestQueue(getContext()).add(albumsRequest);

//                                recyclerFrameLayout = getActivity().findViewById(R.id.recyclerFrameLayout2);


                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            Log.i("chullu",""+response.toString());
                            // Parse response and create new Artist object
//                            Artist artist = parseArtistResponse(response);
//
//                            // Add Artist object to ArrayList in ArtistAdapter
//                            artistAdapter.addArtist(artist);
//
//                            // Notify adapter that data set has changed
//                            artistAdapter.notifyDataSetChanged();




                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ArtistFragment", "API error: " + error.getMessage());
                        }
                    });

            // Add request to Volley request queue
            VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
        }

    }

    public void notifyDataReady(ArrayList<Artist> aList){
        recyclerView = (RecyclerView) recyclerFrameLayout.findViewById(R.id.rv_artists);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArtistAdapter adapter;

        ArrayList<Artist> min = this.getArtist();

        Log.i("milli", Integer.toString(min.size()));

//        Log.i("ARTTTTTTTT",Integer.toString(artistList.size()));
        adapter = new ArtistAdapter(new ArrayList<Artist>(min));
        recyclerView.setAdapter(adapter);

        adapter.setData(min);
        adapter.notifyDataSetChanged();

    }
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Intent intent = getActivity().getIntent();
        Bundle event = intent.getExtras();
        boolean is_music_event = event.getBoolean("is_music_event");

        String[] attrList = event.getStringArray("attraction_list");

//        getEventDetails(eventId);
        this.isMusicEvent = is_music_event;
        this.attractionsList = attrList;
        Log.i("Eventtttta: " ,""+this.isMusicEvent +this.attractionsList);
        List<Artist> artistList = new ArrayList<>();






        return inflater.inflate(R.layout.fragment_artists, container, false);
    }



//    public <Artist> parseArtistResponse(JSONObject response){
//
//    }
}
