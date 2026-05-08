package com.skilldistillery.artiststracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.skilldistillery.artiststracker.entities.Artist;
import com.skilldistillery.artiststracker.entities.Event;
import com.skilldistillery.artiststracker.entities.User;
import com.skilldistillery.artiststracker.repositories.ArtistRepository;
import com.skilldistillery.artiststracker.repositories.EventRepository;
import com.skilldistillery.artiststracker.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (artistRepo.count() > 0) {
            return;
        }

        User blake = userRepo.findByUsername("blake");
        if (blake == null) {
            blake = new User();
            blake.setUsername("blake");
            blake.setPassword(encoder.encode("blake"));
            blake.setEnabled(true);
            blake.setRole("standard");
            blake = userRepo.saveAndFlush(blake);
        }

        String[][] artistData = {
            {"2Pac", null}, {"Adele", null}, {"Aerosmith", null},
            {"Alabama Shakes", null}, {"Arctic Monkeys", null},
            {"Beyonce", null}, {"Bob Dylan", null}, {"Coldplay", null},
            {"Drake", null}, {"Ed Sheeran", null}, {"Eminem", null},
            {"Foo Fighters", null}, {"Green Day", null}, {"Imagine Dragons", null},
            {"Jay-Z", null}, {"Kanye West", null}, {"Led Zeppelin", null},
            {"Metallica", null}, {"Nirvana", null}, {"Oasis", null}
        };

        List<Artist> artists = new ArrayList<>();
        for (String[] data : artistData) {
            Artist a = new Artist();
            a.setName(data[0]);
            a.setBand(data[1]);
            a.setUsers(new ArrayList<>());
            a.getUsers().add(blake);
            artists.add(artistRepo.saveAndFlush(a));
        }

        if (blake.getFavoriteArtists() == null) {
            blake.setFavoriteArtists(new ArrayList<>());
        }
        blake.getFavoriteArtists().addAll(artists);
        userRepo.saveAndFlush(blake);

        Object[][] eventData = {
            {"2Pac Tribute Concert", "Madison Square Garden", "New York", "NY", "US", 2026, 7, 15, 20, 0, 75.0, 350.0, "https://www.stubhub.com/event/123456", 0},
            {"Adele Live 2026", "The O2 Arena", "London", null, "UK", 2026, 8, 20, 19, 30, 120.0, 800.0, "https://www.stubhub.com/event/234567", 1},
            {"Adele - Las Vegas Residency", "Caesars Palace", "Las Vegas", "NV", "US", 2026, 9, 10, 21, 0, 200.0, 1500.0, "https://www.stubhub.com/event/234568", 1},
            {"Aerosmith Farewell Tour", "Fenway Park", "Boston", "MA", "US", 2026, 6, 28, 19, 0, 90.0, 500.0, "https://www.stubhub.com/event/345678", 2},
            {"Alabama Shakes Reunion Show", "Ryman Auditorium", "Nashville", "TN", "US", 2026, 10, 5, 20, 0, 65.0, 250.0, "https://www.stubhub.com/event/456789", 3},
            {"Arctic Monkeys World Tour", "Red Rocks Amphitheatre", "Morrison", "CO", "US", 2026, 8, 15, 19, 30, 85.0, 400.0, "https://www.stubhub.com/event/567890", 4},
            {"Beyonce Renaissance Tour", "SoFi Stadium", "Los Angeles", "CA", "US", 2026, 11, 1, 20, 0, 150.0, 2000.0, "https://www.stubhub.com/event/678901", 5},
            {"Coldplay Music of the Spheres", "Wembley Stadium", "London", null, "UK", 2026, 7, 25, 18, 0, 95.0, 600.0, "https://www.stubhub.com/event/789012", 7},
            {"Drake Summer Tour", "Scotiabank Arena", "Toronto", "ON", "CA", 2026, 8, 5, 20, 0, 110.0, 750.0, "https://www.stubhub.com/event/890123", 8},
            {"Foo Fighters Stadium Tour", "Soldier Field", "Chicago", "IL", "US", 2026, 9, 20, 19, 0, 80.0, 450.0, "https://www.stubhub.com/event/901234", 11},
        };

        for (Object[] e : eventData) {
            Event event = new Event();
            event.setName((String) e[0]);
            event.setVenue((String) e[1]);
            event.setCity((String) e[2]);
            event.setState((String) e[3]);
            event.setCountry((String) e[4]);
            event.setEventDate(LocalDateTime.of((int) e[5], (int) e[6], (int) e[7], (int) e[8], (int) e[9]));
            event.setMinPrice((Double) e[10]);
            event.setMaxPrice((Double) e[11]);
            event.setTicketUrl((String) e[12]);
            event.setCurrency("USD");
            event.setSource("stubhub");
            event.setArtist(artists.get((int) e[13]));
            event.setCreatedAt(LocalDateTime.now());
            eventRepo.saveAndFlush(event);
        }

        System.out.println("=== DATABASE SEEDED: 20 artists, 10 events, 1 user (blake/blake) ===");
    }
}
