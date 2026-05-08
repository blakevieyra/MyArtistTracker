package com.skilldistillery.artiststracker.services;

import java.util.List;

import com.skilldistillery.artiststracker.entities.Event;

public interface EventService {

	List<Event> getEventsForArtist(int artistId);

	List<Event> getUpcomingEventsForArtist(int artistId);

	List<Event> getUpcomingEventsForFavoriteArtists(String username);

	List<Event> getTrackedEvents(String username);

	Event trackEvent(String username, int eventId);

	boolean untrackEvent(String username, int eventId);

	Event findById(int id);

	List<Event> searchEvents(String keyword);

	Event create(int artistId, Event event);

	Event update(int eventId, Event event);

	boolean delete(int eventId);
}
