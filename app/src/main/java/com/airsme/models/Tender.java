package com.airsme.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 12/11/2017.
 */

public class Tender extends Model {
    private String ID;
    private String tenderno;
    private String businessUid;
    private Date date;
    private String time;
    private String building;
    private String unit;
    private String floor;
    private String street;
    private String surbub;
    private String town;
    private String contact;
    private String email;


    private String status="Pending";
    private String name="New Tender";


    private String notes;
    private String location;
    private String contactperson;
    private String courierOptions;

    boolean compulsoryMeeting=false;
    Date compulsoryMeetingDate;
    String compulsoryMeetingVenue;



    private List<String> interests=new ArrayList<>();

    @Override
    public String getNode() {
        return "tender";
    }

    @Override
    public String getPKeyValue() {
        return ID;
    }

    @Override
    public String getPKeyName() {
        return "tenderno";
    }

    @Override
    public void setPKeyValue(String id) {
        this.ID=id;
    }


    public Tender(String tenderno, String businessUid, Date date, String time, String building,
                  String unit, String floor, String street, String surbub, String town,
                  String contact, String email) {
        this.tenderno = tenderno;
        this.businessUid = businessUid;
        this.date = date;
        this.time = time;
        this.building = building;
        this.unit = unit;
        this.floor = floor;
        this.street = street;
        this.surbub = surbub;
        this.town = town;
        this.contact = contact;
        this.email = email;
        date=new Date();
    }

    public Tender() {
        date=new Date();
    }

    public String getImageURL() {
        return "images/tender/"+getTenderno();
    }
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUniqueLongTenderno() {
        return ID+"/"+tenderno;
    }

    public String getTenderno() {
        return tenderno;
    }

    public void setTenderno(String tenderno) {
        this.tenderno = tenderno;
    }

    public String getBusinessUid() {
        return businessUid;
    }

    public void setBusinessUid(String businessUid) {
        this.businessUid = businessUid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSurbub() {
        return surbub;
    }

    public void setSurbub(String surbub) {
        this.surbub = surbub;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getContactperson() {
        return contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public String getCourierOptions() {
        return courierOptions;
    }

    public void setCourierOptions(String courierOptions) {
        this.courierOptions = courierOptions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void addInterest(String interest) {
        if(!interests.contains(interest))
        this.interests.add(interest);
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public LatLng jgetMaplocation() {
        if(location==null) location="-33.9238445,18.4244628";
        String[] loc = location.replace("lat/lng: (", "").replace(")", "").split(",");
        return new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));
    }

    public void jsetMaplocation(LatLng maplocation) {
        this.location = maplocation.toString();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isCompulsoryMeeting() {
        return compulsoryMeeting;
    }

    public void setCompulsoryMeeting(boolean compulsoryMeeting) {
        this.compulsoryMeeting = compulsoryMeeting;
    }

    public Date getCompulsoryMeetingDate() {
        return compulsoryMeetingDate;
    }

    public void setCompulsoryMeetingDate(Date compulsoryMeetingDate) {
        this.compulsoryMeetingDate = compulsoryMeetingDate;
    }

    public String getCompulsoryMeetingVenue() {
        return compulsoryMeetingVenue;
    }

    public void setCompulsoryMeetingVenue(String compulsoryMeetingVenue) {
        this.compulsoryMeetingVenue = compulsoryMeetingVenue;
    }

    @Override
    public String toString() {
        return "Tender{" +
                "ID='" + ID + '\'' +
                ", tenderno='" + tenderno + '\'' +
                ", businessUid='" + businessUid + '\'' +
                ", date=" + date +
                ", time='" + time + '\'' +
                ", building='" + building + '\'' +
                ", unit='" + unit + '\'' +
                ", floor='" + floor + '\'' +
                ", street='" + street + '\'' +
                ", surbub='" + surbub + '\'' +
                ", town='" + town + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", notes='" + notes + '\'' +
                ", maplocation='" + location + '\'' +
                ", contactperson='" + contactperson + '\'' +
                ", courierOptions='" + courierOptions + '\'' +
                '}';
    }
    public String toPrint() {
        return  "tenderno='" + tenderno + '\'' +'\n'+
                "date=" + date +'\n'+
                "time='" + time + '\'' +'\n'+
                "building='" + building + '\'' +'\n'+
                "unit='" + unit + '\'' +'\n'+
                "floor='" + floor + '\'' +'\n'+
                "street='" + street + '\'' +'\n'+
                "surbub='" + surbub + '\'' +'\n'+
                "town='" + town + '\'' +'\n'+
                "contact='" + contact + '\'' +'\n'+
                "email='" + email + '\'' +'\n'+
                "notes='" + notes + '\'' +'\n'+
                "maplocation='" + location + '\'' +'\n'+
                "contactperson='" + contactperson + '\'' +'\n'+
                "courierOptions='" + courierOptions + '\'';
    }
}
