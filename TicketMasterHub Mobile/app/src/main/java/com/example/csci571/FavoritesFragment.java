package com.example.csci571;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;

    private RelativeLayout recyclerFrameLayout;

    private LinearLayout noFavoritesLayout;

    public FavoritesFragment() {
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
        Log.i("chummm","hererererererer");



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        // Inflate the layout for this fragment

        Log.i("chummm","hererererererer");
        // Get the shared preferences object
          return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noFavoritesLayout = view.findViewById(R.id.no_favorites_layout);
        recyclerFrameLayout = view.findViewById(R.id.recyclerFrameLayout1);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyEvents", getActivity().MODE_PRIVATE);

// Retrieve the list of event details from shared preferences
        String serializedList = sharedPreferences.getString("eventList", "");
        List<EventModel.EventDetails> eventList = new ArrayList<>();;

// If the list is not empty, deserialize it using ObjectInputStream
        if (!serializedList.isEmpty()) {
            byte[] bytes = Base64.decode(serializedList, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                eventList = (List<EventModel.EventDetails>) objectInputStream.readObject();
                Log.i("rummm",eventList.toString());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();

            }
        } else {
            // If the list is empty, create a new list
            eventList = new ArrayList<>();
        }
        for (EventModel.EventDetails event : eventList) {
            Log.i("rummm",eventList.toString());
            System.out.println("Event Name: " + event.getEventName());
            System.out.println("Event Date: " + event.getEventDate());
            System.out.println("Event Location: " + event.getEventLocation());
            System.out.println("Event Time: " + event.getEventTime());
            System.out.println("Event Category: " + event.getEventCategory());
            System.out.println("Event Image URL: " + event.getEventImageUrl());
            System.out.println("Event ID: " + event.getEventId());
            System.out.println("------------------------");
        }

        Log.i("jummmmm",Integer.toString(eventList.size()));
        if(eventList.size()==0){
            Log.i("ZUMMMMMM"," ");
            noFavoritesLayout.setVisibility(View.VISIBLE);
            recyclerFrameLayout.setVisibility(View.GONE);
            return;
        }
        else{
            Log.i("LUMMMMMM",eventList.toString());
            noFavoritesLayout.setVisibility(View.GONE);
            recyclerFrameLayout.setVisibility(View.VISIBLE);
        }


        // Register a listener for shared preference changes
        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


                if (key.equals("eventList")) {
                    String serializedList = sharedPreferences.getString("eventList", "");
                    List<EventModel.EventDetails> updatedList = new ArrayList<>();

                    List<SearchResult> searchResults = new ArrayList<>();
                    recyclerFrameLayout = view.findViewById(R.id.recyclerFrameLayout1);

//                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyEvents", getActivity().MODE_PRIVATE);

// Retrieve the list of event details from shared preferences
                    List<EventModel.EventDetails> eventList = new ArrayList<>();;

// If the list is not empty, deserialize it using ObjectInputStream
                    if (!serializedList.isEmpty()) {
                        byte[] bytes = Base64.decode(serializedList, Base64.DEFAULT);
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                        try {
                            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                            eventList = (List<EventModel.EventDetails>) objectInputStream.readObject();
                            Log.i("rummm",eventList.toString());
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();

                        }
                    } else {
                        // If the list is empty, create a new list
                        Log.d("opopopopopo",Integer.toString(serializedList.length()));
                        eventList = new ArrayList<>();
                    }
                    // Retrieve the updated list of event details from shared preferences



                    recyclerView = (RecyclerView) recyclerFrameLayout.findViewById(R.id.favoritesRecyclerView);

                    Log.i("remiiii",searchResults.size()+ " "+ recyclerView.toString());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    FragmentRecyclerViewAdapter adapter;


                    adapter = new FragmentRecyclerViewAdapter(new ArrayList<SearchResult>(searchResults));
                    recyclerView.setAdapter(adapter);
                    Log.i("FAVORRRR","NOT NULL");
                    adapter.setData(searchResults);
                    adapter.notifyDataSetChanged();
                    displayData(eventList,view);
                }
            }
        };

        // Register the listener with the shared preferences
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

// Do something with the list of event details
// For example, pass it to an adapter and display it in a RecyclerView

        displayData(eventList,view);

    }

        public void displayData(List<EventModel.EventDetails> eventList, View view){
        List<SearchResult> searchResults = new ArrayList<>();

        recyclerView = (RecyclerView) recyclerFrameLayout.findViewById(R.id.favoritesRecyclerView);

        Log.i("gemiiii",recyclerView.toString());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FragmentRecyclerViewAdapter adapter;
        String EventName =  "";
        String EventDate = "";
        String EventLocation = "";
        String EventTime = "";
        String EventCategory = "";
        String EventImageURL = "";
        String EventID = "";
        for (EventModel.EventDetails event : eventList) {
            EventName =  event.getEventName();
             EventDate = event.getEventDate();
             EventLocation = event.getEventLocation();
             EventTime = event.getEventTime();
             EventCategory = event.getEventCategory();
             EventImageURL = event.getEventImageUrl();
             EventID = event.getEventId();
            SearchResult searchResult = new SearchResult(EventName, EventDate, EventLocation, EventTime, EventCategory, EventImageURL, true);
            searchResult.setId(EventID);
            searchResults.add(searchResult);
        }

        for(SearchResult sr: searchResults){
            Log.i("srsrsr",sr.getCategory());
        }
            if(eventList.size()==0){
                Log.i("ZUMMMMMM"," ");
                noFavoritesLayout.setVisibility(View.VISIBLE);
                recyclerFrameLayout.setVisibility(View.GONE);
                return;
            }
            else{
                Log.i("LUMMMMMM",eventList.toString());
                noFavoritesLayout.setVisibility(View.GONE);
                recyclerFrameLayout.setVisibility(View.VISIBLE);
            }

        adapter = new FragmentRecyclerViewAdapter(new ArrayList<SearchResult>(searchResults));
        recyclerView.setAdapter(adapter);
        Log.i("FAVORRRR","NOT NULL");
        adapter.setData(searchResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        onViewCreated(getView(), null);
    }
}