package org.example.model.account;

import org.example.model.content.Song;
import org.example.model.request.LyricEditRequest;
import org.example.repository.InMemoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Admin extends Account {

    private List<String> requests = new ArrayList<>();

    public Admin() { setAccountType("ADMIN"); }
    public Admin(String name, String lastName, String username , String email, String password) {
        super(name, lastName, username, email, password);
        setAccountType("ADMIN");
        setId(UUID.randomUUID().toString());
    }

    public List<String> getRequests() {
        return requests;
    }

    public void setRequests(List<String> requests) {
        this.requests = requests;
    }

    public void ViewRequests() {
        int counter = 1;
        for(LyricEditRequest request : requestsInList()) {
            System.out.print(counter + ". request for ");
            if(musicRepo.findById(request.getSong()).isPresent()) {
                System.out.println(musicRepo.findById(request.getSong()).get().getTitle());
            }
            counter++;
        }
    }

    public void approveRequest(LyricEditRequest request) {
        if(!userRepo.findById(request.getSong()).get().isActive()) {
            request.setStatus(LyricEditRequest.Status.APPROVED);
            musicRepo.findById(request.getSong()).ifPresent(music -> {
                music.setLyrics(request.getLyric());
            });
            System.out.println("request has been approved");
            requestRepo.save(request);
        }
        else {
            System.out.println("artist is active");
        }
    }

    public void rejectRequest(LyricEditRequest request) {
        if(!userRepo.findById(request.getSong()).get().isActive()) {
            request.setStatus(LyricEditRequest.Status.REJECTED);
            System.out.println("request has been rejected");
            requestRepo.save(request);
        }
        else {
            System.out.println("artist is active");
        }
    }

    public void addRequest(LyricEditRequest request) {
        requests.add(request.getId());
        userRepo.save(this);
    }

    private List<LyricEditRequest> requestsInList() {
        List<LyricEditRequest> requests = new ArrayList<>();
        for (String request : this.requests) {
            requestRepo.findById(request).ifPresent(requests::add);
        }
        return requests;
    }
}
