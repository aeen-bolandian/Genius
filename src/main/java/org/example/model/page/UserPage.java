package org.example.model.page;

import org.example.model.account.Account;
import org.example.model.account.Artist;
import org.example.model.account.User;
import org.example.model.content.Album;
import org.example.model.content.Comment;
import org.example.model.content.Song;
import org.example.model.notification.Notification;
import org.example.model.request.LyricEditRequest;
import org.example.repository.InMemoryRepository;
import org.example.services.AuthService;

import java.util.*;

public class UserPage {

    Scanner scanner = new Scanner(System.in);
    private AuthService authService;

    public void setAuthService(AuthService authService) { this.authService = authService; }

    InMemoryRepository<Account, String> userRepo;
    InMemoryRepository<Album, String> albumRepo;
    InMemoryRepository<Song, String> musicRepo;
    InMemoryRepository<Notification, String> notificationRepo;
    InMemoryRepository<LyricEditRequest, String> requestRepo;
    InMemoryRepository<Comment, String> commentRepo;

    public UserPage() {}

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

    public void userPage(User user) {
        while(true) {
            System.out.println("hello " + user.getUsername());
            System.out.println("last login on : " + new Date());
            System.out.println("-------------------------------");
            showNewestSongs(user);
            System.out.println("-------------------------------");
            showMostPopularSongs(user);
            System.out.println("-------------------------------");
            System.out.println("1.notifications\n2.search artists\n3.search music\n4.favorite songs\n5.logout");
            System.out.println("-------------------------------");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1: {
                    notificationPage(user);
                    break;
                }
                case 2: {
                    searchArtist(user);
                    break;
                }
                case 3: {
                    searchMusic(user);
                    break;
                }
                case 4: {
                    favoriteSong(user);
                    break;
                }
                case 5: {
                    authService.logout(user);
                    return;
                }
            }
        }
    }
    public void showNewestSongs(User user) {
        List<Song> songs = musicRepo.findAll().stream()
                .filter(song -> user.getFollowedArtists().contains(song.getArtist()))
                .sorted((s1, s2) -> s2.getPublishDate().compareTo(s1.getPublishDate()))
                .toList();

        if (songs.isEmpty()) {
            System.out.println("No new songs from followed artists.");
            return;
        }

        System.out.println("Newest songs for you:");
        for (int i = 0; i < Math.min(5, songs.size()); i++) {
            System.out.println((i + 1) + ". " + songs.get(i).getTitle());
        }
    }


    private void showMostPopularSongs(User user) {
        sortMusicByLikes(musicRepo.findAll());
        System.out.println("most popular songs for you : ");
        for(int i = 0; i < Math.min(musicRepo.findAll().size() , 5); i++) {
            System.out.println((i + 1) + ". " + musicRepo.findAll().get(i).getTitle());
        }
    }

    private void sortMusicsByDate(List<Song> songs) {
        if(!songs.isEmpty()) {
            songs.sort(Comparator.comparing(Song::getPublishDate).reversed());
        }
    }

    private void sortMusicByLikes(List<Song> songs) {
        if(!songs.isEmpty()) {
            songs.sort((song1, song2) -> Long.compare(song2.getLikes(), song1.getLikes()));
        }
    }

    private void notificationPage(User user) {
        user.viewNotifications();
    }

    private void searchArtist(User user) {

        List<Account> artists = userRepo.findAll().stream().filter(account -> account.getAccountType().equals("ARTIST")).toList();

        System.out.println("which artist you are looking for ?");
        String userInput = scanner.nextLine();
        List<Account> searchResults = artists.stream().filter(account -> account.getUsername().toLowerCase().contains(userInput.toLowerCase())).toList();
        int counter = 1;
        for(Account account : searchResults) {
            System.out.println(counter + ". " + account.getUsername());
            counter++;
        }
        // select artist
        System.out.println("which one do you want : ");
        System.out.println("(0 for return to main)");
        int choice = scanner.nextInt();
        if (choice == 0) {userPage(user);}
        else if (choice >= 1 && choice <= searchResults.size()) {
            System.out.println(searchResults.get(choice-1).toString());
            if(user.getFollowedArtists().contains(searchResults.get(choice-1).getId())) {
                System.out.println("you are following this artist");
                System.out.println("do you want to unfollow this artist?");
                System.out.println("1.yes\n2.no");
                int choice2 = scanner.nextInt();
                if(choice2 == 1) {
                    user.unfollowArtist((Artist) searchResults.get(choice - 1));
                }
                System.out.println("press any key to return");
                scanner.nextLine();
                searchArtist(user);
            }
            System.out.println("would you like to follow this artist?");
            System.out.println("1.yes 2.no");
            int choice3 = scanner.nextInt();
            if(choice3 == 1) { user.followArtist((Artist) searchResults.get(choice - 1)); }
            System.out.println("Press any key to return");
            scanner.nextLine();
            searchArtist(user);
        }
        else {
            System.out.println("Invalid choice");
            searchArtist(user);
        }

    }

    private void searchMusic(User user) {

        List<Song> musics = musicRepo.findAll();

        System.out.println("which Song you are looking for ?");
        String userInput = scanner.nextLine();
        List<Song> searchResults = musics.stream().filter(song -> song.getTitle().toLowerCase().contains(userInput)).toList();
        int counter = 1;
        for(Song song : searchResults) {
            System.out.println(counter + ". " + song.getTitle());
            counter++;
        }
        System.out.println("which one do you want : ");
        System.out.println("(0 for return to main)");
        int choice = scanner.nextInt();
        if (choice == 0) {userPage(user);}
        else if (choice >= 1 && choice <= searchResults.size()) {
            searchResults.get(choice - 1).showToUser(user);
        }
        else {
            System.out.println("Invalid choice");
            searchMusic(user);
        }
    }

    private void favoriteSong(User user) {
        if(user.getFavoriteSongs().isEmpty()) {
            System.out.println("you don't have any favorite songs");
        }
        else {
            int count = 1;
            List<Song> favoriteSongs = new ArrayList<>();
            for(String song : user.getFavoriteSongs()) {
                favoriteSongs.add(musicRepo.findById(song).get());
            }
            for (Song song : favoriteSongs) {
                System.out.println(count + ". " + song.getTitle());
                count++;
            }
            System.out.println("which one do you want : ");
            System.out.println("(0 for return to main)");
            int choice = scanner.nextInt();
            if (choice == 0) {userPage(user);}
            else if (choice >= 1 && choice <= user.getFavoriteSongs().size()) {
                musicRepo.findById(user.getFavoriteSongs().get(choice - 1)).get().showToUser(user);
            }
            else{
                System.out.println("Invalid choice");
                favoriteSong(user);
            }
        }
    }
}
