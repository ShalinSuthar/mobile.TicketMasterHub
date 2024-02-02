package com.example.csci571;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

// This Adapter is for the Event list
public class FragmentRecyclerViewAdapter extends RecyclerView.Adapter<FragmentRecyclerViewAdapter.ViewHolder> {

    private List<SearchResult> searchResults;
    private ImageView imageView;
    private LinearLayout noFavoritesLayout;
    private RelativeLayout recyclerFrameLayout;
    private Fragment fragment;



    public FragmentRecyclerViewAdapter(List<SearchResult> searchResults) {

        this.searchResults = searchResults;
    }

    public void setFragment(Fragment myfrag){
        this.fragment = myfrag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("shaaanonCreateViewHolder","came in here");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        if (searchResults.isEmpty()) {
//            holder.noFav.setVisibility(View.VISIBLE);
//            holder.favFrag.setVisibility(View.GONE);
//            return;
//        } else {
//            holder.noFav.setVisibility(View.GONE);
//            holder.favFrag.setVisibility(View.VISIBLE);
//        }
        Log.i("ON CREATE onBindViewHolder","HERE"+searchResults.toString());
        SearchResult searchResult = searchResults.get(position);
        holder.title.setText(searchResult.getTitle());
        holder.date.setText(searchResult.getDate());
        holder.location.setText(searchResult.getLocation());
        holder.time.setText(searchResult.getTime());
        holder.category.setText(searchResult.getCategory());
        SearchResult searchResult1 = searchResults.get(position);
        holder.heartButton.setButtonDrawable(R.drawable.heart);

        String imageUrl = searchResult1.getEventImage();
        Picasso.get().load(imageUrl).into(holder.imageurl);

        SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
                SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                          String key) {
                        // your stuff here
                    }
                };

        holder.heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("clickkkkkkkk",Boolean.toString(holder.heartButton.isChecked()));

                SearchResult clickedResult = searchResults.get(holder.getBindingAdapterPosition());
                // get unique event ID from searchResult object
                String eventId = clickedResult.getId();
                // handle click event
                boolean isFav = clickedResult.isFavorite();
                if(isFav){
                    holder.heartButton.setButtonDrawable(R.drawable.heart);
                }
//                EventModel.EventDetails eventDetails = new EventModel.EventDetails();
//                eventDetails.setEventName(clickedResult.getTitle());
//                eventDetails.setEventDate(clickedResult.getDate());
//                eventDetails.setEventLocation(clickedResult.getLocation());
//                eventDetails.setEventCategory(clickedResult.getCategory());
//                eventDetails.setEventId(clickedResult.getId());
//                eventDetails.setEventTime(clickedResult.getTime());
//                eventDetails.setEventImageUrl(clickedResult.getEventImage());

                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("MyEvents", v.getContext().MODE_PRIVATE);

// Retrieve the list of event details from shared preferences
                String serializedList = sharedPreferences.getString("eventList", "");
                List<EventModel.EventDetails> eventList;

                Log.i("clickkkkkkkk",Boolean.toString(holder.heartButton.isChecked()));

// If the list is not empty, deserialize it using ObjectInputStream
                if (!serializedList.isEmpty()) {
                    byte[] bytes = Base64.decode(serializedList, Base64.DEFAULT);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                    try {
                        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                        eventList = (List<EventModel.EventDetails>) objectInputStream.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }
                } else {
                    // If the list is empty, create a new list
                    eventList = new ArrayList<>();
                }

