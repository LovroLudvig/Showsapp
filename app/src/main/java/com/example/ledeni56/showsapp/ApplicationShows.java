package com.example.ledeni56.showsapp;

import java.util.ArrayList;

public class ApplicationShows {
    private static ArrayList<Show> shows=new ArrayList<>();

    static {
        //creating harcoded shows
        shows.add(new Show("Zabranjena ljubav"));
        shows.add(new Show("Ljubav je na selu"));
        shows.add(new Show("Gossip Girl"));
        shows.add(new Show("Pokemon"));
        shows.add(new Show("Naruto"));
        shows.add(new Show("Seks i grad"));
        shows.add(new Show("Vampirski dnevnici"));

    }

    public static ArrayList<Show> getShows() {
        return shows;
    }

    public static boolean addEpisodeToShow(Episode ep, int id){
        for (Show show:shows){
            if (show.getID()==id){
                return show.addEpisode(ep);
            }
        }
        return false;
    }

    //never used but thought it might be useful at some point
    public ArrayList<Episode> showEpisodes(int id){
        for (Show show:shows){
            if (show.getID()==id){
                return show.getEpisodes();
            }
        }
        return new ArrayList<>();
    }

    //returns true if episode with "name" exists in show with id "id"
    public static boolean nameExists(String name, int id) {
        for (Show show:shows){
            if (show.getID()==id){
                for (Episode ep:show.getEpisodes()){
                    if (ep.getName().equals(name)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
