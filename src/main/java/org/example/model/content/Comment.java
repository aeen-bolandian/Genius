package org.example.model.content;

import org.example.model.account.Account;
import org.example.model.notification.Notification;
import org.example.model.request.LyricEditRequest;
import org.example.repository.InMemoryRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class Comment {

    InMemoryRepository<Account, String> userRepo;
    InMemoryRepository<Album, String> albumRepo;
    InMemoryRepository<Song, String> musicRepo;
    InMemoryRepository<Notification, String> notificationRepo;
    InMemoryRepository<LyricEditRequest, String> requestRepo;
    InMemoryRepository<Comment, String> commentRepo;

    public void setUserRepo(InMemoryRepository<Account, String> userRepo) {
        this.userRepo = userRepo;
    }

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

    private String id;
    private String text;
    private String author;
    private String song;
    private Date publishDate;

    public Comment(){ id = UUID.randomUUID().toString(); }
    public Comment(String text, String author, String song) {
        this.text = text;
        this.author = author;
        this.song = song;
        id = UUID.randomUUID().toString();
        publishDate = new Date();
    }

    // getters

    public String getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public String getSong() {
        return song;
    }

    public String getText() {
        return text;
    }

    // setters

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public void setText(String text) {
        this.text = text;
    }

    // show to each type of account
    public void read() {
        System.out.println("-------------------");
        System.out.println("author: " + userRepo.findById(author).get().getUsername());
        System.out.println("text : " + text);
        System.out.println("publishDate : " + publishDate.toString());
        System.out.println("-------------------");
    }
}
