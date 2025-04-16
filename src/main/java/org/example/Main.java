package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.account.Account;
import org.example.model.account.Admin;
import org.example.model.account.Artist;
import org.example.model.account.User;
import org.example.model.content.Album;
import org.example.model.content.Comment;
import org.example.model.content.Song;
import org.example.model.notification.Notification;
import org.example.model.page.AdminPage;
import org.example.model.page.ArtistPage;
import org.example.model.page.UserPage;
import org.example.model.request.LyricEditRequest;
import org.example.repository.InMemoryRepository;
import org.example.services.AuthService;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ObjectMapper mapper = new ObjectMapper();


        InMemoryRepository<Account, String> userRepo = new InMemoryRepository<>(Account.class , "accounts.json" , mapper);
        InMemoryRepository<Album, String> albumRepo = new InMemoryRepository<>(Album.class , "albums.json" , mapper);
        InMemoryRepository<Song, String> musicRepo = new InMemoryRepository<>(Song.class , "musics.json" , mapper);
        InMemoryRepository<Notification, String> notificationRepo = new InMemoryRepository<>(Notification.class , "notifications.json" , mapper);
        InMemoryRepository<LyricEditRequest, String> requestRepo = new InMemoryRepository<>(LyricEditRequest.class , "requests.json" , mapper);
        InMemoryRepository<Comment, String> commentRepo = new InMemoryRepository<>(Comment.class , "comments.json" , mapper);

        AuthService authService = new AuthService(userRepo);

        userRepo.loadAll();
        albumRepo.loadAll();
        musicRepo.loadAll();
        notificationRepo.loadAll();
        requestRepo.loadAll();
        commentRepo.loadAll();

        org.example.Inject.injectAllRepositories(userRepo, albumRepo, musicRepo, notificationRepo, requestRepo, commentRepo);

        authService.setAlbumRepo(albumRepo);
        authService.setMusicRepo(musicRepo);
        authService.setNotificationRepo(notificationRepo);
        authService.setRequestRepo(requestRepo);
        authService.setCommentRepo(commentRepo);


        if (userRepo.findAll().isEmpty()) {
            System.out.println("Seeding initial data...");
            SeedData.seedAll(userRepo, albumRepo, musicRepo, notificationRepo, requestRepo, commentRepo);

        }

        org.example.Inject.injectAllRepositories(userRepo, albumRepo, musicRepo, notificationRepo, requestRepo, commentRepo);

        Admin admin = (Admin) userRepo.findByUserName("admin").get();
        admin.setAlbumRepo(albumRepo);
        admin.setMusicRepo(musicRepo);
        admin.setNotificationRepo(notificationRepo);
        admin.setRequestRepo(requestRepo);
        admin.setCommentRepo(commentRepo);

        while(true) {
            System.out.println("1.SIGN UP\n2.LOGIN\n3.EXIT");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1: {
                    authService.signUp();
                    break;
                }
                case 2: {
                        authService.login();
                        Account account = userRepo.findByUserName(authService.getUsername()).get();
                        account.setRequestRepo(requestRepo);
                        account.setCommentRepo(commentRepo);
                        account.setAlbumRepo(albumRepo);
                        account.setMusicRepo(musicRepo);
                        account.setNotificationRepo(notificationRepo);
                        account.setUserRepo(userRepo);
                        if (account.getAccountType().equals("ARTIST")) {
                            ArtistPage artistpage = new ArtistPage(admin);
                            artistpage.setAlbumRepo(albumRepo);
                            artistpage.setMusicRepo(musicRepo);
                            artistpage.setAuthService(authService);
                            artistpage.artistPage((Artist) account);
                        }
                        else if (account.getAccountType().equals("USER")) {
                            UserPage userpage = new UserPage();
                            userpage.setCommentRepo(commentRepo);
                            userpage.setNotificationRepo(notificationRepo);
                            userpage.setUserRepo(userRepo);
                            userpage.setAlbumRepo(albumRepo);
                            userpage.setMusicRepo(musicRepo);
                            userpage.setAuthService(authService);
                            userpage.userPage((User) account);
                        }
                        else if (account.getAccountType().equals("ADMIN")) {
                            AdminPage adminpage = new AdminPage();
                            adminpage.setMusicRepo(musicRepo);
                            adminpage.setNotificationRepo(notificationRepo);
                            adminpage.setRequestRepo(requestRepo);
                            adminpage.setUserRepo(userRepo);
                            adminpage.setAlbumRepo(albumRepo);
                            adminpage.setCommentRepo(commentRepo);
                            adminpage.setAuthService(authService);
                            adminpage.adminPage((Admin) account);
                        }
                        else
                            System.out.println("Unknown account type");
                    break;
                }
                case 3: {
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        userRepo.saveAll();
                        albumRepo.saveAll();
                        musicRepo.saveAll();
                        notificationRepo.saveAll();
                        requestRepo.saveAll();
                        commentRepo.saveAll();
                        System.out.println("All data has been saved to JSON files!");
                    }));
                    System.exit(0);
                }
            }
        }
    }
}