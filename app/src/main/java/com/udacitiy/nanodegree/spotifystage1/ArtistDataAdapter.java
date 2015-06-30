package com.udacitiy.nanodegree.spotifystage1;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by Yash on 6/23/2015.
 */
public class ArtistDataAdapter extends ArrayAdapter<Artist> {

    Context con;
    public ArtistDataAdapter(Context context, List<Artist> objects) {
        super(context, 0, objects);
        con=context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Artist artist=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.artist_list_item, parent, false);
        }
        ImageView image=(ImageView) convertView.findViewById(R.id.artist_image);
        if(artist.images!=null && artist.images.size()>0){
            Log.d("ArrayAdapter", "image not null");
            Picasso.with(con).load(artist.images.get(0).url).into(image);
        }
        TextView nameTv=(TextView) convertView.findViewById(R.id.artist_name_textview);
        nameTv.setText(artist.name);
        return convertView;
    }
}
