package org.example.model.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.model.content.Album;
import org.example.model.content.Comment;
import org.example.model.content.Song;
import org.example.model.notification.Notification;
import org.example.model.request.LyricEditRequest;
import org.example.repository.InMemoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "accountType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Admin.class, name = "ADMIN"),
        @JsonSubTypes.Type(value = Artist.class, name = "ARTIST"),
        @JsonSubTypes.Type(value = User.class, name = "USER")
})

public class Account {
    @JsonIgnore
    public String[] accountTypes = {"ADMIN", "ARTIST", "USER"};
    @JsonProperty("accountType")
    private String accountType;

    private String id = UUID.randomUUID().toString();
    private String name;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private boolean active;
    private List<String> notifications = new ArrayList<String>();

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

    public Account() {
        id = UUID.randomUUID().toString();
    }

    public Account(String name, String lastName, String username, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.active = false;
        this.username = username;
    }

    // getters :

    public String getUsername() {
        return username;
    }

    public String getAccountType() {
        return accountType;
    }

    public boolean isActive() {
        return active;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public String getPassword() {
        return password;
    }

    // setters :

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void viewNotifications() {
        Scanner scanner = new Scanner(System.in);
        List<Notification> notifications = notificationsInList();
        int counter = 1;
        for (Notification notification : notifications) {
            if (notification.isRead())
                System.out.println(counter + ". from : " + userRepo.findById(notification.getAuthor()).get().getUsername() + " ✔️✔️");
            else
                System.out.println(counter + ". from : " + userRepo.findById(notification.getAuthor()).get().getUsername());
            counter++;
        }
        System.out.println("which one are you looking for?");
        int choice = scanner.nextInt();
        if (choice >= 1 && choice <= notifications.size()) {
            notifications.get(choice - 1).read();
        }
    }

    public void addnotification(Notification notification) {
        notifications.add(notification.getId());
        userRepo.save(this);
    }

    public List<Notification> notificationsInList() {
        List<Notification> notifications = new ArrayList<>();
        for (String notification : this.notifications) {
            notificationRepo.findById(notification).ifPresent(notifications::add);
        }
        return notifications;
    }
}

