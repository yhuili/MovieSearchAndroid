package com.facebook.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MovieItem extends Activity
{
	private TextView itemTitle;
	private TextView itemYear;
	private TextView itemDirector;
	private TextView itemRating;
	private ImageView itemCover;
	private Bitmap bm;
	private Button fbButton;
	
	private String selectedCover;
	private String selectedTitle;
    private String selectedYear;
    private String selectedDirector;
    private String selectedRating;
    private String selectedDetails;

    private static final String APP_ID = "391068687636092";
    private static final String FACEBOOK_PERMISSION = "publish_stream";
    //private static final String MSG = "Message from FacebookSample";
    private FacebookConnector facebookConnector;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectedmovie);

        if (APP_ID == null) 
        {
            Util.showAlert(this, "Warning", "Facebook Applicaton ID must be " +
                    "specified before running this example: see Example.java");
        }
		
		// create facebook connector
        this.facebookConnector = new FacebookConnector(APP_ID, this, getApplicationContext(), new String[] {FACEBOOK_PERMISSION});
		
		itemTitle = (TextView) findViewById(R.id.itemTitle);
		itemYear = (TextView) findViewById(R.id.itemYear);
		itemDirector = (TextView) findViewById(R.id.itemDirector);
		itemRating = (TextView) findViewById(R.id.itemRating);
		itemCover = (ImageView) findViewById(R.id.itemCover);
		fbButton = (Button) findViewById(R.id.facebookButton);
		
		// get intent
        Intent i = getIntent();
        
        // getting attached intent data
        selectedCover = i.getStringExtra("cover");
        selectedTitle = i.getStringExtra("title");
        selectedYear = i.getStringExtra("year");
        selectedDirector = i.getStringExtra("director");
        selectedRating = i.getStringExtra("rating");
        selectedDetails = i.getStringExtra("details");
        
        // get bitmap
        try 
		{
			URL coverUrl = new URL(selectedCover);
			HttpURLConnection coverURLConnection = (HttpURLConnection)coverUrl.openConnection();
			coverURLConnection.setDoInput(true);
			//imgURLConnection.connect();   
			if (coverURLConnection.getResponseCode() == 200) 
			{  
				InputStream is = coverURLConnection.getInputStream();
				bm = BitmapFactory.decodeStream(is);
			}
		} 
		catch (MalformedURLException e) {
        	e.printStackTrace();
        }
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
        
        // displaying selected product name
        itemCover.setImageBitmap(bm);
        itemTitle.setText("\nName: "+selectedTitle+" ("+selectedYear+")"+"\n");
        itemYear.setText("Year: "+selectedYear+"\n");
        itemDirector.setText("Director: "+selectedDirector+"\n");
        itemRating.setText("Rating: "+selectedRating+"\n");
        
        
        // facebook button
        fbButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	postToFeed();
            }            
        });
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        facebookConnector.getFacebook().authorizeCallback(requestCode, resultCode, data);
    }
	
	public void postFacebookMsg() 
	{
		// edit information for sending
		Bundle infoBundle = new Bundle(); 
	    infoBundle.putString("method", "feed");
	    infoBundle.putString("link", selectedDetails);
	    infoBundle.putString("picture", selectedCover);
	    infoBundle.putString("name", selectedTitle);
	    infoBundle.putString("caption", "I am interested in this movie/series/game");
	    infoBundle.putString("description", selectedTitle+" ( "+selectedYear+" ) "+" released in "+selectedYear+" has a rating of "+selectedRating);
	    
	    try 
	    {
	    	JSONObject review = new JSONObject();
	        review.put("text", "here");
	        review.put("href", selectedDetails+"reviews");
	        JSONObject properties = new JSONObject();
	        properties.put("Look at user reviews", review);
	        infoBundle.putString("properties", properties.toString());
	    } catch (JSONException e) {
	    	e.printStackTrace();
	    }
	    facebookConnector.getFacebook().dialog(MovieItem.this, "feed", infoBundle, new SampleDialogListener());
	}	
	
	public void postToFeed()
	{
        if (facebookConnector.getFacebook().isSessionValid()) 
        {
        	postFacebookMsg();
		} 
        else 
        {
			SessionEvents.AuthListener listener = new SessionEvents.AuthListener() 
			{
				@Override
				public void onAuthSucceed() 
				{
					postFacebookMsg();
				}
				@Override
				public void onAuthFail(String error) {
				}
			};
			SessionEvents.addAuthListener(listener);
			facebookConnector.login();
		}
	}
	
	public class WallPostRequestListener extends BaseRequestListener 
	{
        public void onComplete(final String response, final Object state) {
            Log.d("Facebook-Example", "Got response: " + response);
            String message = "is Success!!!";
            try {
                JSONObject json = Util.parseJson(response);
                message = json.getString("message");
            } catch (JSONException e) {
                Log.w("Facebook-Example", "JSON Error in response");
            } catch (FacebookError e) {
                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
            }
            
            final String text = "Your Wall Post: " + message;
            MovieItem.this.runOnUiThread(new Runnable() {
                public void run() {
                	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MovieItem.this);
            		// set title
            		alertDialogBuilder.setTitle("Alert Message");
            		alertDialogBuilder.setMessage(text)
            						  .setNeutralButton("OK", new DialogInterface.OnClickListener() {
            							  public void onClick(DialogInterface dialog, int id) {
            							  }  
            						  });
            		// create alert dialog
    				AlertDialog alertDialog = alertDialogBuilder.create();
    				// show it
    				alertDialog.show();
                }
            });
        }
    }
	
	public class SampleDialogListener extends BaseDialogListener 
	{
        public void onComplete(Bundle values) 
        {
            final String postId = values.getString("post_id");
            if (postId != null) 
            {
                Log.d("Facebook-Example", "Dialog Success! post_id=" + postId);
                facebookConnector.getAsyncRunner().request(postId, new WallPostRequestListener());
            } else {
                Log.d("Facebook-Example", "No wall post made");
            }
        }
    }
}
