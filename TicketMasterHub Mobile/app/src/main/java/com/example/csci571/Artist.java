package com.example.csci571;

import java.util.List;

public class Artist {

        private String artistImageUrl;
        private String artistName;
        private int followers;
        private String spotifyUrl;
        private int popularity;
        private List<String> albumsUrl;

        // Constructor, getters, and setters
        public Artist(String artistImageUrl,String name, int followers, String spotifyUrl, int popularity, List<String> albumsUrl){
                this.artistImageUrl = artistImageUrl;
                this.artistName = name;
                this.followers = followers;
                this.spotifyUrl = spotifyUrl;
                this.popularity = popularity;
                this.albumsUrl = albumsUrl;
        }

        public String getArtistImageUrl(){
                return this.artistImageUrl;
        }

        public void setArtistImageUrl(String artistImageUrl){
                this.artistImageUrl = artistImageUrl;
        }

        public String getArtistName() {
                return this.artistName;
        }

        public void setAristName(String name) {
                this.artistName = name;
        }

        public int getFollowers() {
                return this.followers;
        }

        public void setFollowers(int followers) {
                this.followers = followers;
        }

        public String getSpotifyUrl() {
                return this.spotifyUrl;
        }

        public void setSpotifyUrl(String spotifyUrl) {
                this.spotifyUrl = spotifyUrl;
        }

        public int getPopularity() {
                return this.popularity;
        }

        public void setPopularity(int popularity) {
                this.popularity = popularity;
        }

        public List<String> getAlbumsUrl() {
                return this.albumsUrl;
        }

        public void setAlbumsUrl(List<String> albumsUrl) {
                this.albumsUrl = albumsUrl;
        }

}
