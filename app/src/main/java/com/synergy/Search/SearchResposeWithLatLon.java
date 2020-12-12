package com.synergy.Search;

public class SearchResposeWithLatLon {
   private GeoLocation geoLocation;
   private String frId;

    public SearchResposeWithLatLon(GeoLocation geoLocation, String frId) {
        this.geoLocation = geoLocation;
        this.frId = frId;
    }
}
