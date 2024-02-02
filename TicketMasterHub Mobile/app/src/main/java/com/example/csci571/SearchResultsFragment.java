package com.example.csci571;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.csci571.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private List<SearchResult> searchResults;



    public SearchResultsFragment() {
        // Required empty public constructor
    }

    public SearchResultsFragment(List<SearchResult>searchResults){
        this.searchResults = searchResults;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultsFragment newInstance(String param1, String param2) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_search_results, container, false);
//        recyclerView = view.findViewById(R.id.recyclerView);
//        adapter = new RecyclerViewAdapter(new ArrayList<SearchResult>());
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        return view;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i("uuuuu","");
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter(new ArrayList<SearchResult>(searchResults));
        recyclerView.setAdapter(adapter);
        Log.i("ON CREATE","NOT NULL");
//        populateRecyclerView(searchResults);
        adapter.setData(searchResults);

    }

    public void setDataa(){
        adapter.setData(searchResults);
    }

    //    public void populateRecyclerView(JSONObject response) {
//        Log.d("inserach","Search API response" + response);
//        try {
//            // Get the JSON array containing search results
//            JSONArray resultsArray = response.getJSONArray("events");
//
//            Log.d("inserach","Search API response" + resultsArray);
//            // Create a list to hold search results
//            List<SearchResult> searchResults = new ArrayList<>();
//
//            // Loop through the JSON array and extract information for each search result
//            for (int i = 0; i < resultsArray.length(); i++) {
//                JSONObject result = resultsArray.getJSONObject(i);
//
//                // Extract the required information for each search result
////                String title = result.getString("title");
////                String venue = result.getString("venue");
////                String date = result.getString("date");
//                String title = "title";
//                String venue ="venue";
//                String date = "date";
//
//                // Create a new SearchResult object with the extracted information
//                SearchResult searchResult = new SearchResult(title, venue, date);
//
//                // Add the SearchResult object to the list
//                searchResults.add(searchResult);
//            }
//
//            Log.d("empty","inn here" + searchResults);
//            // Set the adapter for the RecyclerView with the list of search results
//            adapter = new RecyclerViewAdapter(searchResults);
//            recyclerView.setAdapter(adapter);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    public void populateRecyclerView(JSONObject response) {
        try {
            // Get the JSON array containing search results
            JSONArray resultsArray = response.getJSONArray("events");

            // Create a list to hold search results
            List<SearchResult> searchResults = new ArrayList<>();

            // Loop through the JSON array and extract information for each search result
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject result = resultsArray.getJSONObject(i);

                Log.i("BIGGGG",result.toString());
                // Extract the required information for each search result
//                String title = result.getString("title");
//                String venue = result.getString("venue");
//                String date = result.getString("date");
                String title = "title";
                String venue ="venue";
                String date = "date";
                String time = "time";
                String category = "category";
                String eventImage = "image";

                // Create a new SearchResult object with the extracted information
                SearchResult searchResult = new SearchResult(title, date, venue, time, category, eventImage, false);

                // Add the SearchResult object to the list
                searchResults.add(searchResult);
            }
            Log.i("SEARCH RESULT",searchResults.toString());

            // Set the adapter for the RecyclerView with the list of search results
            if(adapter!=null) {
                adapter.setData(searchResults);
            }
            else{
                Log.i("NULLLL","");
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Log.i("onDestroy","");
        super.onDestroy();
    }

    @Override
    public void onDetach(){
        Log.i("OnDetach","");
        super.onDetach();
    }
}