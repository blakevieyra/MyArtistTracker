package com.skilldistillery.artiststracker.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skilldistillery.artiststracker.entities.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {

	List<Event> findByArtist_Id(int artistId);

	List<Event> findByArtist_IdAndEventDateAfter(int artistId, LocalDateTime date);

	List<Event> findByArtist_IdInAndEventDateAfterOrderByEventDateAsc(List<Integer> artistIds, LocalDateTime date);

	List<Event> findByTrackers_UsernameOrderByEventDateAsc(String username);

	List<Event> findByNameContainingIgnoreCaseOrVenueContainingIgnoreCaseOrCityContainingIgnoreCase(
			String name, String venue, String city);

	Event findByExternalId(String externalId);
}
