package com.example.ledeni56.showsapp.Static;

import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Entities.Show;

import java.util.ArrayList;

public class ApplicationShows {
    private static ArrayList<Show> shows=new ArrayList<>();

    static {
        //creating harcoded shows
//        shows.add(new Show("Zabranjena ljubav"));
//        shows.add(new Show("Ljubav je na selu"));
//        shows.add(new Show("Gossip Girl"));
//        shows.add(new Show("Pokemon"));
//        shows.add(new Show("Naruto"));
//        shows.add(new Show("Seks i grad"));
//        shows.add(new Show("Vampirski dnevnici"));

        //shows.get(0).addEpisode(new Episode("test name", "test description", 1, 1, Uri.parse("http://halobing.net/serije/slike/zabranjenaljubav1.jpg" ) ));

    }

    public static ArrayList<Show> getShows() {
        return shows;
    }

    public static void addShow(Show show){
        if (!shows.contains(show)){
            shows.add(show);
        }
    }

    public static boolean addEpisodeToShow(Episode ep, String id){
        for (Show show:shows){
            if (show.getID().equals(id)){
                return show.addEpisode(ep);
            }
        }
        return false;
    }

    public static ArrayList<Episode> showEpisodes(String id){
        for (Show show:shows){
            if (show.getID().equals(id)){
                return show.getEpisodes();
            }
        }
        return new ArrayList<>();
    }

    public static Show getShow(String id){
        for (Show show:shows){
            if (show.getID().equals(id)){
                return show;
            }
        }
        return null;
    }

    public static boolean nameExists(String name, String id) {
        for (Show show:shows){
            if (show.getID().equals(id)){
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
