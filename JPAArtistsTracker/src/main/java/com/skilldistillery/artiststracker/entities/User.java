package com.skilldistillery.artiststracker.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "\"user\"")
public class User {
	
	public User(){}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String username;

	private String password;
	
	private boolean enabled;

	private String email;

	private String role;

//	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "user_has_artist", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "artist_id"))
	private List<Artist> favoriteArtists;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "user_tracked_event", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "event_id"))
	private List<Event> trackedEvents;
	

	public List<Event> getTrackedEvents() {
		return trackedEvents;
	}

	public void setTrackedEvents(List<Event> trackedEvents) {
		this.trackedEvents = trackedEvents;
	}

	public void trackEvent(Event event) {
		if (trackedEvents == null) {
			trackedEvents = new ArrayList<>();
		}
		if (!trackedEvents.contains(event)) {
			trackedEvents.add(event);
		}
	}

	public void untrackEvent(Event event) {
		if (trackedEvents != null) {
			trackedEvents.remove(event);
		}
	}

	public void addArtist(Artist artist) {
		if (favoriteArtists == null) {
			favoriteArtists = new ArrayList<>();
		}
		if (!favoriteArtists.contains(artist)) {
			favoriteArtists.add(artist);
			artist.addUser(this);
		}
	}
	

	public void removeArtist(Artist artist) {
		if (favoriteArtists != null && favoriteArtists.contains(artist)) {
			favoriteArtists.remove(artist);
			artist.removeUser(this);
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Artist> getFavoriteArtists() {
		return favoriteArtists;
	}

	public void setFavoriteArtists(List<Artist> favoriteArtists) {
		this.favoriteArtists = favoriteArtists;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", enabled=" + enabled
				+ ", email=" + email + ", role=" + role + ", favoriteArtists=" + favoriteArtists + "]";
	}

}
