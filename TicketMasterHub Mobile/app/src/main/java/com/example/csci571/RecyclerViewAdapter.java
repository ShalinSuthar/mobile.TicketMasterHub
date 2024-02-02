package com.example.csci571;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;


import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

// This Adapter is for the Event list
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<SearchResult> searchResults;
    private ImageView imageView;
//    private OnFavoriteClickListener onFavoriteClickListener;

    public RecyclerViewAdapter(List<SearchResult> searchResults) {

        this.searchResults = searchResults;
    }

//    public interface OnFavoriteClickListener {
//        void onFavoriteClicked(EventModel.EventDetails eventDetails, boolean isFavourite);
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("mannnnnnn","came in here");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("ON CREATE","HERE"+searchResults.toString());
        SearchResult searchResult = searchResults.get(position);
        holder.title.setText(searchResult.getTitle());
        holder.date.setText(searchResult.getDate());
        holder.location.setText(searchResult.getLocation());
        holder.time.setText(searchResult.getTime());
        holder.category.setText(searchResult.getCategory());
        SearchResult searchResult1 = searchResults.get(position);
        Log.i("mistiiiiii",Boolean.toString(searchResult.isFavorite()));

        SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("MyEvents", 0);
        String serializedList = sharedPreferences.getString("eventList", "");
        List<EventModel.EventDetails> eventList;

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

// Retrieve the list of event details from shared preferences
//        String serializedList1 = sharedPreferences.getString("eventList", "");
//        List<EventModel.EventDetails> eventList1;

        Boolean isAlreadyFav = false;
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getEventId().equals(searchResult.getId())) {
                isAlreadyFav = true;
                break;
            }}
        if(isAlreadyFav){
            holder.heartButton.setButtonDrawable(R.drawable.heart);
        }


                String imageUrl = searchResult1.getEventImage();
        Picasso.get().load(imageUrl).into(holder.imageurl);


        holder.heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                holder.heartButton.setChecked(!holder.heartButton.isChecked());
                SearchResult clickedResult = searchResults.get(holder.getBindingAdapterPosition());
                // get unique event ID from searchResult object
                String eventId = clickedResult.getId();


                boolean isFav = clickedResult.isFavorite();
                if(isFav){
                    holder.heartButton.setButtonDrawable(R.drawable.heart);
                }
                // handle click event
                EventModel.EventDetails eventDetails = new EventModel.EventDetails();
                eventDetails.setEventName(clickedResult.getTitle());
                eventDetails.setEventDate(clickedResult.getDate());
                eventDetails.setEventLocation(clickedResult.getLocation());
                eventDetails.setEventCategory(clickedResult.getCategory());
                eventDetails.setEventId(clickedResult.getId());
                eventDetails.setEventTime(clickedResult.getTime());
                eventDetails.setEventImageUrl(clickedResult.getEventImage());

                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("MyEvents", v.getContext().MODE_PRIVATE);

// Retrieve the list of event details from shared preferences
                String serializedList = sharedPreferences.getString("eventList", "");
                List<EventModel.EventDetails> eventList;

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

// Add the new event details to the list
                Boolean isAlreadyFav = false;
                for (int i = 0; i < eventList.size(); i++) {
                    if (eventList.get(i).getEventId().equals(clickedResult.getId())) {
                        eventList.remove(i);
//                        searchResults.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());
                        holder.heartButton.setButtonDrawable(R.drawable.heart_outline);
                        Log.d("addddddd",Integer.toString(eventList.size()));
                        isAlreadyFav = true;
                        clickedResult.setFavorite(false);
                        Toast.makeText(v.getContext(), clickedResult.getTitle()+" removed from the favorites", Toast.LENGTH_SHORT).show();
                        break;
                    }

                }
                if(!isAlreadyFav){
                    holder.heartButton.setButtonDrawable(R.drawable.heart);
                    clickedResult.setFavorite(true);
//                    searchResults.add(searchResult);
                    eventList.add(eventDetails);
                    notifyDataSetChanged();
                    notifyItemInserted(position);
                    notifyItemRangeChanged(position, getItemCount());
                    Toast.makeText(v.getContext(),clickedResult.getTitle()+" added to the favorites", Toast.LENGTH_SHORT).show();
                    Log.d("addddddd",Integer.toString(eventList.size()));


                }


                // Notify the listener
//                onFavoriteClickListener.onFavoriteClicked(eventDetails, isFav);
//                if(!holder.heartButton.isChecked()){
//                    holder.heartButton.setButtonDrawable(R.drawable.heart);
//
//                }
//                else{
//
//                }



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
                notifyDataSetChanged();



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
        Log.i("lennnnnnn",Integer.toString(s));
        return searchResults.size();
    }

    public void setData(List<SearchResult> searchResults) {
        Log.i("small",searchResults.toString());
        this.searchResults = searchResults;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView date;

        public TextView location;

        public TextView time;
        public TextView category;

        public ImageView imageurl;

        public ToggleButton heartButton;
        // add other views for search result item layout

        public ViewHolder(View view) {
            super(view);
            Log.i("lomppp",view.toString());
            title = (TextView) view.findViewById(R.id.event_name);
            date = (TextView) view.findViewById(R.id.event_date);
            location = (TextView) view.findViewById(R.id.event_location);
            time = (TextView) view.findViewById(R.id.event_time);
            category = (TextView) view.findViewById(R.id.event_category);
            imageurl = (ImageView) view.findViewById(R.id.event_image);
            heartButton = view.findViewById(R.id.favorite_button);
        }
    }
}