                for (int i = 0; i < eventList.size(); i++) {
                    Log.i("Sheero",eventList.get(i).getEventId()+" me " +clickedResult.getId());
                    if (eventList.get(i).getEventId().equals(clickedResult.getId())) {
                        eventList.remove(i);
//                        noFavoritesLayout = view.findViewById(R.id.no_favorites_layout);
//                        recyclerFrameLayout = view.findViewById(R.id.recyclerFrameLayout1);
//                        if(eventList.size()==0){
//                            noFavoritesLayout.setVisibility(View.VISIBLE);
//                            recyclerFrameLayout.setVisibility(View.GONE);
//                        }

                        searchResults.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemChanged(position);
                        notifyDataSetChanged();
//                        notifyItemRangeChanged(position, getItemCount());

                        Toast.makeText(v.getContext(), clickedResult.getTitle()+" removed from the favorites", Toast.LENGTH_SHORT).show();
                        break;
                    }

                }
//                eventList.remove(position);
//
//                Toast.makeText(v.getContext(), "Event removed from the favorites", Toast.LENGTH_SHORT).show();


//
//                Boolean isAlreadyFav = false;
//                for (int i = 0; i < eventList.size(); i++) {
//                    if (eventList.get(i).getEventId().equals(clickedResult.getId())) {
//                        eventList.remove(i);
//                        Toast.makeText(v.getContext(), "Event removed from the favorites", Toast.LENGTH_SHORT).show();
//                        isAlreadyFav = true;
//                        break;
//                    }
//
//                }
//                if(!isAlreadyFav){
//                    eventList.add(eventDetails);
//                    Toast.makeText(v.getContext(), "Event added to favorites", Toast.LENGTH_SHORT).show();
//                }
//                if(eventList!=null && eventList.size()!=0){
//                    final EventModel.EventDetails event = eventList.get(position);
//
//                }
//                else{
//
//                }
//                if(!holder.heartButton.isChecked()){
//                    holder.heartButton.setButtonDrawable(R.drawable.heart);
//                    Toast.makeText(v.getContext(), "Event added to favorites", Toast.LENGTH_SHORT).show();
//                }
//                else{
//
//                    eventList.remove(event);
//                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position, getItemCount());
//                    holder.heartButton.setButtonDrawable(R.drawable.heart_outline);
//                    Toast.makeText(v.getContext(), "Event removed from the favorites", Toast.LENGTH_SHORT).show();
////
//
//                }


// Add the new event details to the list
//                eventList.add(eventDetails);

// Serialize the updated list using ObjectOutputStream
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(eventList);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                String serializedUpdatedList = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

// Save the updated list back to shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("eventList", serializedUpdatedList);
                editor.apply();
                Context context = v.getContext();
//                ((FavoritesFragment) context).onViewCreated();
//
//                toggleFavorite(eventId);
//                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("EventDetails", v.getContext().MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("eventName", clickedResult.getTitle());
//                editor.putString("eventDate", clickedResult.getDate());
//                editor.putString("eventLocation", clickedResult.getLocation());
//                editor.putString("eventTime", clickedResult.getTime());
//                editor.putString("eventCategory", clickedResult.getCategory());
//                editor.putString("eventImageUrl", clickedResult.getEventImage());
//                editor.putString("eventId", clickedResult.getId());
//                editor.apply();
                // TODO: Save event to shared preferences
            }
        });
        // set other data to holder views

        holder.imageurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("On clicking", "onClick: ");
                Intent intent = new Intent(v.getContext(), SecondActivity.class);
                intent.putExtra("event_id", searchResults.get(holder.getBindingAdapterPosition()).getId());
                intent.putExtra("venue_name", searchResults.get(holder.getBindingAdapterPosition()).getLocation());
                intent.putExtra("attraction_list", searchResults.get(holder.getBindingAdapterPosition()).getAttractionsList());
                intent.putExtra("event_name", searchResults.get(holder.getBindingAdapterPosition()).getTitle());
                intent.putExtra("is_music_event", searchResults.get(holder.getBindingAdapterPosition()).getIsMusicEvent());
                intent.putExtra("is_favorite_event", searchResults.get(holder.getBindingAdapterPosition()).isFavorite());

                Log.d("IDDDD", "ID IS "+searchResults.get(holder.getBindingAdapterPosition()).getId());
                Log.d("meeee", "ID IS" + intent.getExtras());
                v.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        int s = searchResults.size();
        Log.i("shaaangetItemCount",Integer.toString(s));
        return searchResults.size();
    }

    public void setData(List<SearchResult> searchResults) {
        Log.i("shaaansetData",searchResults.toString());
        this.searchResults = searchResults;
        notifyDataSetChanged();
    }

//    @Override
//    public void onFavoriteClicked(EventModel.EventDetails eventDetails, boolean isFavourite) {
//        notifyDataSetChanged();
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView date;

        public TextView location;

        public TextView time;
        public TextView category;

        public ImageView imageurl;

        public LinearLayout noFav;

        public RelativeLayout favFrag;

        public ToggleButton heartButton;

        private Fragment favFrament;
        // add other views for search result item layout

        public ViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.fav_event_name);
            date = (TextView) view.findViewById(R.id.fav_event_date);
            location = (TextView) view.findViewById(R.id.fav_event_location);
            time = (TextView) view.findViewById(R.id.fav_event_time);
            category = (TextView) view.findViewById(R.id.fav_event_category);
            imageurl = (ImageView) view.findViewById(R.id.fav_event_image);
            heartButton = view.findViewById(R.id.fav_favorite_button);

        }
    }
}
