package com.example.ledeni56.showsapp.Entities;



import java.util.ArrayList;


public class Show {
    private String id;
    private String name;
    private String description;
    private ArrayList<Episode> episodes;
    private String pictureUrl;



    public Show(String id, String name,String description, String url){
        this.id=id;
        this.name=name;
        this.description=description;
        this.episodes=new ArrayList<>();
        this.pictureUrl ="https://api.infinum.academy"+url;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return id;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Show) {
            return this.id.equals(((Show) obj).getID());
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
