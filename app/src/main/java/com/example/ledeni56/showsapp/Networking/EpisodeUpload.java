package com.example.ledeni56.showsapp.Networking;


import com.squareup.moshi.Json;

public class EpisodeUpload {
    @Json(name = "showId")
    private String showId;
    @Json(name = "mediaId")
    private String mediaId;
    @Json(name = "title")
    private String title;
    @Json(name = "description")
    private String description;
    @Json(name = "episodeNumber")
    private String episodeNumber;
    @Json(name = "season")
    private String season;

    public EpisodeUpload(String showId, String mediaId, String title, String description, String episodeNumber, String season){
        this.showId=showId;
        this.mediaId=mediaId;
        this.title=title;
        this.description=description;
        this.episodeNumber=episodeNumber;
        this.season=season;
    }

}
