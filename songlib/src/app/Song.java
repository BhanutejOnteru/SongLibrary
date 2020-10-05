/*
 * Bhanutej Onteru
 * Deepika Onteru
 */
package app;

public class Song implements Comparable<Song> {
	
	private String title;
	private String artist;
	private String album;
	private String year;
	
	
	public Song(String title, String artist,String album,String year)
	{
		this.title=title;
		this.artist=artist;
		this.album=album;
		this.year=year;
		
	}
	
	public Song(String title,String artist)
	{
		this.title=title;
		this.artist=artist;
		
	}
	//setters
	public void setTitle(String title)
	{
		this.title=title;
	}
	
	public void setArtist(String artist)
	{
		this.artist=artist;
	}
	
	public void setAlbum(String album)
	{
		this.album=album;
	}

	public void setYear(String year)
	{
		this.year=year;
	}
	//getters
	public String getTitle()
	{
		return this.title;
	}
	
	public String getArtist()
	{
		return this.artist;
	}
	
	public String getAlbum()
	{
		return this.album;
	}

	public String getYear()
	{
		return this.year;
	}
	
	@Override
	public String toString()
	{
		return "Song: "+  title + 
				"		Artist: " + artist + "		Album: " + album + 
				"		Year: " + year;
	}
	
	@Override
	public int compareTo(Song song){
		//sorted by title, for dup title by artist name 
		if(this.title.toUpperCase().compareTo(song.title.toUpperCase())==0) {
			return this.artist.toUpperCase().compareTo(song.artist.toUpperCase());
		}
		else {
			return this.title.toUpperCase().compareTo(song.title.toUpperCase());
		}
		
	}
}
