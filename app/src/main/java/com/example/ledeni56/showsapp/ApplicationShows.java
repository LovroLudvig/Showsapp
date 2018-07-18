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

        //adding few episodes to "Zabranjena ljubav"
        shows.get(0).addEpisode(new Episode("Episode 1", "Description 1"));
        shows.get(0).addEpisode(new Episode("Episode 2", "Description 2"));
        shows.get(0).addEpisode(new Episode("Episode 3", "Description 3"));
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
    public ArrayList<Episode> getEpisodesOfShow(int id){
        for (Show show:shows){
            if (show.getID()==id){
                return show.getEpisodes();
            }
        }
        return new ArrayList<>();
    }
}
