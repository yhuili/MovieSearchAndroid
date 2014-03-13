package com.facebook.android;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieAdapter extends ArrayAdapter<Movie>
{
	private Context context;
	private List<Movie> Movies;
	// private static final String tag = "MyActivity";
    // Initialize adapter
	public MovieAdapter(Context context, int textViewResourceId,
			List<Movie> objects)
	{
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
        this.Movies = objects;
	}
	
	public Movie getItem(int position) 
	{
        return Movies.get(position);
    }

    public long getItemId(int position) 
    {
        return position;
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
        // Get the current movie object
		Movie movie = getItem(position);

        // Inflate the view
        if(convertView == null)
        {
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            convertView = vi.inflate(R.layout.movieresult, parent, false);
        }
        
        // Get the image from the movieresults.xml file
        ImageView cover = (ImageView) convertView.findViewById(R.id.movieCover);
        // Get the text from the movieresults.xml file     
        TextView title = (TextView) convertView.findViewById(R.id.movieTitle);
        TextView rating = (TextView) convertView.findViewById(R.id.movieRating);
        // Assign the appropriate data from movie object above
        if (movie.getTitle().equals(""))
		{
        	title.setText("No contents found!!!");
		}
        else 
        {
        	title.setText(movie.getTitle()+" ("+movie.getYear()+")");
    		rating.setText("Rates: "+movie.getRating());
    		try 
    		{
    			URL imgUrl = new URL(movie.getCover());
    			HttpURLConnection imgURLConnection = (HttpURLConnection)imgUrl.openConnection();
    			imgURLConnection.setDoInput(true);
    			//imgURLConnection.connect();   
    			if (imgURLConnection.getResponseCode() == 200) 
    			{  
    				InputStream inputStream = imgURLConnection.getInputStream();
    				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
    				cover.setImageBitmap(bitmap);
    				inputStream.close();
    			}
    		} 
    		catch (MalformedURLException e) {
            	e.printStackTrace();
            }
    		catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();	
    		}
		}
		return convertView;
	}
}
