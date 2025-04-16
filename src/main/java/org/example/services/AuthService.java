package org.example.services;

import org.example.model.account.Account;
import org.example.model.account.Artist;
import org.example.model.account.User;
import org.example.model.content.Album;
import org.example.model.content.Comment;
import org.example.model.content.Song;
import org.example.model.notification.Notification;
import org.example.model.request.LyricEditRequest;
import org.example.repository.InMemoryRepository;

import java.util.Scanner;

public class AuthService {

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

    private String username;
    private String password;

    public AuthService(InMemoryRepository<Account , String> userRepo) { this.userRepo = userRepo; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void signUp() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("name: ");
        String name = scanner.nextLine();
        System.out.println("lastName: ");
        String lastName = scanner.nextLine();
        System.out.println("email: ");
        String email = scanner.nextLine();
        System.out.println("username: ");
        String username = scanner.nextLine();
        System.out.println("password: ");
        String password = scanner.nextLine();

        System.out.println("choose your role:");
        System.out.println("1. Artist\n2. User");
        int roleNum = 0;
        String type;

        while (true) {
            System.out.println("choose between 1 and 2");
            roleNum = scanner.nextInt();
            if (roleNum == 1 || roleNum == 2) break;
        }

        type = (roleNum == 1) ? "ARTIST" : "USER";

        validateEmail(email);
        validateUsername(username);
        validatePassword(password);

        Account account;
        if (type.equals("ARTIST")) {
            account = new Artist(name, lastName, username, email, password);
        } else {
            account = new User(name, lastName, username, email, password);
        }

        userRepo.save(account); // This will save to JSON
        System.out.println("Account created successfully");
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("username: ");
        String username = scanner.nextLine().trim();
        System.out.println("password: ");
        String password = scanner.nextLine().trim();

        Account account = userRepo.findByUserName(username).get();
        if (userRepo.findByUserName(username).isEmpty()) {
            System.out.println("User not found");
        }

        if (password.equals(account.getPassword())) {
            this.username = username;
            this.password = password; // Note: Storing plaintext - consider security implications
            account.setActive(true);
            userRepo.save(account);
            System.out.println("Logged in successfully");
        } else {
            System.out.println("Wrong password");
        }
    }

    public void logout(Account account) {
        account.setActive(false);
        System.out.println("logged out successfully");
        userRepo.save(account);
    }


    // validation methods :
    private void validateUsername(String username) {
        if (username == null || username.length() < 4 || username.length() > 20) {
            throw new IllegalArgumentException("Username must be 4-20 characters");
        }
        if (userRepo.findByUserName(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }
}
