package com.example.geotracker.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.location.Location;

public class Tracker {
	private String trackerName;
	private List<Location> location;
	private Date startDate;
	private Date endDate;
	private Double distance;

	public Tracker() {
		location = new ArrayList<Location>();
	}

	public String getTrackerName() {
		return trackerName;
	}

	public void setTrackerName(String trackerName) {
		this.trackerName = trackerName;
	}

	public List<Location> getLocation() {
		return location;
	}

	public void setLocation(List<Location> location) {
		this.location = location;
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

	public Double getDistance() {
		return distance;
	}

	public void setDistance(double d) {

		this.distance = d;
	}

}
