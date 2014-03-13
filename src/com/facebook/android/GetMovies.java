package com.facebook.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;



public class GetMovies extends Activity
{
	private List<Movie> movieList = new ArrayList<Movie>(); 
	private int movieItem;
	private String movieResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.getmovies);
        
        try
		{
            Bundle bundle = this.getIntent().getExtras();
            String movieTitle = bundle.getString("title");
            String titleType = bundle.getString("type");
            String title = movieTitle.replaceAll("\\s{1,}", "+"); //replace all blank to +
            // deal with the UTF-8 code
            // convert the type string for servlet
            if (titleType.equals("All Types"))
			{
            	titleType = "feature,tv_series,game";
			}
            else if (titleType.equals("Feature Film"))
			{
            	titleType = "feature";
			}
            else if (titleType.equals("TV Series"))
			{
            	titleType = "tv_series";
			}
            else if (titleType.equals("Video Game"))
			{
            	titleType = "game";
			}
            //Log.d("MySecondPage", "movie is: "+title);
            //Log.d("MySecondPage", "type is: "+titleType);
        	// get movie result from servlet, movieResult is a string in json format
            movieResult = getResult(title, titleType);
            //Log.d("MySecondPage", "empty result is: "+movieResult);
            if (movieResult.equals("{\"results\":{\"result\":[{}]}}"))
            {
            	Movie movie = new Movie(); 
            	movie.setCover("");
    		    movie.setTitle("");
    		    movie.setYear("");
    		    movie.setDirector("");
    		    movie.setRating("");
    		    movie.setDetails("");
    		    movieList.add(movie);
            }
            else 
            {
	            try
				{
	            	JSONObject jsonObject = new JSONObject(movieResult).getJSONObject("results");
	        		JSONArray movieArray = jsonObject.getJSONArray("result");
	        		for (int i = 0; i < movieArray.length(); i++)
	        		{ 
	        		    JSONObject movieObj = (JSONObject)movieArray.opt(i); 
	        		    Movie movie = new Movie(); 
	        		    movie.setCover(movieObj.getString("cover"));
	        		    movie.setTitle(movieObj.getString("title"));
	        		    movie.setYear(movieObj.getString("year"));
	        		    movie.setDirector(movieObj.getString("director"));
	        		    movie.setRating(movieObj.getString("rating"));
	        		    movie.setDetails(movieObj.getString("details"));
	        		    movieList.add(movie); 
	        		}
	        		
				} catch (JSONException e)
				{
					// TODO: handle exception
					e.printStackTrace();
				}
            }
	        // bind data to movie adapter, set this adapter to list view
	        ListView movieListView =  (ListView) findViewById(R.id.list);
			MovieAdapter mAdapter = new MovieAdapter(getApplicationContext(), R.layout.movieresult, movieList);
            movieListView.setAdapter(mAdapter);
            // Create a message handling object as an anonymous class.
            movieListView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    // Do something in response to the click
                	movieItem = (int)id;
                	// Launching new Activity on selecting single List Item
                	Intent i = new Intent(getApplicationContext(), MovieItem.class);
                	// sending data to new activity
                	i.putExtra("cover", movieList.get(movieItem).getCover());
            		i.putExtra("title", movieList.get(movieItem).getTitle());
            		i.putExtra("year", movieList.get(movieItem).getYear());
            		i.putExtra("director", movieList.get(movieItem).getDirector());
            		i.putExtra("rating", movieList.get(movieItem).getRating());
            		i.putExtra("details", movieList.get(movieItem).getDetails());
            		GetMovies.this.startActivity(i);
                }
            });
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public String getResult(String title, String type) throws Exception
	{
		// invoke servlet
		String urlStr = "http://cs-server.usc.edu:25289/examples/servlet/TestServlet?title="+title+"&title_type="+type;
		URL url = new URL(urlStr);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setAllowUserInteraction(false);
		InputStream urlStream = url.openStream();
		
		// read the result from the servlet
		BufferedReader inFile = new BufferedReader(new InputStreamReader(urlStream));
	    StringBuffer buffer = new StringBuffer();
	    String line = "";
		while ((line = inFile.readLine()) != null)
		{
			 buffer.append(line);
	    }
		String result = buffer.toString();
		return result;
	}
}
