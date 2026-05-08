package com.skilldistillery.artiststracker.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skilldistillery.artiststracker.entities.Event;
import com.skilldistillery.artiststracker.services.EventService;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin({ "*", "http://localhost/" })
@RequestMapping("api")
@RestController
public class EventController {

	@Autowired
	private EventService eventService;

	@GetMapping("events")
	public List<Event> getUpcomingEventsForFavorites(Principal principal, HttpServletResponse res) {
		List<Event> events = eventService.getUpcomingEventsForFavoriteArtists(principal.getName());
		if (events == null || events.isEmpty()) {
			res.setStatus(200);
		}
		return events;
	}

	@GetMapping("events/tracked")
	public List<Event> getTrackedEvents(Principal principal, HttpServletResponse res) {
		return eventService.getTrackedEvents(principal.getName());
	}

	@GetMapping("events/{eventId}")
	public Event getEvent(@PathVariable("eventId") Integer eventId, HttpServletResponse res) {
		Event event = eventService.findById(eventId);
		if (event == null) {
			res.setStatus(404);
		}
		return event;
	}

	@GetMapping("artists/{artistId}/events")
	public List<Event> getEventsForArtist(@PathVariable("artistId") Integer artistId, HttpServletResponse res) {
		List<Event> events = eventService.getUpcomingEventsForArtist(artistId);
		return events;
	}

	@GetMapping("events/search/{keyword}")
	public List<Event> searchEvents(@PathVariable("keyword") String keyword, HttpServletResponse res) {
		return eventService.searchEvents(keyword);
	}

	@PostMapping("events/{eventId}/track")
	public Event trackEvent(Principal principal, @PathVariable("eventId") Integer eventId, HttpServletResponse res) {
		Event event = eventService.trackEvent(principal.getName(), eventId);
		if (event == null) {
			res.setStatus(404);
		} else {
			res.setStatus(201);
		}
		return event;
	}

	@DeleteMapping("events/{eventId}/track")
	public void untrackEvent(Principal principal, @PathVariable("eventId") Integer eventId, HttpServletResponse res) {
		boolean removed = eventService.untrackEvent(principal.getName(), eventId);
		if (removed) {
			res.setStatus(204);
		} else {
			res.setStatus(404);
		}
	}

	@PostMapping("artists/{artistId}/events")
	public Event createEvent(@PathVariable("artistId") Integer artistId, @RequestBody Event event, HttpServletResponse res) {
		Event created = eventService.create(artistId, event);
		if (created != null) {
			res.setStatus(201);
		} else {
			res.setStatus(400);
		}
		return created;
	}

	@PutMapping("events/{eventId}")
	public Event updateEvent(@PathVariable("eventId") Integer eventId, @RequestBody Event event, HttpServletResponse res) {
		Event updated = eventService.update(eventId, event);
		if (updated == null) {
			res.setStatus(404);
		}
		return updated;
	}

	@DeleteMapping("events/{eventId}")
	public void deleteEvent(@PathVariable("eventId") Integer eventId, HttpServletResponse res) {
		boolean deleted = eventService.delete(eventId);
		if (deleted) {
			res.setStatus(204);
		} else {
			res.setStatus(404);
		}
	}
}
