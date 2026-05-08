package com.skilldistillery.artiststracker.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilldistillery.artiststracker.entities.Artist;
import com.skilldistillery.artiststracker.entities.Event;
import com.skilldistillery.artiststracker.entities.User;
import com.skilldistillery.artiststracker.repositories.ArtistRepository;
import com.skilldistillery.artiststracker.repositories.EventRepository;
import com.skilldistillery.artiststracker.repositories.UserRepository;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository eventRepo;

	@Autowired
	private ArtistRepository artistRepo;

	@Autowired
	private UserRepository userRepo;

	@Override
	public List<Event> getEventsForArtist(int artistId) {
		return eventRepo.findByArtist_Id(artistId);
	}

	@Override
	public List<Event> getUpcomingEventsForArtist(int artistId) {
		return eventRepo.findByArtist_IdAndEventDateAfter(artistId, LocalDateTime.now());
	}

	@Override
	public List<Event> getUpcomingEventsForFavoriteArtists(String username) {
		User user = userRepo.findByUsername(username);
		if (user == null || user.getFavoriteArtists() == null) {
			return List.of();
		}
		List<Integer> artistIds = user.getFavoriteArtists().stream()
				.map(Artist::getId)
				.collect(Collectors.toList());
		if (artistIds.isEmpty()) {
			return List.of();
		}
		return eventRepo.findByArtist_IdInAndEventDateAfterOrderByEventDateAsc(artistIds, LocalDateTime.now());
	}

	@Override
	public List<Event> getTrackedEvents(String username) {
		return eventRepo.findByTrackers_UsernameOrderByEventDateAsc(username);
	}

	@Override
	public Event trackEvent(String username, int eventId) {
		User user = userRepo.findByUsername(username);
		Optional<Event> optEvent = eventRepo.findById(eventId);
		if (user == null || optEvent.isEmpty()) {
			return null;
		}
		Event event = optEvent.get();
		user.trackEvent(event);
		userRepo.saveAndFlush(user);
		return event;
	}

	@Override
	public boolean untrackEvent(String username, int eventId) {
		User user = userRepo.findByUsername(username);
		Optional<Event> optEvent = eventRepo.findById(eventId);
		if (user == null || optEvent.isEmpty()) {
			return false;
		}
		user.untrackEvent(optEvent.get());
		userRepo.saveAndFlush(user);
		return true;
	}

	@Override
	public Event findById(int id) {
		return eventRepo.findById(id).orElse(null);
	}

	@Override
	public List<Event> searchEvents(String keyword) {
		return eventRepo.findByNameContainingIgnoreCaseOrVenueContainingIgnoreCaseOrCityContainingIgnoreCase(
				keyword, keyword, keyword);
	}

	@Override
	public Event create(int artistId, Event event) {
		Optional<Artist> optArtist = artistRepo.findById(artistId);
		if (optArtist.isEmpty()) {
			return null;
		}
		event.setArtist(optArtist.get());
		event.setCreatedAt(LocalDateTime.now());
		return eventRepo.saveAndFlush(event);
	}

	@Override
	public Event update(int eventId, Event event) {
		Optional<Event> optExisting = eventRepo.findById(eventId);
		if (optExisting.isEmpty()) {
			return null;
		}
		Event existing = optExisting.get();
		existing.setName(event.getName());
		existing.setVenue(event.getVenue());
		existing.setCity(event.getCity());
		existing.setState(event.getState());
		existing.setCountry(event.getCountry());
		existing.setEventDate(event.getEventDate());
		existing.setTicketUrl(event.getTicketUrl());
		existing.setMinPrice(event.getMinPrice());
		existing.setMaxPrice(event.getMaxPrice());
		existing.setCurrency(event.getCurrency());
		existing.setSource(event.getSource());
		existing.setExternalId(event.getExternalId());
		existing.setImageUrl(event.getImageUrl());
		return eventRepo.saveAndFlush(existing);
	}

	@Override
	public boolean delete(int eventId) {
		if (eventRepo.existsById(eventId)) {
			eventRepo.deleteById(eventId);
			return true;
		}
		return false;
	}
}
