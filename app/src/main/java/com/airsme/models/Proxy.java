package com.airsme.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 12/11/2017.
 */

public class Proxy extends Model {
    private String ID;
    private String uid;
    private String pic;
    private String title;
    private String name;
    private String surname;
    private String education;
    private String languages;
    private String transport;
    private String employmentstatus;
    private String aboutme;
    private String accountname;
    private String accountnumber;
    private String branchcode;
    private String profession;
    private List<String> skills;
    private List<String> qualifications;

    private String contact;
    private String email;
    private String bankName;
    private String address;
    private Date dob;
    private String location;
    private List<String> appliedtenders=new ArrayList<>();

    @Override
    public String getNode() {
        return "proxy";
    }

    @Override
    public String getPKeyValue() {
        return uid;
    }

    @Override
    public String getPKeyName() {
        return "uid";
    }

    @Override
    public void setPKeyValue(String id) {
        this.uid=id;
    }

    public Proxy(String title) {
        this.title = title;
    }

    public Proxy() {
    }

    public Proxy(String pic, String title, String name, String surname, String education,
                 String languages, String transport, String employmentstatus, String aboutme,
                 String accountname, String accountnumber, String branchcode, String professions,
                 List<String> skills, List<String> qualifications) {
        this.pic = pic;
        this.title = title;
        this.name = name;
        this.surname = surname;
        this.education = education;
        this.languages = languages;
        this.transport = transport;
        this.employmentstatus = employmentstatus;
        this.aboutme = aboutme;
        this.accountname = accountname;
        this.accountnumber = accountnumber;
        this.branchcode = branchcode;
        this.profession = profession;
        this.skills = skills;
        this.qualifications = qualifications;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPic() {
        return  "images/proxy/"+getPKeyValue();
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getEmploymentstatus() {
        return employmentstatus;
    }

    public void setEmploymentstatus(String employmentstatus) {
        this.employmentstatus = employmentstatus;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getBranchcode() {
        return branchcode;
    }

    public void setBranchcode(String branchcode) {
        this.branchcode = branchcode;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<String> qualifications) {
        this.qualifications = qualifications;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public List<String> getAppliedtenders() {
        return appliedtenders;
    }

    public void setAppliedtenders(List<String> appliedtenders) {
        this.appliedtenders = appliedtenders;}

    public LatLng jgetMaplocation() {
        if(location==null) location="-33.238445,18.4244628";
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

    @Override
    public String toString() {
        return "Proxy{" +
               // "ID='" + ID + '\'' +"\n"+
                // ", uid='" + uid + '\'' +"\n"+
                //", pic='" + pic + '\'' +"\n"+
                ", title='" + title + '\'' +"\n"+
                ", name='" + name + '\'' +"\n"+
                ", surname='" + surname + '\'' +"\n"+
                ", education='" + education + '\'' +"\n"+
                ", languages='" + languages + '\'' +"\n"+
                ", transport='" + transport + '\'' +"\n"+
                ", employmentstatus='" + employmentstatus + '\'' +"\n"+
                ", aboutme='" + aboutme + '\'' +"\n"+
                ", accountname='" + accountname + '\'' +"\n"+
                ", accountnumber='" + accountnumber + '\'' +"\n"+
                ", branchcode='" + branchcode + '\'' +"\n"+
                ", profession='" + profession + '\'' +"\n"+
                ", skills=" + skills +"\n"+
                ", qualifications=" + qualifications +"\n"+
                ", contact='" + contact + '\'' +"\n"+
                //", email='" + email + '\'' +"\n"+
                ", bankName='" + bankName + '\'' +"\n"+
                ", address='" + address + '\'' +"\n"+
                //", dob=" + dob +"\n"+
                //", appliedtenders=" + appliedtenders +"\n"+
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Proxy proxy = (Proxy) o;

        return uid.equals(proxy.uid);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
