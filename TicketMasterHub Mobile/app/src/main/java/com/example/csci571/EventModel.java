package com.example.csci571;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EventModel {
    public static class EventDetails implements Serializable {
        private String eventName;
        private String eventDate;
        private String eventLocation;
        private String eventTime;
        private String eventCategory;
        private String eventImageUrl;
        private String eventId;

        public EventDetails(){
            this.eventName = "";
            this.eventDate = "";
            this.eventLocation = "";
            this.eventTime = "";
            this.eventCategory = "";
            this.eventImageUrl = "";
            this.eventId = "";
        }
        public EventDetails(String eventName, String eventDate, String eventLocation, String eventTime,
                            String eventCategory, String eventImageUrl, String eventId) {
            this.eventName = eventName;
            this.eventDate = eventDate;
            this.eventLocation = eventLocation;
            this.eventTime = eventTime;
            this.eventCategory = eventCategory;
            this.eventImageUrl = eventImageUrl;
            this.eventId = eventId;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getEventDate() {
            return eventDate;
        }

        public void setEventDate(String eventDate) {
            this.eventDate = eventDate;
        }

        public String getEventLocation() {
            return eventLocation;
        }

        public void setEventLocation(String eventLocation) {
            this.eventLocation = eventLocation;
        }

        public String getEventTime() {
            return eventTime;
        }

        public void setEventTime(String eventTime) {
            this.eventTime = eventTime;
        }

        public String getEventCategory() {
            return eventCategory;
        }

        public void setEventCategory(String eventCategory) {
            this.eventCategory = eventCategory;
        }

        public String getEventImageUrl() {
            return eventImageUrl;
        }

        public void setEventImageUrl(String eventImageUrl) {
            this.eventImageUrl = eventImageUrl;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        // Constructor and getter/setter methods
    }

    public static class EventList implements Serializable {
        private List<EventDetails> events;

        public EventList() {
            events = new ArrayList<>();
        }

        public List<EventDetails> getEvents() {
            return events;
        }

        public void addEvent(EventDetails eventDetails) {
            events.add(eventDetails);
        }
    }
}
