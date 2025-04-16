package org.example.model.content;

import org.example.model.account.Account;
import org.example.model.account.Admin;
import org.example.model.account.Artist;
import org.example.model.account.User;
import org.example.model.notification.Notification;
import org.example.model.request.LyricEditRequest;
import org.example.repository.InMemoryRepository;

import java.time.LocalDateTime;
import java.util.*;

public class Album {

    InMemoryRepository<Account, String> userRepo;
    InMemoryRepository<Album, String> albumRepo;
    InMemoryRepository<Song, String> musicRepo;
    InMemoryRepository<Notification, String> notificationRepo;
    InMemoryRepository<LyricEditRequest, String> requestRepo;
    InMemoryRepository<Comment, String> commentRepo;

    public void setAlbumRepo(InMemoryRepository<Album, String> albumRepo) {
        this.albumRepo = albumRepo;
    }

    public void setCommentRepo(InMemoryRepository<Comment, String> commentRepo) {
        this.commentRepo = commentRepo;
    }

    public void setMusicRepo(InMemoryRepository<Song, String> musicRepo) {
        this.musicRepo = musicRepo;
    }

    public void setNotificationRepo(InMemoryRepository<Notification, String> notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    public void setRequestRepo(InMemoryRepository<LyricEditRequest, String> requestRepo) {
        this.requestRepo = requestRepo;
    }

    public void setUserRepo(InMemoryRepository<Account, String> userRepo) {
        this.userRepo = userRepo;
    }

    private String id;
    private String title;
    private String artist;
    private Date publishDate;
    // set publish date in publish album method on artist class

    private List<String> songs = new ArrayList<>();

    public Album() { }
    public Album(String title, String artist, List<String> songs) {
        this.title = title;
        this.artist = artist;
        this.songs = songs;
        id = UUID.randomUUID().toString();
        publishDate = new Date();
    }
    // getters
    public String getArtist() {
        return artist;
    }

    public String getId() {
        return id;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public List<String> getSongs() {
        return songs;
    }

    public String getTitle() {
        return title;
    }

    // setters

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // show to each type of account
    public void showToUser(User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Title: " + getTitle());
        System.out.println("Artist: " + getArtist());
        System.out.println("Publish Date: " + getPublishDate().toString());
        System.out.println(songs.size() + " Songs");
        List<Song> songsList = songsInList();
        int counter = 1;
        for (Song song : songsList) {
            System.out.println(counter + ". " + song.getTitle());
        }
        while(true) {
            System.out.println("which one are you looking for?");
            System.out.println("(0) for exit");
            int choice = scanner.nextInt();
            if (choice == 0) break;
            else if (choice >= 1 && choice <= songsList.size()) {
                songsList.get(choice - 1).showToUser(user);
                break;
            }
            else
                System.out.println("invalid choice");
        }
        scanner.close();
    }
    public void showToArtist(Artist artist){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Title: " + getTitle());
        System.out.println("Artist: " + getArtist());
        System.out.println("Publish Date: " + getPublishDate().toString());
        System.out.println(songs.size() + " Songs");
        List<Song> songsList = songsInList();
        int counter = 1;
        for (Song song : songsList) {
            System.out.println(counter + ". " + song.getTitle());
        }
        while(true) {
            System.out.println("which one are you looking for?");
            System.out.println("(0) for exit");
            int choice = scanner.nextInt();
            if (choice == 0) break;
            else if (choice >= 1 && choice <= songsList.size()) {
                songsList.get(choice - 1).showToArtist(artist);
                break;
            }
            else
                System.out.println("invalid choice");
        }
        scanner.close();
    }
    public void showToAdmin(Admin admin){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Title: " + getTitle());
        System.out.println("Artist: " + getArtist());
        System.out.println("Publish Date: " + getPublishDate().toString());
        System.out.println(songs.size() + " Songs");
        List<Song> songsList = songsInList();
        int counter = 1;
        for (Song song : songsList) {
            System.out.println(counter + ". " + song.getTitle());
        }
        while(true) {
            System.out.println("which one are you looking for?");
            System.out.println("(0) for exit");
            int choice = scanner.nextInt();
            if (choice == 0) break;
            else if (choice >= 1 && choice <= songsList.size()) {
                songsList.get(choice - 1).showToAdmin(admin);
                break;
            }
            else
                System.out.println("invalid choice");
        }
        scanner.close();
    }

    private List<Song> songsInList(){
        List<Song> songs = new ArrayList<>();
        for (String song : this.songs) {
            musicRepo.findById(song).ifPresent(songs::add);
        }
        return songs;
    }
}
