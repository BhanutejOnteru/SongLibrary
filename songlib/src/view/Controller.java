/*
 * Bhanutej Onteru
 * Deepika Onteru
 */

package view;

import app.Song;
import java.io.*;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
//import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Controller {
	//@FXML ListView <Song> songlist;
	@FXML Button add;
    @FXML Button edit;
	@FXML Button delete;
	@FXML GridPane gridpane;
	//@FXML ListView <String> listView;
	@FXML TextField titleIF;
	@FXML TextField artistIF;
	@FXML TextField albumIF;
	@FXML TextField yearIF;

	
	@FXML Label titleLabel;
	@FXML Label artistLabel;
	@FXML Label albumLabel;
	@FXML Label yearLabel;
	
	ObservableList<Song> songs = FXCollections.observableArrayList();
	@FXML ListView<Song> listView;
	
	private Song selected;
	private boolean ediit;
	
	@FXML
	void initialize() {
		
		// set listener for the items
		listView.getSelectionModel().selectedIndexProperty().addListener((songs, oldVal, newVal) -> showSongs());
		
		BufferedReader buffRead = null;
		
		try {
			File file = new File("src/songData.csv");
			
			if(!file.exists()) {
				file.createNewFile();
			}
			
			buffRead = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = buffRead.readLine()) != null) {
				//each line in csv is a song split w "," for its fields
				String [] parse = line.split(",");
				//send in fields to constructor 
				Song song = new Song(parse[0], parse[1], parse[2], parse[3]);
				//add to our observableList songs
				songs.add(song);
			}
			
			listView.setItems(songs);
			
			if(!songs.isEmpty()) {
				selected = songs.get(0);
				listView.requestFocus();
				listView.getSelectionModel().select(0);
				listView.getFocusModel().focus(0);
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
		finally {
			try {
				buffRead.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void showSongs() {
		Song song = listView.getSelectionModel().getSelectedItem();
		selected = song;
		
		if (selected == null) {
			return;
		}
		else {
			
				titleIF.setText(selected.getTitle());
				artistIF.setText(selected.getArtist());
				albumIF.setText(selected.getAlbum());
				yearIF.setText(selected.getYear());
				
		}
	}
	
	@FXML
	public void handleAdd(ActionEvent e) {
		System.out.println(titleIF.getText());
		System.out.println(artistIF.getText());
		
		ediit = false;
		
		//get all the song details 
		//you need to have title and artist other fields can be empty
		if(titleIF.getText().isEmpty() || artistIF.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText("Mandatory fields are missing");
			alert.setContentText("Title and artist must be provided to add a song.");
			alert.showAndWait();
			System.out.print("You are missing mandatory fields");
			return;
			
		}
		
		Song toAdd = new Song(titleIF.getText(), artistIF.getText(),
								albumIF.getText(), yearIF.getText());
		
		handleAddHelper(toAdd);
	}
	
	public int handleAddHelper(Song toAdd) {
		//add to listView in alphabetical order
		//check for duplicates 
		//sort by title name first then by artist 
		
		int idx = findSongIndex(songs,toAdd);
		String title = toAdd.getTitle();
		String artist = toAdd.getArtist();
		
		if (idx == -1) {
			//song already exists in playlist
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText("Duplicate");
			alert.setContentText("Song already exists in playlist.");
			alert.showAndWait();
			return -1;
		}
		
		else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("WARNING");
			alert.setHeaderText("Adding/Editing the following song.");
			alert.setContentText("Title: " + title + "	Artist: " + artist);
			
			Optional <ButtonType> res = alert.showAndWait();
			if(res.get()== ButtonType.OK) {
				songs.add(idx, toAdd);
				writeToFile();
				//clear fields
				titleIF.setText("");
				artistIF.setText("");
				albumIF.setText("");
				yearIF.setText("");
				return 0;
			}
			else {
				showSongs();
			}
		}
		return 0;

		
	}
	
		
	public int findSongIndex(ObservableList<Song> songs, Song s) {
		int i;
		for(i = 0; i < songs.size(); i++) {
			if(songs.get(i).compareTo(s)==0) {
				return -1;
			}
			else if(songs.get(i).compareTo(s)>0){
				return i; 
			}
			else {
				continue;
			}
		}
		
		return i;

	}

	
	public void handleDelete(ActionEvent e)
	{
		//if the song list is empty, alert no song in playlist
		
		//selected song, find the index and remove it from the observable list
		
		if (songs.isEmpty())
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText("Playlist is empty");
			alert.setContentText("There are no songs to delete");
			alert.showAndWait();
			
		}
		else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("WARNING");
			alert.setHeaderText("Deleting the selected song...");
			alert.setContentText("Are you sure?");
			
			Optional <ButtonType> res = alert.showAndWait();
			if(res.get()==ButtonType.OK)
			{
				int idxToDel = listView.getSelectionModel().getSelectedIndex();
				songs.remove(idxToDel);
				writeToFile();
				
				// if there are no songs in playList - everything will be set empty.
				if(songs.isEmpty())
				{
					titleIF.setText("");
					artistIF.setText("");
					albumIF.setText("");
					yearIF.setText("");
				}
				// After deleting, the next song should be selection - display the song details
				// If there is no next song, previous song should be selected
				else if (idxToDel == songs.size()-1)
				{
					listView.getSelectionModel().select(idxToDel--);
				}
				else {
					listView.getSelectionModel().select(idxToDel++);
				}
				
			}
			
			return;
		}
		System.out.println(artistIF.getText());
	}
	
	public void handleEdit(ActionEvent e){
		//ANY fields can be changed
		Song songToEdit = listView.getSelectionModel().getSelectedItem();
		Song temp = new Song(titleIF.getText(), artistIF.getText(), albumIF.getText(), yearIF.getText());
		
		//if name and artist conflict with existing song, the edit should not be allowed 
		
		if ((songToEdit.getTitle().compareTo(temp.getTitle())==0) && 
				(songToEdit.getArtist().compareTo(temp.getArtist())==0)){
			listView.getSelectionModel().getSelectedItem().setAlbum(temp.getAlbum());
			listView.getSelectionModel().getSelectedItem().setYear(temp.getYear());
		}
		else {
			songs.remove(listView.getSelectionModel().getSelectedIndex());
			if(handleAddHelper(temp)==-1) {
				handleAddHelper(songToEdit);
				writeToFile();
			}
		}
	/*	System.out.println(titleIF.getText());
		System.out.println(artistIF.getText());
		System.out.println(albumIF.getText());
		System.out.println(yearIF.getText());

	 */
		//selected song should be displayed in textfields
		
		//replace the selected item with new inputfields
	}
	
	public void writeToFile() {
		BufferedWriter buffWrite = null;
		try {
			File file = new File("src/songData.csv");
			
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter fWriter = new FileWriter(file);
			buffWrite = new BufferedWriter(fWriter);
			
			for(Song s: songs) {
				buffWrite.write(s.getTitle()+ "," +s.getArtist()+ "," + s.getYear()+ "," +s.getYear() + ",.\n");
			}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if(buffWrite != null)
					{
						buffWrite.close();
					}
				}
				catch(Exception e) {
					System.out.println("Error when trying to close" + e);
				}
			}
		}
		
		
	
	}

