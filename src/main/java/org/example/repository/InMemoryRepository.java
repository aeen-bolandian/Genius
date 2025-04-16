package org.example.repository;

import org.example.core.repository.IRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.account.Account;
import org.example.model.content.Album;
import org.example.model.content.Comment;
import org.example.model.content.Song;
import org.example.model.notification.Notification;
import org.example.model.request.LyricEditRequest;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepository<T, ID> implements IRepository<T, ID> {
    private final Map<ID, T> storage = new ConcurrentHashMap<>();
    private final Map<String, ID> usernameToIdMap = new ConcurrentHashMap<>(); // Username to ID mapping
    private final ObjectMapper objectMapper;
    private final String filePath;
    private final Class<T> entityType;
    private final RepositoryType type;

    public enum RepositoryType {
        ACCOUNT, SONG, NOTIFICATION, ALBUM, LYRIC_EDIT_REQUEST, COMMENT
    }

    public InMemoryRepository(Class<T> entityType, String filePath, ObjectMapper objectMapper) {
        this.entityType = entityType;
        this.filePath = filePath;
        this.objectMapper = objectMapper;

        // Determine repository type
        if (Account.class.isAssignableFrom(entityType)) {
            type = RepositoryType.ACCOUNT;
        } else if (Song.class.isAssignableFrom(entityType)) {
            type = RepositoryType.SONG;
        } else if (Notification.class.isAssignableFrom(entityType)) {
            type = RepositoryType.NOTIFICATION;
        } else if (Album.class.isAssignableFrom(entityType)) {
            type = RepositoryType.ALBUM;
        } else if (LyricEditRequest.class.isAssignableFrom(entityType)) {
            type = RepositoryType.LYRIC_EDIT_REQUEST;
        } else if (Comment.class.isAssignableFrom(entityType)) {
            type = RepositoryType.COMMENT;
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + entityType.getName());
        }

        loadFromFile();
    }

    @Override
    public T save(T entity) {
        ID id = getId(entity);
        storage.put(id, entity);

        // Special handling for Account entities
        if (type == RepositoryType.ACCOUNT) {
            Account account = (Account) entity;
            usernameToIdMap.put(account.getUsername().toLowerCase(), id); // Case-insensitive
        }

        saveToFile();
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<T> findByUserName(String username) {
        if (type != RepositoryType.ACCOUNT) {
            return Optional.empty();
        }
        ID id = usernameToIdMap.get(username.toLowerCase());
        return id != null ? Optional.ofNullable(storage.get(id)) : Optional.empty();
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(ID id) {
        if (type == RepositoryType.ACCOUNT) {
            T entity = storage.get(id);
            if (entity != null) {
                Account account = (Account) entity;
                usernameToIdMap.remove(account.getUsername().toLowerCase());
            }
        }
        storage.remove(id);
        saveToFile();
    }

    @Override
    public void saveAll() {
        saveToFile();
    }

    @Override
    public void loadAll() {
        loadFromFile();
    }

    @Override
    public boolean existsById(ID id) {
        return storage.containsKey(id);
    }

    private ID getId(T entity) {
        try {
            java.lang.reflect.Method getIdMethod = entity.getClass().getMethod("getId");
            @SuppressWarnings("unchecked")
            ID id = (ID) getIdMethod.invoke(entity);
            return id;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get ID from entity", e);
        }
    }

    private void loadFromFile() {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                T[] entities = objectMapper.readValue(file,
                        objectMapper.getTypeFactory()
                                .constructArrayType(entityType));

                // Clear existing data
                storage.clear();
                if (type == RepositoryType.ACCOUNT) {
                    usernameToIdMap.clear();
                }

                // Load new data
                Arrays.stream(entities).forEach(entity -> {
                    ID id = getId(entity);
                    storage.put(id, entity);

                    // Initialize username mapping for Accounts
                    if (type == RepositoryType.ACCOUNT) {
                        Account account = (Account) entity;
                        usernameToIdMap.put(account.getUsername().toLowerCase(), id);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from file", e);
        }
    }

    private void saveToFile() {
        try {
            objectMapper.writeValue(new File(filePath), findAll());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file", e);
        }
    }
}