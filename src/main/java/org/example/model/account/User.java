package org.example.model.account;

import org.example.model.content.Comment;
import org.example.model.content.Song;
import org.example.model.request.LyricEditRequest;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class User extends Account {

    private List<String> followedArtists = new ArrayList<>();
    private List<String> favoriteSongs = new ArrayList<>();
    private List<String> likedSongs = new ArrayList<>();
    private List<String> dislikedSongs = new ArrayList<>();

    public User() {setAccountType("USER");}
    public User(String name , String lastName , String username , String email , String password) {
        super(name , lastName , username , email , password);
        setAccountType("USER");
        setId(UUID.randomUUID().toString());
    }

    // getters
    public List<String> getFavoriteSongs() {
        return favoriteSongs;
    }

    public List<String> getFollowedArtists() {
        return followedArtists;
    }

    public List<String> getLikedSongs() { return likedSongs; }

    public List<String> getDislikedSongs() { return dislikedSongs; }

    // setters

    public void setFavoriteSongs(List<String> favoriteSongs) {
        this.favoriteSongs = favoriteSongs;
    }

    public void setFollowedArtists(List<String> followedArtists) {
        this.followedArtists = followedArtists;
    }

    public void setLikedSongs(List<String> likedSongs) { this.likedSongs = likedSongs; }

    public void setDislikedSongs(List<String> dislikedSongs) { this.dislikedSongs = dislikedSongs; }

    // add comment
    public void addComment(Song song) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the following comment: ");
        String text = scanner.nextLine();
        Comment comment = new Comment(text , this.getId() , song.getId());
        List<String> comments = song.getComments();
        comments.add(comment.getId());
        commentRepo.save(comment);
        musicRepo.save(song);
    }

    // lyric edit request
    public void sendRequest(Song song) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter lyrics (type 'END' on empty line to finish):");
        StringBuilder lyrics = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("END") || line.isEmpty()) {
                break;
            }
            lyrics.append(line).append("\n");
        }
        LyricEditRequest request = new LyricEditRequest(getId() , song.getArtist() , song.getId() , lyrics.toString());
        requestRepo.save(request);
        List<String> requests = song.getRequests();
        requests.add(request.getId());
        song.setRequests(requests);
        musicRepo.save(song);
        Admin admin = (Admin) userRepo.findByUserName("admin").get();
        admin.addRequest(request);
        Artist artist = (Artist) userRepo.findById(musicRepo.findById(request.getSong()).get().getArtist()).get();
        artist.addRequest(request);
        userRepo.save(artist);
    }

    // view favorite songs
    public void viewFavoriteSongs() {
        List<Song> favoriteSongs = favoriteSongsInList();
        int counter = 1;
        for (Song song : favoriteSongs) {
            System.out.println(counter + ". " + song.getTitle() + " from " + userRepo.findById(song.getArtist()).get().getUsername());
            counter++;
        }
    }

    public void addToFavoriteSongs(String songId) {
        if (favoriteSongs.contains(songId)) {
            System.out.println("this song is already in your favorites");
        }
        else
        {
            favoriteSongs.add(songId);
            userRepo.save(this);
        }
    }

    // follow and unfollow
    public void followArtist(Artist artist) {
        if(followedArtists.contains(artist.getUsername())) {
            System.out.println("you are already following this artist");
        }
        else {
            this.followedArtists.add(artist.getId());
            userRepo.save(artist);
            artist.setFollowerCount(artist.getFollowerCount() + 1);
            userRepo.save(artist);
        }
    }
    public void unfollowArtist(Artist artist) {
        if(followedArtists.contains(artist.getUsername())) {
            System.out.println("you haven't follow this artist yet");
        }
        else {
            this.followedArtists.remove(artist.getId());
            userRepo.save(artist);
            artist.setFollowerCount(artist.getFollowerCount() - 1);
            userRepo.save(artist);
        }
    }

    // like and dislike
    public void likeSong(Song song) {
        if (this.likedSongs.contains(song.getId())) {
            System.out.println("You already liked this song.");
        }
        else {
            song.setLikes(song.getLikes() + 1);
            likedSongs.add(song.getId());
            musicRepo.save(song);
            userRepo.save(this);
        }
    }
    public void dislikeSong(Song song) {
        if (this.dislikedSongs.contains(song.getId())) {
            System.out.println("You already disliked this song.");
        }
        else {
            song.setDislikes(song.getDislikes() + 1);
            dislikedSongs.add(song.getId());
            musicRepo.save(song);
            userRepo.save(this);
        }
    }

    private List<Song> favoriteSongsInList() {
        List<Song> favoriteSongs = new ArrayList<Song>();
        for(String song : this.favoriteSongs) {
            musicRepo.findById(song).ifPresent(favoriteSongs::add);
        }
        return favoriteSongs;
    }

    // show profile
    public void showProfile() {
        System.out.println("name : " + getName());
        System.out.println("last name : " + getLastName());
        System.out.println("email : " + getEmail());
        System.out.println("username : " + getUsername());
        System.out.println("userid : " + getId());
    }
}
