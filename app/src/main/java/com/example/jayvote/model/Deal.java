package com.example.jayvote.model;

public class Deal {

    private String title;
    private String votes;
    private String value;

    public String getVoteCast() {
        return VoteCast;
    }

    public void setVoteCast(String voteCast) {
        VoteCast = voteCast;
    }

    private String VoteCast;

    public String getPercentage() {
        return Percentage;
    }

    public void setPercentage(String percentage) {
        Percentage = percentage;
    }

    private String Percentage;

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    private String Count;


    public String getKey() {
        return key;
    }

    public String setKey(String key) {
        this.key = key;
        return key;
    }

    private String key;


    public Deal() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getvotes() {
        return votes;
    }

    public void setvotes(String votes) {
        this.votes = votes;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
