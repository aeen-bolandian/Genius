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

import java.util.*;

public class ArtistPage {

    private final Admin admin;

    Scanner scanner = new Scanner(System.in);

    InMemoryRepository<Album, String> albumRepo;
    InMemoryRepository<Song, String> musicRepo;

    public void setAlbumRepo(InMemoryRepository<Album, String> albumRepo) {
        this.albumRepo = albumRepo;
    }

    public void setMusicRepo(InMemoryRepository<Song, String> musicRepo) {
        this.musicRepo = musicRepo;
    }



    private AuthService authService;

    public ArtistPage(Admin admin) { this.admin = admin; }

    public void setAuthService(AuthService authService) { this.authService = authService; }

    public void artistPage(Artist artist) {

        System.out.println("hello " + artist.getName());
        System.out.println("last login on : " + new Date());
        System.out.println("-------------------------------");
        while (true) {
            System.out.println("1.Notifications\n2.Songs\n3.Albums\n4.Profile\n6.Create new song\n7.Create new album\n8.Remove song\n9.Remove album\n10.Log out");
            System.out.println("-------------------------------");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1: {
                    notificationPage(artist);
                    break;
                }
                case 2: {
                    songPage(artist);
                    break;
                }
                case 3: {
                    albumPage(artist);
                    break;
                }
                case 4: {
                    profilePage(artist);
                    break;
                }
                case 6: {
                    createNewSong(artist);
                    break;
                }
                case 7: {
                    createNewAlbum(artist);
                    break;
                }
                case 8: {
                    removeSong(artist);
                    break;
                }
                case 9: {
                    removeAlbum(artist);
                    break;
                }
                case 10: {
                    authService.logout(artist);
                    return;
                }
            }
        }
    }
    private void notificationPage(Artist artist) {
        artist.viewNotifications();
    }

    private void songPage(Artist artist) {
        System.out.println("how would you like to sort songs?");
        System.out.println("1.most viewed\n2.most popular\n3.most hated\n4.newest songs");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
            {
                artist.showMostViewedSongs();
            }
            case 2:
            {
                artist.showMostLikedSongs();
            }
            case 3:
            {
                artist.showMostHatedSongs();
            }
            case 4:
            {
                artist.showNewestSongs();
            }
        }
        System.out.println("which song you are looking for?");
        System.out.println("(0 for return to main)");

        choice = scanner.nextInt();
        if (choice == 0) {artistPage(artist);}
        else if (choice >= 1 && choice <= artist.getSongs().size()) {
            System.out.println("\033[H\033[2J");
            musicRepo.findById(artist.getSongs().get(choice - 1)).get().showToArtist(artist);
            songPage(artist);
        }
        else {
            System.out.println("Invalid choice");
            songPage(artist);
        }
    }

    private void albumPage(Artist artist) {
        artist.showAlbums();
        System.out.println("which album you are looking for?");
        System.out.println("(0 for return to main)");
        int choice = scanner.nextInt();
        if(choice == 0) {artistPage(artist);}
        else if (choice >= 1 && choice <= artist.getAlbums().size()) {
            albumRepo.findById(artist.getAlbums().get(choice - 1)).get().showToArtist(artist);
        }
        else {
            System.out.println("Invalid choice");
            artistPage(artist);
        }
    }

    private void profilePage(Artist artist) {
        System.out.println("\033[H\033[2J");
        artist.showProfile();
        System.out.println("press any key to return");
        char ch = scanner.next().charAt(0);
        artistPage(artist);
    }

    private void createNewSong(Artist artist) {
        System.out.println("title : ");
        String title = scanner.nextLine();
        System.out.println("album : ");
        String albumTitle = scanner.nextLine();
        List<Album> albums = new ArrayList<>();
        for (String album : artist.getAlbums()) {
            albums.add(albumRepo.findById(album).get());
        }
        Album album = albums.stream().filter(a -> a.getTitle().equals(albumTitle)).findFirst().orElse(null);
        List<String> coArtists = new ArrayList<>();

        String coArtistname = "";
        while(true) {
            System.out.println("coArtists : ");
            coArtistname = scanner.nextLine();
            if (coArtistname.equals("end")) {
                break;
            }
            coArtists.add(coArtistname);
        }
        System.out.println("Enter lyrics (type 'END' on empty line to finish):");
        StringBuilder lyrics = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("END") || line.isEmpty()) {
                break;
            }
            lyrics.append(line).append("\n");
        }
        Song song;
        if(album == null) { song = new Song(title , artist.getId() , coArtists , null , lyrics.toString());}
        else
            song = new Song(title , artist.getId() , coArtists , album.getId() , lyrics.toString());
        artist.publishSong(song);
    }

    private void createNewAlbum(Artist artist) {
        System.out.println("\033[H\033[2J");
        System.out.println("you are going to send a new album");
        System.out.println("title : ");
        String title = scanner.nextLine();
        System.out.println("songs : ");
        List<String> songs = new ArrayList<>();
        while(true) {
            System.out.println("title : ");
            String songTitle = scanner.nextLine();
            if(songTitle.equals("end")) { break; }
            List<Song> songList = new ArrayList<>();
            for(String song : artist.getSongs()) {
                songList.add(musicRepo.findById(song).get());
            }
            Song song = songList.stream().filter(s->s.getTitle().equals(songTitle)).findFirst().orElse(null);
            if(song != null && song.getAlbum() == null) {
                songs.add(song.getId());
            }
            else if(song != null && song.getAlbum() != null) {
                System.out.println("already has an album");
            }
            else if(song == null) {
                List<String> coArtists = new ArrayList<>();
                System.out.println("coArtists : ");
                String coArtistname = "";
                while(true) {
                    coArtistname = scanner.nextLine();
                    if (coArtistname.equals("end")) {
                        break;
                    }
                    coArtists.add(coArtistname);
                }
                System.out.println("Enter lyrics (type 'END' on empty line to finish):");
                StringBuilder lyrics = new StringBuilder();
                while (true) {
                    String line = scanner.nextLine();
                    if (line.equalsIgnoreCase("END") || line.isEmpty()) {
                        break;
                    }
                    lyrics.append(line).append("\n");
                }
                song = new Song(title , artist.getId() , coArtists , null , lyrics.toString());
                songs.add(song.getId());
            }
            Album album = new Album(title , artist.getId() , songs);
            artist.publishAlbum(album);
        }
    }

    private void removeSong(Artist artist) {
        System.out.println("\033[H\033[2J");
        System.out.println("Which song do you want to remove?");
        System.out.println("title : ");
        String title = scanner.nextLine();
        List<Song> songList = new ArrayList<>();
        for(String song : artist.getSongs()) {
            songList.add(musicRepo.findById(song).get());
        }
        Song song = songList.stream().filter(s->s.getTitle().equals(title)).findFirst().orElse(null);
        if(song != null) {
            artist.unpublishSong(song);

            musicRepo.delete(song.getId());
        }
        else {
            System.out.println("not found");
        }
    }

    private void removeAlbum(Artist artist) {
        System.out.println("\033[H\033[2J");
        System.out.println("Which album do you want to remove?");
        System.out.println("title : ");
        String title = scanner.nextLine();
        List<Album> albumList = new ArrayList<>();
        for(String album : artist.getAlbums()) {
            albumList.add(albumRepo.findById(album).get());
        }
        Album album = albumList.stream().filter(a -> a.getTitle().equals(title)).findFirst().orElse(null);
        if(album != null) {
            artist.unpublishAlbum(album);
            for(String song : album.getSongs()) {
                artist.unpublishSong(musicRepo.findById(song).get());
            }
        }
        else {
            System.out.println("not found");
        }
    }
}
