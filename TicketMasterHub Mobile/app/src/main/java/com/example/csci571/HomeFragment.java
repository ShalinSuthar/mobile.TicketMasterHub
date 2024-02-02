package com.example.csci571;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

public class HomeFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private AutoCompleteTextView keywordInput;
    private EditText distanceInput;
    private Spinner categoryInput;
    private EditText locationInput;
    private Button searchButton;
    private Button clearButton;

    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private static final int DEBOUNCE_DELAY = 500;

    private LinearLayout homePg;

    private boolean shouldOpenDropdown = true;

    private RecyclerView recyclerView;
    private LinearLayout formFrameLayout;
    private RelativeLayout recyclerFrameLayout;

    private String selectedVal;
    private FusedLocationProviderClient fusedLocationClient;
    private Switch switchAutoLocation;
    private RequestQueue requestQueue;

    private FrameLayout frameLayout;

    private LinearLayout noEevntsLayout;
    private final String BASE_URL = "https://csci571-210323.wl.r.appspot.com/";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        keywordInput = view.findViewById(R.id.keyword_input);
        distanceInput = view.findViewById(R.id.distance_input);
        categoryInput = view.findViewById(R.id.category_input);
        locationInput = view.findViewById(R.id.location_input);
        searchButton = view.findViewById(R.id.search_button);
        clearButton = view.findViewById(R.id.clear_button);
        formFrameLayout = view.findViewById(R.id.form_frame);
        recyclerFrameLayout = view.findViewById(R.id.recyclerFrameLayout);
        noEevntsLayout = view.findViewById(R.id.no_events_layout);
        homePg = view.findViewById(R.id.homeprogress);
//        formFrameLayout = view.findViewById(R.id.frame_parent);


        requestQueue = Volley.newRequestQueue(getContext());
        // Set up autocomplete for keyword input
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.my_list_item, getResources().getStringArray(R.array.keyword_options));
        keywordInput.setAdapter(adapter);
        String selectedCategory = getResources().getStringArray(R.array.category_values)[categoryInput.getSelectedItemPosition()];

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<CharSequence> adapterCat = ArrayAdapter.createFromResource(getContext(),
                R.array.category_names, R.layout.spinner_item_layout);
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryInput.setAdapter(adapterCat);

        Spinner categorySpinner = view.findViewById(R.id.category_input);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedCategory = parent.getItemAtPosition(position).toString();
                String selectedCategory = getResources().getStringArray(R.array.category_values)[categoryInput.getSelectedItemPosition()];
                Log.d("Selected Category", selectedCategory);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        ImageButton backButton = (ImageButton) (view.findViewById(R.id.backButton));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the form layout when the back button is clicked
                formFrameLayout.setVisibility(View.VISIBLE);

                recyclerFrameLayout.setVisibility(View.GONE);
//                FrameLayout recyclerFrameLayout = findViewById(R.id.recyclerFrameLayout);
//                recyclerFrameLayout.setVisibility(View.GONE);
//                // Replace "formLayout" with the ID of your form layout
//                findViewById(R.id.formLayout).setVisibility(View.VISIBLE);
            }
        });


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        Switch autoLocationSwitch = view.findViewById(R.id.switchAutoLocation);
        autoLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    locationInput.setEnabled(false);
                    locationInput.setVisibility(View.GONE);

                    Log.d("checked", "Current location: " );
                    // Auto-location is enabled, get the device's current location
//                    if (ContextCompat.checkSelfPermission(getActivity(),
//                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                        Log.d("premissioned", "Current location: " );
//                        LocationRequest locationRequest = LocationRequest.create();
//                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                        locationRequest.setInterval(10000);
//                        // Permission already granted, get the current location
//                        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//                            @Override
//                            public void onSuccess(Location location) {
//                                if (location != null) {
//                                    // Use the location to get the latitude and longitude
//                                    double latitude = location.getLatitude();
//                                    double longitude = location.getLongitude();
//                                    // Use these values to make your API call or perform any other action
//                                    Log.d("Location", "Current location: " + latitude + ", " + longitude);
//                                }
//                            }
//                        });
//                    } else {
//                        // Permission not granted, show a dialog to request permission
//                        ActivityCompat.requestPermissions(getActivity(),
//                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                MY_PERMISSIONS_REQUEST_LOCATION);
//                    }
                } else {
                    locationInput.setEnabled(true);
                    locationInput.setVisibility(View.VISIBLE);
//                    // Auto-location is disabled, perform any required actions
                }
            }
        });

