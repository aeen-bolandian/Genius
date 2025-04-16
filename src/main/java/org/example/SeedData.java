package org.example;

import org.example.model.account.Account;
import org.example.model.account.Admin;
import org.example.model.account.Artist;
import org.example.model.account.User;
import org.example.model.content.Album;
import org.example.model.content.Comment;
import org.example.model.content.Song;
import org.example.model.notification.Notification;
import org.example.model.request.LyricEditRequest;
import org.example.repository.InMemoryRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SeedData {

    public static void seedAll(
            InMemoryRepository<Account, String> userRepo,
            InMemoryRepository<Album, String> albumRepo,
            InMemoryRepository<Song, String> musicRepo,
            InMemoryRepository<Notification, String> notificationRepo,
            InMemoryRepository<LyricEditRequest, String> requestRepo,
            InMemoryRepository<Comment, String> commentRepo
    ) {
        // Admin
        Admin admin = new Admin("Alice", "Admin", "admin", "admin@example.com", "admin123");
        userRepo.save(admin);

        // Artist
        Artist artist = new Artist("Bob", "Beatmaker", "bobmusic", "bob@example.com", "passbob");
        userRepo.save(artist);

        // User
        User user = new User("Charlie", "Listener", "charlie123", "charlie@example.com", "passcharlie");
        user.getFollowedArtists().add(artist.getId());
        userRepo.save(user);

        // Song
        Song song = new Song("Echoes of Code", artist.getId(), Collections.emptyList(), null,
                "In the land of loops and semicolons,\nCode flows like rivers wide...");
        song.setId("song-1");
        song.setLikes(10);
        song.setDislikes(0);
        song.setViews(25);
        song.setPublishDate(new Date());
        musicRepo.save(song);
        artist.getSongs().add(song.getId());

        // Album
        Album album = new Album("Debugged Melodies", artist.getId(), List.of(song.getId()));
        album.setId("album-1");
        album.setPublishDate(new Date());
        albumRepo.save(album);
        artist.getAlbums().add(album.getId());
        userRepo.save(artist);

        // Comment
        Comment comment = new Comment("This song really hits different!", user.getId(), song.getId());
        comment.setId("comment-1");
        comment.setPublishDate(new Date());
        commentRepo.save(comment);
        song.getComments().add(comment.getId());
        musicRepo.save(song);

        // Notification
        Notification notif = new Notification(admin.getId(), user.getId(), "Welcome", "Welcome to the platform!");
        notif.setId("notif-1");
        notif.setSendDate(new Date());
        notificationRepo.save(notif);
        user.getNotifications().add(notif.getId());
        userRepo.save(user);

        // Lyric Edit Request
        LyricEditRequest request = new LyricEditRequest(user.getId(), artist.getId(), song.getId(),
                "In a world of bugs and fixes,\nEvery line we write persists.\n");
        request.setId("request-1");
        request.setSendDate(new Date());
        requestRepo.save(request);
        song.getRequests().add(request.getId());
        musicRepo.save(song);
        artist.getRequests().add(request.getId());
        admin.getRequests().add(request.getId());
        userRepo.save(artist);
        userRepo.save(admin);
    }
}
