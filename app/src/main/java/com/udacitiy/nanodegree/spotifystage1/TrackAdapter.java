package com.udacitiy.nanodegree.spotifystage1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by Yash on 6/30/2015.
 */
public class TrackAdapter extends ArrayAdapter<Track> {
    Context con;
    public TrackAdapter(Context context, List<Track> objects) {
        super(context, 0, objects);
        con=context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Track track=getItem(position);
        AlbumSimple albumSimple=track.album;
        if(convertView==null){
            convertView= LayoutInflater.from(con).inflate(R.layout.track_list_item, parent, false);
        }
        ImageView image=(ImageView) convertView.findViewById(R.id.track_image);
        if(albumSimple.images!=null && albumSimple.images.size()>0){
            Picasso.with(con).load(albumSimple.images.get(0).url).into(image);
        }
        TextView nameTv=(TextView) convertView.findViewById(R.id.track_name_textview);
        nameTv.setText(track.name);
        return convertView;
    }
}
