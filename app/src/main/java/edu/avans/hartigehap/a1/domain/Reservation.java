package edu.avans.hartigehap.a1.domain;

import java.util.Date;

import edu.avans.hartigehap.a1.timepicker.Time;

public class Reservation {
    private String restaurant;
    private Date date;
    private Time startTime;
    private Time endTime;
    private int persons;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String comments;

    public Reservation(String restaurant, Date date, Time startTime, Time endTime, int persons, String firstName, String lastName, String emailAddress, String phoneNumber, String comments) {
        this.restaurant = restaurant;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.persons = persons;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.comments = comments;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public Date getDate() {
        return date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public int getPersons() {
        return persons;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getComments() {
        return comments;
    }
}