//        keywordInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Get the selected option
//                String selectedOption = (String) parent.getItemAtPosition(position);
//
//                // Perform the desired action with the selected option
//                // ...
//
//                // Reset the text and clear focus to dismiss the dropdown
////                keywordInput.setText();
//
//
//                // Prevent any new API calls until the current one has completed
////                isApiCallInProgress = true;
//
//                // Update the dropdown with the selected option
////                List<String> selectedOptionList = Collections.singletonList(selectedOption);
////                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, selectedOptionList);
////                keywordInput.setAdapter(adapter);
//            }
//        });
//
//        keywordInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Get the selected option
//                String selectedOption = (String) parent.getItemAtPosition(position);
//
//                ListAdapter adapter = keywordInput.getAdapter();
////                keywordInput.setAdapter(null);
////                keywordInput.dismissDropDown();
////                keywordInput.setText("whatever");
//
//                keywordInput.setText(keywordInput.getAdapter().getItem(position).toString(), false);
////                autoCompleteTextView.setAdapter(adapter);
//                // Perform the desired action with the selected option
//                // ...
//
////                // Reset the text and clear focus to dismiss the dropdown
////                autoCompleteTextView.setText("");
////                autoCompleteTextView.clearFocus();
////
////                // Prevent any new API calls until the current one has completed
////                isApiCallInProgress = true;
////
////                // Update the dropdown with the selected option
////                List<String> selectedOptionList = Collections.singletonList(selectedOption);
////                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, selectedOptionList);
////                autoCompleteTextView.setAdapter(adapter);
//            }
//        });
////        keywordInput.setDropDownBackgroundResource();
        keywordInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                shouldOpenDropdown = true;
                selectedVal = s.toString();
                if (mRunnable != null) {
                    mHandler.removeCallbacks(mRunnable);
                }

                // Set up a new request
//                mRunnable = new Runnable() {
//                    @Override
//                    public void run() {
                        String keyword = s.toString().trim();
                        if (keyword.length() > 1) {
                            Log.d("API Call", "Making API call for keyword: " + keyword);
                            String url = BASE_URL + "autocomplete?input=" + keyword;
                            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                                    response -> {
                                        try{
                                            String[] suggestions = new String[response.length()];
                                            for (int i = 0; i < response.length(); i++) {
                                                suggestions[i] = response.getString(i);
                                            }
                                            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                                                    R.layout.my_list_item, suggestions);
                                            keywordInput.setAdapter(adapter);
                                            keywordInput.showDropDown();
//                                            adapter.clear();
//                                            adapter.addAll(suggestions);
                                            adapter.notifyDataSetChanged();
                                            Log.d("API Call", "API call successful. Suggestions received: " + Arrays.toString(suggestions));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    },
                                    error -> {
//                                        Toast.makeText(requireContext(), "Error fetching autocomplete suggestions", Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();
                                    });
//                            mHandler.postDelayed(mRunnable, DEBOUNCE_DELAY);
                            requestQueue.add(jsonArrayRequest);

                        }
