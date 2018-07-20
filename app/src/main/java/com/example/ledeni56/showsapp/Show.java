package com.example.ledeni56.showsapp;


import java.util.ArrayList;
import java.util.List;

public class Show {
    private static int counter=0;

    private int id;
    private String name;
    private ArrayList<Episode> episodes;


    public Show(String name){
        this.id=counter++;
        this.name=name;
        this.episodes=new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Show) {
            return this.id==((Show) obj).getID();
        }else{
            return false;
        }
    }


    //adds an episode if one doesn't already exist with the same episode number and season number
    public boolean addEpisode(Episode episode){
        if (episodes.contains(episode)){
            return false;
        }
        else{
            episodes.add(episode);
            return true;
        }
    }

}
