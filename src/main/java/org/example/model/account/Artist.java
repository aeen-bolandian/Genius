package org.example.model.account;

import org.example.model.content.Album;
import org.example.model.content.Song;
import org.example.model.notification.Notification;
import org.example.model.request.LyricEditRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class Artist extends Account {
    private List<String> songs = new ArrayList<String>();
    private List<String> albums = new ArrayList<>();
    private List<String> followers = new ArrayList<>();
    private List<String> requests = new ArrayList<>();

    private int followerCount;

    public Artist() {setAccountType("ARTIST");}
    public Artist(String name , String lastName , String username , String email , String password) {
        super(name, lastName, username, email, password);
        setAccountType("ARTIST");
        setId(UUID.randomUUID().toString());
        followerCount = 0;
    }

    // getters :

    public List<String> getAlbums() {
        return albums;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public List<String> getRequests() {
        return requests;
    }

    public List<String> getSongs() {
        return songs;
    }

    public int getFollowerCount() { return followerCount; }

    // setters :

    public void setAlbums(List<String> albums) {
        this.albums = albums;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public void setRequests(List<String> requests) {
        this.requests = requests;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }

    public void setFollowerCount(int followerCount) { this.followerCount = followerCount; }

    // songs and albums :
    public void publishSong(Song song){
        songs.add(song.getId());
        musicRepo.save(song);
        notifyFollowers("new song" , song.getTitle() + " from " + userRepo.findById(song.getArtist()) + " is now on GENIUS!");
    }
    public void unpublishSong(Song song){
        songs.remove(song.getId());
        musicRepo.delete(song.getId());
    }
    public void publishAlbum(Album album){
        albums.add(album.getId());
        albumRepo.save(album);
        notifyFollowers("new album" , album.getTitle() + " from " + userRepo.findById(album.getArtist()) + " is now on GENIUS!");
    }
    public void unpublishAlbum(Album album){
        albums.remove(album.getId());
        albumRepo.delete(album.getId());
    }

    private void notifyFollowers(String title , String body){
        for(String follower : followers){
            Notification notification = new Notification(getId(), follower, title, body);
            notificationRepo.save(notification);
            userRepo.save(this);
        }
    }

    // manage lyric edit requests :
    public void ViewRequests() {
        int counter = 1;
        for(LyricEditRequest request : requestsInList()) {
            System.out.print(counter + ". request for ");
            if(musicRepo.findById(request.getSong()).isPresent()) {
                System.out.println(musicRepo.findById(request.getSong()).get().getTitle());
            }
            counter++;
        }
    }

    public void approveRequest(LyricEditRequest request) {
        request.setStatus(LyricEditRequest.Status.APPROVED);
        requestRepo.save(request);
        musicRepo.findById(request.getSong()).ifPresent(music -> {
            music.setLyrics(request.getLyric());
            musicRepo.save(music);
        });

        System.out.println("request has been approved");
        Notification notification = new Notification(getId() , request.getRecipient() , "request has been approved" , "your request for " + musicRepo.findById(request.getSong()).get().getTitle() + "has been approved");
        userRepo.findById(request.getRecipient()).ifPresent(user -> { user.addnotification(notification);});

    }

    public void addRequest(LyricEditRequest request) {
        requests.add(request.getId());
        userRepo.save(this);
    }

    public void rejectRequest(LyricEditRequest request) {
        request.setStatus(LyricEditRequest.Status.REJECTED);
        requestRepo.save(request);
        System.out.println("request has been rejected");
        Notification notification = new Notification(getId() , request.getRecipient() , "request has been rejected" , "your request for " + musicRepo.findById(request.getSong()).get().getTitle() + "has been rejected");
        userRepo.findById(request.getRecipient()).ifPresent(user -> { user.addnotification(notification);});
    }

    // view songs
    public void showMostViewedSongs(){
        List<Song> songs = songsInList();
        sortSongsByView(songs);
        int counter = 1;
        for(Song song : songs){
            System.out.println(counter + ". " + song.getTitle());
            counter++;
        }
    }
    public void showMostHatedSongs(){
        List<Song> songs = songsInList();
        sortSongsByDislikes(songs);
        int counter = 1;
        for(Song song : songs){
            System.out.println(counter + ". " + song.getTitle());
        }
    }
    public void showMostLikedSongs(){
        List<Song> songs = songsInList();
        sortSongsByLikes(songs);
        int counter = 1;
        for(Song song : songs){
            System.out.println(counter + ". " + song.getTitle());
        }
    }
    public void showNewestSongs(){
        List<Song> songs = songsInList();
        sortSongsByDate(songs);
        int counter = 1;
        for(Song song : songs){
            System.out.println(counter + ". " + song.getTitle());
        }
    }

    // view albums
    public void showAlbums(){
        List<Album> albums = albumsInList();
        sortAlbumsByDate(albums);
        int counter = 1;
        for(Album album : albums){
            System.out.println(counter + ". " + album.getTitle());
        }
    }

    // sort songs
    public void sortSongsByView(List<Song> songs){
        songs.sort((song1 , song2) -> Long.compare(song2.getViews() , song1.getViews()));
    }
    public void sortSongsByLikes(List<Song> songs){
        songs.sort((song1 , song2) -> Long.compare(song2.getLikes() , song1.getLikes()));
    }
    public void sortSongsByDislikes(List<Song> songs){
        songs.sort((song1 , song2) -> Long.compare(song2.getDislikes() , song1.getDislikes()));
    }
    public void sortSongsByDate(List<Song> songs){
        songs.sort(Comparator.comparing(Song::getPublishDate).reversed());
    }

    // sort albums
    public void sortAlbumsByDate(List<Album> albums){
        albums.sort(Comparator.comparing(Album::getPublishDate).reversed());
    }

    private List<LyricEditRequest> requestsInList() {
        List<LyricEditRequest> requests = new ArrayList<>();
        for (String request : this.requests) {
            requestRepo.findById(request).ifPresent(requests::add);
        }
        return requests;
    }

    private List<Song> songsInList() {
        List<Song> songs = new ArrayList<>();
        for (String song : this.songs) {
            musicRepo.findById(song).ifPresent(songs::add);
        }
        return songs;
    }

    private List<Album> albumsInList() {
        List<Album> albums = new ArrayList<>();
        for (String album : this.albums) {
            albumRepo.findById(album).ifPresent(albums::add);
        }
        return albums;
    }

    public void showProfile(){
        System.out.println("name : " + getName());
        System.out.println("last name : " + getLastName());
        System.out.println("username : " + getUsername());
        System.out.println("email : " + getEmail());
        System.out.println("id : " + getId());
    }
}
