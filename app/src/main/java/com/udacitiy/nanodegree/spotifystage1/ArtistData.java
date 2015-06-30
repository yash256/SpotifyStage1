package com.udacitiy.nanodegree.spotifystage1;

import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by Yash on 6/23/2015.
 */
public class ArtistData {
    String artistName;
    Image artistImage;
    public ArtistData(String name, Image img){
        artistName=name;
        artistImage=img;
    }
    public ArtistData(String name){
        artistName=name;
        artistImage=null;
    }

}
