package com.example.heshammuhammed.reminder.DTO;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by HeshamMuhammed on 3/5/2018.
 */

public class Trip implements Serializable {

    private int tripId;
    private String tripName;
    private double startPointLatitude;
    private double startPointLongitude;
    private double endPointLatitude;
    private double endPointLongitude;
    private String startPointName;
    private String endPointName;
    private Date startDate;
    private Date endDate;
    private String status;
    private double distance;
    private String imageLink;
    private String roundTrip;

    public Trip() {

    }

    public Trip(String tripName, double startPointLatitude, double startPointLongitude, double endPointLatitude, double endPointLongitude, String startPointName, String endPointName, Date startDate, Date endDate, String status, double distance, String imageLink) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.startPointLatitude = startPointLatitude;
        this.startPointLongitude = startPointLongitude;
        this.endPointLatitude = endPointLatitude;
        this.endPointLongitude = endPointLongitude;
        this.startPointName = startPointName;
        this.endPointName = endPointName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.distance = distance;
        this.imageLink = imageLink;
    }

    public Trip(String tripName, double startPointLatitude, double startPointLongitude, double endPointLatitude, double endPointLongitude, String startPointName, String endPointName, Date startDate, Date endDate, String status, double distance, String imageLink, String roundTrip) {
        this.tripName = tripName;
        this.startPointLatitude = startPointLatitude;
        this.startPointLongitude = startPointLongitude;
        this.endPointLatitude = endPointLatitude;
        this.endPointLongitude = endPointLongitude;
        this.startPointName = startPointName;
        this.endPointName = endPointName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.distance = distance;
        this.imageLink = imageLink;
        this.roundTrip = roundTrip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public double getStartPointLatitude() {
        return startPointLatitude;
    }

    public void setStartPointLatitude(double startPointLatitude) {
        this.startPointLatitude = startPointLatitude;
    }

    public double getStartPointLongitude() {
        return startPointLongitude;
    }

    public void setStartPointLongitude(double startPointLongitude) {
        this.startPointLongitude = startPointLongitude;
    }

    public double getEndPointLatitude() {
        return endPointLatitude;
    }

    public void setEndPointLatitude(double endPointLatitude) {
        this.endPointLatitude = endPointLatitude;
    }

    public double getEndPointLongitude() {
        return endPointLongitude;
    }

    public void setEndPointLongitude(double endPointLongitude) {
        this.endPointLongitude = endPointLongitude;
    }

    public String getStartPointName() {
        return startPointName;
    }

    public void setStartPointName(String startPointName) {
        this.startPointName = startPointName;
    }

    public String getEndPointName() {
        return endPointName;
    }

    public void setEndPointName(String endPointName) {
        this.endPointName = endPointName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRoundTrip() {
        return roundTrip;
    }

    public void setRoundTrip(String roundTrip) {
        this.roundTrip = roundTrip;
    }
}