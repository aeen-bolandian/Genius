package org.example.model.page;

import org.example.model.account.Account;
import org.example.model.account.Admin;
import org.example.model.account.Artist;
import org.example.model.content.Album;
import org.example.model.content.Comment;
import org.example.model.content.Song;
import org.example.model.notification.Notification;
import org.example.model.request.LyricEditRequest;
import org.example.repository.InMemoryRepository;
import org.example.services.AuthService;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class AdminPage {

    Scanner scanner = new Scanner(System.in);

    InMemoryRepository<Account, String> userRepo;
    InMemoryRepository<Album, String> albumRepo;
    InMemoryRepository<Song, String> musicRepo;
    InMemoryRepository<Notification, String> notificationRepo;
    InMemoryRepository<LyricEditRequest, String> requestRepo;
    InMemoryRepository<Comment, String> commentRepo;



    public void setUserRepo(InMemoryRepository<Account, String> userRepo) {
        this.userRepo = userRepo;
    }

    public void setMusicRepo(InMemoryRepository<Song, String> musicRepo) {
        this.musicRepo = musicRepo;
    }

    public void setAlbumRepo(InMemoryRepository<Album, String> albumRepo) {
        this.albumRepo = albumRepo;
    }

    public void setCommentRepo(InMemoryRepository<Comment, String> commentRepo) {
        this.commentRepo = commentRepo;
    }

    public void setNotificationRepo(InMemoryRepository<Notification, String> notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    public void setRequestRepo(InMemoryRepository<LyricEditRequest, String> requestRepo) {
        this.requestRepo = requestRepo;
    }

    private AuthService authService;

    public void setAuthService(AuthService authService) { this.authService = authService; }

    public AdminPage() {}


    public void adminPage(Admin admin)  {
        while(true) {
            System.out.println("hello " + admin.getUsername());
            System.out.println("-------------------------------");
            System.out.println("1.notifications\n2.search songs\n3.search artists\n4.search user\n5.logout");
            System.out.println("-------------------------------");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1: {
                    notificationPage(admin);
                    break;
                }
                case 2: {
                    searchSongs(admin);
                    break;
                }
                case 3: {
                    searchArtists(admin);
                    break;
                }
                case 4: {
                    searchUsers(admin);
                    break;
                }
                case 5: {
                    authService.logout(admin);
                    return;
                }
            }
        }
    }

    private void notificationPage(Admin admin)  {
        admin.viewNotifications();
    }


    private void searchSongs(Admin admin)  {
        System.out.println("\033[H\033[2J");
        System.out.println("type song name : ");
        String searchSongName = scanner.nextLine();
        List<Song> searchResults = musicRepo.findAll().stream().filter(song -> song.getTitle().toLowerCase().contains(searchSongName.toLowerCase())).toList();
        int counter = 1;
        for (Song song : searchResults) {
            System.out.println(counter + ". " + song.getTitle());
            counter++;
        }
        System.out.println("which song you are looking for ?");
        System.out.println("(0 for return to main)");
        int choice = scanner.nextInt();
        if (choice == 0) {adminPage(admin);}
        else if (choice >= 1 && choice <= searchResults.size()) {
            searchResults.get(choice - 1).showToAdmin(admin);
            searchSongs(admin);
        }
        else {
            System.out.println("Invalid choice");
            searchSongs(admin);
        }
    }

    private void searchArtists(Admin admin)  {
        System.out.println("type artist name : ");
        String searchArtistName = scanner.nextLine();
        List<Account> searchResults = userRepo.findAll().stream().filter(artist -> artist.getUsername().toLowerCase().contains(searchArtistName.toLowerCase()) && artist.getAccountType().equals("ARTIST")).toList();
        List<Artist> convertedToArtist = Collections.singletonList((Artist) searchResults);
        int counter = 1;
        for (Account account : searchResults) {
            System.out.println(counter + ". " + account.getUsername());
            counter++;
        }
        System.out.println("which artist you are looking for ?");
        System.out.println("(0 for return to main)");
        int choice = scanner.nextInt();
        if (choice == 0) {adminPage(admin);}
        else if (choice >= 1 && choice <= searchResults.size()) {
            convertedToArtist.get(choice - 1).showProfile();
        }
        else {
            System.out.println("Invalid choice");
            searchArtists(admin);
        }
    }

    private void searchUsers(Admin admin)  {
        System.out.println("type artist name : ");
        String searchUserName = scanner.nextLine();
        List<Account> searchResults = userRepo.findAll().stream().filter(user -> user.getUsername().toLowerCase().contains(searchUserName.toLowerCase()) && user.getAccountType().equals("USER")).toList();
        List<Artist> convertedToUser = Collections.singletonList((Artist) searchResults);
        int counter = 1;
        for (Account account : searchResults) {
            System.out.println(counter + ". " + account.getUsername());
            counter++;
        }
        System.out.println("which artist you are looking for ?");
        System.out.println("(0 for return to main)");
        int choice = scanner.nextInt();
        if (choice == 0) {adminPage(admin);}
        else if (choice >= 1 && choice <= searchResults.size()) {
            convertedToUser.get(choice - 1).showProfile();
        }
        else {
            System.out.println("Invalid choice");
            searchUsers(admin);
        }
    }
}
