package com.facebook.android;

public class Movie
{
	private String cover;
	private String title;
	private String year;
	private String director;
	private String rating;
	private String details;
	
	public Movie()
	{
		// TODO Auto-generated constructor stub
	}
	
	public Movie(String cover, String title, String year, String director, String rating, String details)
	{
		// TODO Auto-generated constructor stub
		this.cover = cover;
		this.title = title;
		this.year = year;
		this.director = director;
		this.rating = rating;
		this.details = details;
	}
	
	// Getter and setter methods for all the fields.
    // Though we would not be using the setters for this example,
    // it might be useful later.
    public String getCover() 
    {
            return cover;
    }
    public void setCover(String cover) 
    {
            this.cover = cover;
    }
    
    public String getTitle() 
    {
            return title;
    }
    public void setTitle(String title) 
    {
            this.title = title;
    }
    
    public String getDirector() 
    {
            return director;
    }
    public void setDirector(String director) 
    {
            this.director = director;
    }
    
    public String getRating() 
    {
            return rating;
    }
    public void setRating(String rating) 
    {
            this.rating = rating;
    }
    
    public String getDetails() 
    {
            return details;
    }
    public void setDetails(String details) 
    {
            this.details = details;
    }
    
    public String getYear() 
    {
            return year;
    }
    public void setYear(String year) 
    {
            this.year = year;
    }
}
