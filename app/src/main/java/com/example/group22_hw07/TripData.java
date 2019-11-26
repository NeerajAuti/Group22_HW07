package com.example.group22_hw07;

import com.google.type.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TripData {
    String TripID;
    String CreatedBy;
    String TripName;
    String TripDescription;
    List<LatLng> Location;
    String PhotoURL;

    @Override
    public String toString() {
        return "TripData{" +
                "TripID='" + TripID + '\'' +
                ", CreatedBy='" + CreatedBy + '\'' +
                ", TripName='" + TripName + '\'' +
                ", TripDescription='" + TripDescription + '\'' +
                ", Location=" + Location +
                ", PhotoURL='" + PhotoURL + '\'' +
                '}';
    }

    public TripData() {
    }
    public TripData(Map<String, Object> userMap) {
        this.TripID = (String) userMap.get("TripID");
        this.CreatedBy = (String) userMap.get("CreatedBy");
        this.TripName = (String) userMap.get("TripName");
        this.Location = (List<LatLng>) userMap.get("Location");
        this.PhotoURL = (String) userMap.get("PhotoURL");
        this.TripDescription = (String) userMap.get("TripDescription");
    }

    public Map toHashMap() {
        Map<String, Object> userMap = new HashMap<>();

        userMap.put("TripID", this.TripID);
        userMap.put("CreatedBy", this.CreatedBy);
        userMap.put("TripName", this.TripName);
        userMap.put("Location", this.Location);
        userMap.put("PhotoURL", this.PhotoURL);
        userMap.put("TripDescription", this.TripDescription);

        return userMap;
    }

    public String getTripDescription() {
        return TripDescription;
    }

    public void setTripDescription(String tripDescription) {
        TripDescription = tripDescription;
    }

    public String getTripID() {
        return TripID;
    }

    public void setTripID(String tripID) {
        TripID = tripID;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getTripName() {
        return TripName;
    }

    public void setTripName(String tripName) {
        TripName = tripName;
    }

    public List<LatLng> getLocation() {
        return Location;
    }

    public void setLocation(List<LatLng> location) {
        Location = location;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }
}
