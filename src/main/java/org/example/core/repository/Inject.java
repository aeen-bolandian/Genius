package org.example;

import org.example.model.account.Account;
import org.example.model.content.Album;
import org.example.model.content.Comment;
import org.example.model.content.Song;
import org.example.model.notification.Notification;
import org.example.model.request.LyricEditRequest;
import org.example.repository.InMemoryRepository;

public class Inject {

    public static void injectAllRepositories(
            InMemoryRepository<Account, String> userRepo,
            InMemoryRepository<Album, String> albumRepo,
            InMemoryRepository<Song, String> musicRepo,
            InMemoryRepository<Notification, String> notificationRepo,
            InMemoryRepository<LyricEditRequest, String> requestRepo,
            InMemoryRepository<Comment, String> commentRepo
    ) {
        for (Account account : userRepo.findAll()) {
            account.setUserRepo(userRepo);
            account.setAlbumRepo(albumRepo);
            account.setMusicRepo(musicRepo);
            account.setNotificationRepo(notificationRepo);
            account.setRequestRepo(requestRepo);
            account.setCommentRepo(commentRepo);
        }

        for (Song song : musicRepo.findAll()) {
            song.setUserRepo(userRepo);
            song.setAlbumRepo(albumRepo);
            song.setMusicRepo(musicRepo);
            song.setNotificationRepo(notificationRepo);
            song.setRequestRepo(requestRepo);
            song.setCommentRepo(commentRepo);
        }

        for (Album album : albumRepo.findAll()) {
            album.setUserRepo(userRepo);
            album.setAlbumRepo(albumRepo);
            album.setMusicRepo(musicRepo);
            album.setNotificationRepo(notificationRepo);
            album.setRequestRepo(requestRepo);
            album.setCommentRepo(commentRepo);
        }

        for (Comment comment : commentRepo.findAll()) {
            comment.setUserRepo(userRepo);
            comment.setAlbumRepo(albumRepo);
            comment.setMusicRepo(musicRepo);
            comment.setNotificationRepo(notificationRepo);
            comment.setRequestRepo(requestRepo);
            comment.setCommentRepo(commentRepo);
        }

        for (LyricEditRequest request : requestRepo.findAll()) {
            request.setUserRepo(userRepo);
            request.setAlbumRepo(albumRepo);
            request.setMusicRepo(musicRepo);
            request.setNotificationRepo(notificationRepo);
            request.setRequestRepo(requestRepo);
            request.setCommentRepo(commentRepo);
        }

        for (Notification notif : notificationRepo.findAll()) {
            notif.setUserRepo(userRepo);
            notif.setAlbumRepo(albumRepo);
            notif.setMusicRepo(musicRepo);
            notif.setNotificationRepo(notificationRepo);
            notif.setRequestRepo(requestRepo);
            notif.setCommentRepo(commentRepo);
        }
    }
}
