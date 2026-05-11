package com.skilldistillery.artiststracker.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilldistillery.artiststracker.entities.Artist;
import com.skilldistillery.artiststracker.entities.User;
import com.skilldistillery.artiststracker.repositories.ArtistRepository;
import com.skilldistillery.artiststracker.repositories.UserRepository;

@Service
public class ArtistServiceImpl implements ArtistService {

	@Autowired
	private ArtistRepository artistRepo;

	@Autowired
	private UserRepository userRepo;

	@Override
	public Artist findArtistById(String username, int id) {
		return artistRepo.findByUsers_UsernameAndId(username, id);
	}

	@Override
	public Set<Artist> index(String username) {
		return artistRepo.findByUsers_Username(username);
	}

	@Override
	public List<Artist> getArtistByName(String username, String name) {
		return artistRepo.findByUsers_UsernameAndName(username, name);
	}

	@Override
	public List<Artist> keywordSearch(String username, String keyword) {
		keyword = "%" + keyword + "%";
		return artistRepo.findByUsers_UsernameAndNameLikeOrBandLike(username, keyword, keyword);
	}

	@Override
	public Artist create(String username, Artist artist) {
		User user = userRepo.findByUsername(username);
		if (user != null) {
			artist.getUsers().add(user);
			return artistRepo.saveAndFlush(artist);
		}
		return null;
	}

	@Override
	public Artist update(String username, int id, Artist artist) {
		Artist foundArtist = artistRepo.findByUsers_UsernameAndId(username, id);
		if (foundArtist != null) {
			foundArtist.setName(artist.getName());
			foundArtist.setBand(artist.getBand());
			return artistRepo.saveAndFlush(foundArtist);
		}
		return foundArtist;
	}

	@Override
	public boolean destroy(String username, int id) {
		boolean isDeleted = false;
		Artist artist = artistRepo.findByUsers_UsernameAndId(username, id);
		if (artist != null) {
			User user = userRepo.findByUsername(username);
			if (user != null && user.getFavoriteArtists() != null) {
				user.getFavoriteArtists().remove(artist);
				userRepo.saveAndFlush(user);
			}
			isDeleted = true;
		}
		return isDeleted;
	}
}
