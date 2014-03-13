/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.android;

//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;

//import org.json.JSONException;
//import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;

//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

//import com.facebook.android.SessionEvents.AuthListener;
//import com.facebook.android.SessionEvents.LogoutListener;


public class Example extends Activity {

    // Your Facebook Application ID must be set before running this example
    // See http://www.facebook.com/developers/createapp.php
    //public static final String APP_ID = "391068687636092";

    private EditText mKeyWord; //search text
    private Spinner mSpinner; //spinner
	private Button mSearchButton; //search button
    
    private String movieTitle;
    private String titleType;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        // set up search box
        mKeyWord = (EditText) findViewById(R.id.editText);
        
        // set up search spinner
        mSpinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.title_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
        
        //set up search button
        mSearchButton = (Button) findViewById(R.id.searchButton);
        
        mSearchButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	movieTitle = mKeyWord.getText().toString();
            	titleType = mSpinner.getSelectedItem().toString();
            	if (movieTitle.length() == 0 || movieTitle == " ")
            	{
            		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Example.this);
            		// set title
            		alertDialogBuilder.setTitle("Alert Message");
            		alertDialogBuilder.setMessage("Please enter the Title!!!")
            						  .setNeutralButton("OK", new DialogInterface.OnClickListener() {
            							  public void onClick(DialogInterface dialog, int id) {
            								  //Example.this.finish();
            							  }  
            						  });
            		// create alert dialog
    				AlertDialog alertDialog = alertDialogBuilder.create();
    				// show it
    				alertDialog.show();
            	}
            	else
            	{
	            	// using intent to jump to SearchResult class
	            	Intent intent = new Intent();
	            	intent.setClass(Example.this, GetMovies.class);
	            	
	            	Bundle bundle = new Bundle();
	            	bundle.putString("title", movieTitle);
	            	bundle.putString("type", titleType);
	            	//Log.d("MyMainPage", "movie is: "+movieTitle);
	            	//Log.d("MyMainPage", "type is: "+titleType);
	            	//Log.d("MyMainPage", "search click successful!");
	            	intent.putExtras(bundle);     	
	            	startActivity(intent);
            	}
            }
        });
    }
}
