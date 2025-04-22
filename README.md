# Music Platform

## Overview
The Music Platform is a Java-based application designed to manage music-related content, including songs, albums, comments, and lyric edit requests. It supports different user roles (Admin, Artist, and User) with distinct functionalities, such as publishing music, following artists, and managing lyric edit requests. The application uses an in-memory repository with JSON persistence for data storage and includes a console-based user interface.

## Features

### User Roles
- **Admin**: Manages lyric edit requests, searches for songs, artists, and users, and views notifications.
- **Artist**: Publishes and removes songs/albums, manages lyric edit requests, views song statistics (most viewed, liked, disliked), and notifies followers.
- **User**: Follows artists, likes/dislikes songs, adds comments, submits lyric edit requests, and manages favorite songs.

### Content Management
- Songs with attributes like title, artist, lyrics, likes, dislikes, views, and comments.
- Albums containing multiple songs.
- Comments and notifications for user interaction.
- Lyric edit requests for collaborative lyric updates.

### Search Functionality
Search for songs, artists, and users by name.

### Notifications
System for sending and viewing notifications (e.g., new song/album, request status).

### Authentication
Sign-up, login, and logout functionality with basic validation.

## Technologies Used
- **Java**: Core programming language.
- **Jackson**: For JSON serialization/deserialization.
- **InMemoryRepository**: Custom repository for in-memory data storage with JSON file persistence.
- **Console UI**: Scanner-based interface for user interaction.

## Project Structure
```
src/
├── org.example/
│   ├── Main.java                # Entry point of the application
│   ├── SeedData.java           # Seeds initial data for testing
│   ├── core/
│   │   └── repository/
│   │       └── IRepository.java # Repository interface
│   ├── model/
│   │   ├── account/
│   │   │   ├── Account.java    # Base account class
│   │   │   ├── Admin.java      # Admin-specific functionality
│   │   │   ├── Artist.java     # Artist-specific functionality
│   │   │   ├── User.java       # User-specific functionality
│   │   ├── content/
│   │   │   ├── Album.java      # Album entity
│   │   │   ├── Comment.java    # Comment entity
│   │   │   ├── Song.java       # Song entity
│   │   ├── notification/
│   │   │   └── Notification.java # Notification entity
│   │   ├── page/
│   │   │   ├── AdminPage.java  # Admin UI logic
│   │   │   ├── ArtistPage.java # Artist UI logic
│   │   │   ├── UserPage.java   # User UI logic
│   │   ├── request/
│   │   │   └── LyricEditRequest.java # Lyric edit request entity
│   ├── repository/
│   │   └── InMemoryRepository.java # In-memory repository implementation
│   ├── services/
│   │   └── AuthService.java    # Authentication service
```

## Setup and Installation

### Prerequisites
- Java 11 or higher
- Maven (for dependency management)

### Clone the Repository
```bash
git clone <repository-url>
cd music-platform
```

### Install Dependencies
Ensure the `pom.xml` includes the Jackson dependency:
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>
```
Run:
```bash
mvn install
```

### Run the Application
```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

### Initial Data
- The application seeds initial data (admin, artist, user, song, album, comment, notification, and lyric edit request) if the repository is empty.
- Admin credentials: Username: `admin`, Password: `admin123`

## Usage

### Start the Application
Run `Main.java` to launch the console-based interface.

### Main Menu
- **Sign Up**: Create a new Artist or User account.
- **Login**: Log in with existing credentials.
- **Exit**: Save data to JSON files and exit.

### Role-Specific Actions

#### Admin
- View notifications.
- Search songs, artists, or users.
- Approve/reject lyric edit requests.

#### Artist
- Publish/remove songs or albums.
- View song statistics (most viewed, liked, disliked).
- Approve/reject lyric edit requests.
- Notify followers about new content.

#### User
- Follow/unfollow artists.
- Like/dislike songs.
- Add comments or submit lyric edit requests.
- View favorite songs and notifications.

### Data Persistence
- Data is saved to JSON files (`accounts.json`, `albums.json`, `musics.json`, `notifications.json`, `requests.json`, `comments.json`) on exit or after modifications.

## Example Workflow

### Sign Up as a User
- Choose "Sign Up" and enter details (name, lastname, email, username, password).
- Select role: User.

### Log In
- Enter username and password.
- Access the User page.

### Search and Interact
- Search for a song (e.g., "Echoes of Code").
- Like the song or submit a lyric edit request.
- Follow the artist (e.g., "bobmusic").

### Artist Actions
- Log in as an artist.
- View lyric edit requests and approve/reject them.
- Publish a new song or album.

### Admin Actions
- Log in as admin (username: `admin`, password: `admin123`).
- Approve/reject lyric edit requests.
- Search for users or artists.

## Known Issues
- **Console UI Limitations**: The Scanner-based interface may require careful input handling.
- **Password Security**: Passwords are stored in plaintext; consider hashing for production.
- **Error Handling**: Some edge cases (e.g., invalid inputs) may need better validation.
- **Duplicate User Class**: The `User.java` class appears twice in the codebase, which should be resolved by removing the redundant file.

## Future Improvements
- Implement a graphical user interface (e.g., JavaFX or web-based).
- Add password hashing (e.g., BCrypt).
- Support advanced search filters (e.g., by genre, release date).
- Introduce unit tests for core functionalities.
- Enhance error handling for invalid inputs.

## Contributing
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature-name`).
3. Commit changes (`git commit -m "Add feature"`).
4. Push to the branch (`git push origin feature-name`).
5. Create a pull request.

## License
This project is licensed under the SBU License.