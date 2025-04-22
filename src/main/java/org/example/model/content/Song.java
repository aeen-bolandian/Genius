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

public class Song {

    InMemoryRepository<Account, String> userRepo;
    InMemoryRepository<Album, String> albumRepo;
    InMemoryRepository<Song, String> musicRepo;
    InMemoryRepository<Notification, String> notificationRepo;
    InMemoryRepository<LyricEditRequest, String> requestRepo;
    InMemoryRepository<Comment, String> commentRepo;

    public void setCommentRepo(InMemoryRepository<Comment, String> commentRepo) {
        this.commentRepo = commentRepo;
    }

    public void setUserRepo(InMemoryRepository<Account, String> userRepo) {
        this.userRepo = userRepo;
    }

    public void setRequestRepo(InMemoryRepository<LyricEditRequest, String> requestRepo) {
        this.requestRepo = requestRepo;
    }

    public void setAlbumRepo(InMemoryRepository<Album, String> albumRepo) {
        this.albumRepo = albumRepo;
    }

    public void setMusicRepo(InMemoryRepository<Song, String> musicRepo) {
        this.musicRepo = musicRepo;
    }

    public void setNotificationRepo(InMemoryRepository<Notification, String> notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    private String id;
    private String title;
    private String artist;
    private String album;
    private long likes;
    private long dislikes;
    private long views;
    private String lyrics;
    private Date publishDate;

    private List<String> coArtists = new ArrayList<>();
    private List<String> comments = new ArrayList<>();
    private List<String> requests = new ArrayList<>();

    public Song() { }
    public Song(String title , String artist , List<String> coArtists , String album , String lyrics) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.lyrics = lyrics;
        this.coArtists = coArtists;
        id = UUID.randomUUID().toString();
        publishDate = new Date();
    }

    // getters

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public List<String> getCoArtists() {
        return coArtists;
    }

    public List<String> getComments() {
        return comments;
    }

    public long getDislikes() {
        return dislikes;
    }

    public String getId() {
        return id;
    }

    public long getLikes() {
        return likes;
    }

    public String getLyrics() {
        return lyrics;
    }

    public List<String> getRequests() {
        return requests;
    }

    public String getTitle() {
        return title;
    }

    public long getViews() {
        return views;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    // setters

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setCoArtists(List<String> coArtists) {
        this.coArtists = coArtists;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public void setRequests(List<String> requests) {
        this.requests = requests;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    // view requests
    public void viewRequests() {
        List<LyricEditRequest> requests = requestsInList();
        int counter = 1;
        for(LyricEditRequest request : requests) {
            System.out.println(counter + ". from" + userRepo.findById(request.getSender()).get().getUsername());
            counter++;
        }
    }

    // view comments
    public void viewComments() {
        List<Comment> comments = commentsInList();
        int counter = 1;
        for(Comment comment : comments) {
            comment.read();
        }
    }

    // show to each type of account
    public void showToUser(User user) {
        views += 1;
        Scanner scanner = new Scanner(System.in);
        System.out.println("artist : " + userRepo.findById(artist).get().getUsername());
        System.out.println("album : " + albumRepo.findById(album).get().getTitle());
        System.out.println("coArtist : ");
        for(String artist : coArtists) {
            System.out.println(artist);
        }
        System.out.println(likes + " likes");
        System.out.println(dislikes + " dislikes");
        System.out.println("lyrics : ");
        System.out.println("-------------------");
        System.out.println(lyrics);
        System.out.println("-------------------");
        while(true) {
            System.out.println("1.like 2.dislike 3.comment 4.add to favorites 5.lyric edit request");
            int choice1 = scanner.nextInt();
            if(choice1 == 1) { user.likeSong(this);}
            else if(choice1 == 2) { user.dislikeSong(this);}
            else if(choice1 == 3) {
                System.out.println("comments : ");
                viewComments();
                System.out.println("would you like to add comments? 1.yes or 2.no");
                int choice = scanner.nextInt();
                if (choice == 1) {
                    user.addComment(this);
                } else if (choice == 2) {
                    showToUser(user);
                }
            }
            else if(choice1 == 4) {
                user.sendRequest(this);
            }
        }
    }
    public void showToArtist(Artist artist) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("artist : " + userRepo.findById(this.artist).get().getUsername());
        System.out.println("album : " + albumRepo.findById(album).get().getTitle());
        System.out.println("id : " + getId());
        System.out.println("coArtist : ");
        for(String artists : coArtists) {
            System.out.println(artists);
        }
        System.out.println(likes + " likes");
        System.out.println(dislikes + " dislikes");
        System.out.println("lyrics : ");
        System.out.println("-------------------");
        System.out.println(lyrics);
        System.out.println("-------------------");
        System.out.println("comments : ");
        viewComments();
        System.out.println(requests.size() + " requests");
        viewRequests();
        System.out.println("which one are you looking for?");
        int choice = scanner.nextInt();
        if(choice >= 1 && choice <= requests.size()) {
            requestRepo.findById(requests.get(choice - 1)).get().read();
            System.out.println("1.approve 2.reject 3.ignore");
            int rejectChoice = scanner.nextInt();
            if(rejectChoice == 1) {
                artist.approveRequest(requestRepo.findById(requests.get(choice - 1)).get());
            }
            else if(rejectChoice == 2) {
                artist.approveRequest(requestRepo.findById(requests.get(choice - 1)).get());
            }
            else
                System.out.println("That is not a valid choice");
        }
        else
            System.out.println("that is not a valid choice");
    }
    public void showToAdmin(Admin admin) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("artist : " + userRepo.findById(artist).get().getUsername());
        System.out.println("album : " + albumRepo.findById(album).get().getTitle());
        System.out.println("id : " + getId());
        System.out.println("coArtist : ");
        for(String artists : coArtists) {
            System.out.println(artists);
        }
        System.out.println(likes + " likes");
        System.out.println(dislikes + " dislikes");
        System.out.println("lyrics : ");
        System.out.println("-------------------");
        System.out.println(lyrics);
        System.out.println("-------------------");
        System.out.println("comments : ");
        viewComments();
        System.out.println(requests.size() + " requests");
        viewRequests();
        System.out.println("which one are you looking for?");
        int choice = scanner.nextInt();
        if(choice >= 1 && choice <= requests.size()) {
            requestRepo.findById(requests.get(choice - 1)).get().read();
            System.out.println("1.approve 2.reject");
            int rejectChoice = scanner.nextInt();
            if(rejectChoice == 1) {
                admin.approveRequest(requestRepo.findById(requests.get(choice - 1)).get());
            }
            else if(rejectChoice == 2) {
                admin.rejectRequest(requestRepo.findById(requests.get(choice - 1)).get());
            }
            else
                System.out.println("That is not a valid choice");
        }
        else
            System.out.println("That is not a valid choice");
    }

    private List<LyricEditRequest> requestsInList() {
        List<LyricEditRequest> requests = new ArrayList<>();
        for (String request : this.requests) {
            requestRepo.findById(request).ifPresent(requests::add);
        }
        return requests;
    }

    private List<Comment> commentsInList() {
        List<Comment> comments = new ArrayList<>();
        for (String comment : this.comments) {
            commentRepo.findById(comment).ifPresent(comments::add);
        }
        return comments;
    }

    private List<Artist> coArtistsInList() {
        List<Artist> artistlist = new ArrayList<>();
        for (String artist : this.coArtists) {
            userRepo.findById(artist).ifPresent(artistObj -> artistlist.add((Artist) artistObj));
        }
        return artistlist;
    }
}
