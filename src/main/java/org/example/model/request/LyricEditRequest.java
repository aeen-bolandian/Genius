package org.example.model.request;

import org.example.model.account.Account;
import org.example.model.content.Album;
import org.example.model.content.Comment;
import org.example.model.content.Song;
import org.example.model.notification.Notification;
import org.example.repository.InMemoryRepository;

import java.time.LocalDateTime;
import java.util.Date;

public class LyricEditRequest {

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

    public enum Status { APPROVED , REJECTED , PENDING }
    private Status status;

    private String id;
    private String sender;
    private String recipient;
    private String song;
    private String lyric;
    private Date sendDate;
    private boolean read;

    public LyricEditRequest() {}
    public LyricEditRequest(String sender , String recipient , String song , String lyric ) {
        this.sender = sender;
        this.recipient = recipient;
        this.song = song;
        this.lyric = lyric;
        this.status = Status.PENDING;
        this.read = false;
        sendDate = new Date();
    }

    // getters

    public String getId() {
        return id;
    }

    public String getLyric() {
        return lyric;
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

    public String getSender() {
        return sender;
    }

    public String getSong() {
        return song;
    }

    public Status getStatus() {
        return status;
    }

    // setters

    public void setId(String id) {
        this.id = id;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
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

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // read request
    public void read() {
        System.out.println("from " + userRepo.findById(sender).get().getUsername()
        + " to " + userRepo.findById(recipient).get().getUsername());
        System.out.println("suggested lyric : ");
        System.out.println(lyric);
    }
}
