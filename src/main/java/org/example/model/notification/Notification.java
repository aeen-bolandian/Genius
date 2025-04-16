package org.example.model.notification;

import org.example.model.account.Account;
import org.example.model.content.Album;
import org.example.model.content.Comment;
import org.example.model.content.Song;
import org.example.model.request.LyricEditRequest;
import org.example.repository.InMemoryRepository;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class Notification {

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
    private String title;
    private String body;
    private String author;
    private String recipient;
    private Date sendDate;
    private boolean read;

    public Notification(){ id = UUID.randomUUID().toString(); }
    public Notification(String author , String recipient , String title , String body) {
        this.id = UUID.randomUUID().toString();
        this.author = author;
        this.recipient = recipient;
        this.title = title;
        this.body = body;
        this.read = false;
        sendDate = new Date();
    }

    // getters

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public String getId() {
        return id;
    }

    public boolean isRead() {
        return read;
    }

    public String getRecipient() {
        return recipient;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public String getTitle() {
        return title;
    }

    // setters

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // read notification
    public void read(){
        System.out.println("author : " + userRepo.findById(author).get().getUsername());
        System.out.println("recipient : " + userRepo.findById(recipient).get().getUsername());
        System.out.println("title : " + title);
        System.out.println("body : " + body);
        setRead(true);
    }
}
