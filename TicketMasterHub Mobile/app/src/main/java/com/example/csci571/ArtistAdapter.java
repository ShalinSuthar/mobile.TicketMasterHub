package com.example.csci571;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private List<Artist> artistList;
    private Context context;
    private List<Artist> artistResults;

    public ArtistAdapter(List<Artist> artistList) {
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your row layout and return a new ArtistViewHolder object
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artist, parent, false);
        return new ArtistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        // Bind the data to the views inside the ArtistViewHolder
//        Artist artist = artistList.get(position);
//        holder.bind(artist, context);

        Artist artistResults = artistList.get(position);
        holder.nameTextView.setText(artistResults.getArtistName());
        holder.nameTextView.setSelected(true);
//        holder.artistImageView.setText(artistResults.getDate());
        holder.popularityTextView.setText(String.valueOf(artistResults.getPopularity()));
        holder.urlTextView.setText(artistResults.getSpotifyUrl());

//        TextView textView =(TextView)findViewById(R.id.textView);
        String url = artistResults.getSpotifyUrl();
        String message = "Check on spotify";
        SpannableString spannableString = new SpannableString(message);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                // Handle URL click
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                holder.urlTextView.getContext().startActivity(intent);
            }
        };
        spannableString.setSpan(clickableSpan, 0, message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.urlTextView.setText(spannableString);
        holder.urlTextView.setMovementMethod(LinkMovementMethod.getInstance());
        holder.followersTextView.setText(String.valueOf(formatNumber(artistResults.getFollowers()))+" Followers");
//        SearchResult searchResult1 = artistResults.get(position);
//        holder.heartButton.setButtonDrawable(R.drawable.heart);
        List<String> au = artistResults.getAlbumsUrl();
        String a1 = au.get(0);
        String a2 = au.get(1);
        String a3 = au.get(2);
        Picasso.get().load(a1).into(holder.album1);
        Picasso.get().load(a2).into(holder.album2);
        Picasso.get().load(a3).into(holder.album3);

        holder.popularityProgressBar.setProgress(artistResults.getPopularity());


        String imageUrl = artistResults.getArtistImageUrl();
        Picasso.get().load(imageUrl).into(holder.artistImageView);
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    static class ArtistViewHolder extends RecyclerView.ViewHolder {

        // Declare your views here
        TextView nameTextView;
        TextView followersTextView;
        TextView urlTextView;
        ImageView artistImageView;
        ProgressBar popularityProgressBar;
        TextView popularityTextView;
        ImageView album1;
        ImageView album2;
        ImageView album3;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your views here
            nameTextView = itemView.findViewById(R.id.artist_name);
            followersTextView = itemView.findViewById(R.id.artist_followers);
            urlTextView = itemView.findViewById(R.id.artist_url);
            artistImageView = itemView.findViewById(R.id.artist_image);
            popularityProgressBar = itemView.findViewById(R.id.popularity_spinner);
            popularityTextView = itemView.findViewById(R.id.popularity_value);
//            popularAlbumsRecyclerView = itemView.findViewById(R.id.album_1_image);
            album1 = itemView.findViewById(R.id.album_1_image);
            album2 = itemView.findViewById(R.id.album_2_image);
            album3 = itemView.findViewById(R.id.album_3_image);
        }

        public void bind(Artist artist, Context context) {
            // Bind the data to the views here
//            nameTextView.setText(artist.getName());
//            followersTextView.setText(context.getString(R.string.artist_followers, artist.getFollowers()));
//            urlTextView.setText(artist.getUrl());
            // ...
        }


    }

    public static String formatNumber(int number) {
        if (number < 1000) {
            return String.valueOf(number);
        } else if (number < 1000000) {
            return String.format("%.1fK", number / 1000.0);
        } else {
            return String.format("%.1fM", number / 1000000.0);
        }
    }
    public void setData(List<Artist> searchResults) {
//        Log.i("musssi",searchResults.toString() + Integer.toString(searchResults.size()));
        this.artistList = searchResults;
        notifyDataSetChanged();
    }
}