//                    }};


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get selected category
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                Log.d("tag","Search API response");

                // Build search API URL
                String keyword = keywordInput.getText().toString();
                String distance = distanceInput.getText().toString();
                String location = locationInput.getText().toString();

                if(autoLocationSwitch.isChecked()){
                    location = "usc";
                }

                if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(location)) {
//                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    Snackbar s = Snackbar.make(view, "Please fill all fields", Snackbar.LENGTH_SHORT)
                            .setTextColor(Color.BLACK)
                            .setBackgroundTint(Color.rgb(199,198,197))
                            .setActionTextColor(Color.WHITE);
                    View snackbarView = s.getView();
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbarView.getLayoutParams();
                    params.setMargins(16, 16, 16, 16);
                    snackbarView.setLayoutParams(params);
                    s.show();

                    return;
                }

                callTicketMasterAPI(keyword, location, distance, selectedCategory, view);
//                String autolocation = String.valueOf(switchAutoLocation.isChecked());
//                String url = "https://csci571-210323.wl.r.appspot.com/search?keyword=" + keyword + "&distance=" + distance + "&category=" + selectedCategory + "&location=" + location + "&autolocation=true";
//                Log.d("Check",keyword + distance + location);
//                // Call search API
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                        response -> {
//
//                            // Handle API response
//                            Log.d("tag","Search API response" + response.toString());
//                        },
//                        error -> {
//                            // Handle API error
//                            Log.e("tag", "Search API error: " + error.getMessage());
//                        });
//
//                // Add API request to the Volley request queue
//                Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
////
//                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
//                Log.d("intent", "onClick: intent started");
//                intent.putExtra("query", keyword);
//                Log.d("extraput", "onClick: intent started");
//                startActivity(intent);
            }
        });


    }

    private void callTicketMasterAPI(String keyword, String location, String distance, String selectedCategory, View view) {
                // Construct the API request URL
        formFrameLayout.setVisibility(View.GONE);
        recyclerFrameLayout.setVisibility(View.GONE);
        homePg.setVisibility(View.VISIBLE);

                String url = "https://csci571-210323.wl.r.appspot.com/search?keyword=" + keyword + "&distance=" + distance + "&category=" + selectedCategory + "&location=" + location + "&autolocation=false";

                // Send the API request
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
//                                recyclerFrameLayout.setVisibility(View.GONE);
                                homePg.setVisibility(View.GONE);
                                formFrameLayout.setVisibility(View.VISIBLE);
                                Log.d("response","Search API response" + response.toString());
                                // Parse the JSON response and populate the RecyclerView in the SearchResultsFragment
                                try {
                                    if(!response.has("_embedded")){
                                        formFrameLayout.setVisibility(View.GONE);
                                        recyclerFrameLayout.setVisibility(View.VISIBLE);
                                        noEevntsLayout.setVisibility(View.VISIBLE);
                                        recyclerView = (RecyclerView) recyclerFrameLayout.findViewById(R.id.recyclerView);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        RecyclerViewAdapter adapter;
                                        adapter = new RecyclerViewAdapter(new ArrayList<SearchResult>());
                                        recyclerView.setAdapter(adapter);
                                        Log.i("ON CREATE","NOT NULL");
                                        List<SearchResult> sr = new ArrayList<SearchResult>();
                                        adapter.setData(sr);
//
//                                        formFrameLayout.setVisibility(View.GONE);
                                        return;
                                    }
                                    noEevntsLayout.setVisibility(View.GONE);
                                    JSONArray eventsArray = response.getJSONObject("_embedded").getJSONArray("events");
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("events", eventsArray);
                                    Log.d("response","Search API response" + jsonObject);
                                    List<SearchResult> searchResults = new ArrayList<>();


                                    // Loop through the JSON array and extract information for each search result
                                    for (int i = 0; i < eventsArray.length(); i++) {
                                        JSONObject result = eventsArray.getJSONObject(i);

                                        // Extract the required information for each search result
                                        // String title = result.getString("title");
                                        //String venue = result.getString("venue");
//                String date = result.getString("date");
                                        Log.i("BIGGA",result.toString());
                                        String title = result.getString("name");
                                        String venue = result.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                                        String date =  result.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                        String time = "";
                                        if(result.getJSONObject("dates").getJSONObject("start").has("localTime")){
                                            time = (result.getJSONObject("dates").getJSONObject("start").getString("localTime") != null) ? result.getJSONObject("dates").getJSONObject("start").getString("localTime") : "";

                                        }


                                        String category = result.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
//                                        String imageUrl = result.getJSONObject("images").getJSONArray("url").getJSONObject(0).getString("url");
                                        String imageUrl = result.getJSONArray("images").getJSONObject(0).getString("url");

                                        Boolean isMusicEvent = false;
                                        JSONArray classifications = result.getJSONArray("classifications");
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
                                        for (String c : details) {
                                            if ("music".equalsIgnoreCase(c)) {
                                                isMusicEvent = true;
                                                break;
                                            }
                                        }


                                        JSONArray attractions1 = result.getJSONObject("_embedded").getJSONArray("attractions");
                                        JSONArray artistNames1 = new JSONArray();
                                        List<String> artistList = new ArrayList<>();
                                        int length = attractions1.length();
                                        String[] artistArray = new String[length];

                                        if (attractions1.length() > 0) {
                                            for (int j = 0; j < attractions1.length(); j++) {
                                                String name = attractions1.getJSONObject(j).optString("name");
                                                if (!name.equalsIgnoreCase("undefined") && !name.equalsIgnoreCase("null")) {
                                                    Log.i("LULLU",name);
                                                    artistArray[j]=name;
                                                }
                                            }
                                        }


//                                        ImageView eventImage = itemView.findViewById(R.id.event_image);
//                                        Picasso.get().load(imageUrl).into(eventImage);
                                        // Create a new SearchResult object with the extracted information
                                        SearchResult searchResult = new SearchResult(title, date, venue, time, category,imageUrl, false);
                                        searchResult.setId(result.getString("id"));
                                        searchResult.serIsMusicEvent(isMusicEvent);
                                        searchResult.setAttractionsList(artistArray);
//                                        Log.i("MULLU",artistArray[1]);
//                                        SearchResult.setVenue(venue);

                                        // Add the SearchResult object to the list
                                        searchResults.add(searchResult);
                                    }
                                    Log.i("SEARCH RESULT",searchResults.toString());
                                    //SearchResultsFragment searchResultsFragment = new SearchResultsFragment(searchResults);


                                    //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    //FragmentTransaction transaction = fragmentManager.beginTransaction();

                                    //transaction.replace(R.id.container, searchResultsFragment);
                                    //transaction.addToBackStack(null);
                                    //transaction.commitAllowingStateLoss();
                                    //fragmentManager.executePendingTransactions();
                                    //searchResultsFragment.setDataa();
//                                    searchResultsFragment.populateRecyclerView(jsonObject);
                                    formFrameLayout.setVisibility(View.GONE);
                                    recyclerFrameLayout.setVisibility(View.VISIBLE);
                                    recyclerView = (RecyclerView) recyclerFrameLayout.findViewById(R.id.recyclerView);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    RecyclerViewAdapter adapter;
                                    adapter = new RecyclerViewAdapter(new ArrayList<SearchResult>(searchResults));
                                    recyclerView.setAdapter(adapter);
                                    Log.i("ON CREATE","NOT NULL");
                                    adapter.setData(searchResults);



                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                    }
                });

                // Add the API request to the request queue
                VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
            }


//    public void populateAutoCompleteTextView() {
//        String url = "http://example.com/api/search";
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        ArrayList<String> list = new ArrayList<String>();
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                JSONObject jsonObject = response.getJSONObject(i);
//                                String suggestion = jsonObject.getString("suggestion");
//                                list.add(suggestion);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                                android.R.layout.simple_dropdown_item_1line, list);
//                        keywordInput.setAdapter(adapter);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // Handle Volley error
//            }
//        });
//
//        requestQueue.add(jsonArrayRequest);
//    }

    private void performSearch() {
        // TODO: Implement search logic here
    }

    private void clearForm() {
        keywordInput.setText("");
        distanceInput.setText("10");
        categoryInput.setSelection(0);
        locationInput.setText("");
    }

}

