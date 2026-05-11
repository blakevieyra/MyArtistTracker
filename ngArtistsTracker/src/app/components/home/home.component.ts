import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Artist } from './../../models/artist';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ArtistService } from '../../services/artist.service';
import { RegisterComponent } from '../register/register.component';
import { LoginComponent } from '../login/login.component';
import { AuthService } from '../../services/auth.service';
import { CarouselComponent } from '../carousel/carousel.component';
import { SpotifyService } from '../../services/spotify.service';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [
    CommonModule,
    FormsModule,
    RegisterComponent,
    LoginComponent,
    CarouselComponent,
  ],
})
export class HomeComponent implements OnInit {
  constructor(
    private artistService: ArtistService,
    private auth: AuthService,
    private spotifyService: SpotifyService,
    private route: Router
  ) {}

  keyword: string = '';
  token: string = '';
  searchType: string = 'artist';

  spotifyArtists: any[] = [];
  spotifyTracks: any[] = [];
  selectedArtist: any = null;
  relatedArtists: any[] = [];
  artistTopTracks: any[] = [];
  artistAlbums: any[] = [];
  addedMessage: string = '';

  ngOnInit(): void {
    this.spotifyService.getToken().subscribe((token) => {
      this.token = token;
    });
  }

  loggedIn(): boolean {
    return this.auth.checkLogin();
  }

  search(): void {
    if (!this.keyword.trim() || !this.token) return;
    this.selectedArtist = null;
    this.relatedArtists = [];
    this.artistTopTracks = [];
    this.artistAlbums = [];

    if (this.searchType === 'artist') {
      this.spotifyService.searchArtists(this.token, this.keyword, 12).subscribe(artists => {
        this.spotifyArtists = artists;
        this.spotifyTracks = [];
      });
    } else {
      this.spotifyService.searchTracks(this.token, this.keyword, 15).subscribe(tracks => {
        this.spotifyTracks = tracks;
        this.spotifyArtists = [];
      });
    }
  }

  selectArtist(artist: any): void {
    this.selectedArtist = artist;
    this.spotifyService.getRelatedArtists(this.token, artist.id).subscribe(related => {
      this.relatedArtists = related.slice(0, 8);
    });
    this.spotifyService.getArtistTopTracks(this.token, artist.id).subscribe(tracks => {
      this.artistTopTracks = tracks;
    });
    this.spotifyService.getArtistAlbums(this.token, artist.id).subscribe(albums => {
      this.artistAlbums = albums;
    });
  }

  addToFavorites(artist: any): void {
    const newArtist = new Artist();
    newArtist.name = artist.name;
    newArtist.band = artist.genres?.[0] || '';
    this.artistService.create(newArtist).subscribe({
      next: () => {
        this.addedMessage = `Added "${artist.name}" to your favorites!`;
        setTimeout(() => this.addedMessage = '', 3000);
      },
      error: () => {
        this.addedMessage = `Could not add "${artist.name}" (may already exist).`;
        setTimeout(() => this.addedMessage = '', 3000);
      },
    });
  }

  backToResults(): void {
    this.selectedArtist = null;
    this.relatedArtists = [];
    this.artistTopTracks = [];
    this.artistAlbums = [];
  }

  goToTickets(artistName: string): void {
    this.route.navigate(['/events'], { queryParams: { artist: artistName } });
  }

  getArtistImage(artist: any): string {
    return artist?.images?.[0]?.url || 'https://placehold.co/200x200/333/white?text=No+Image';
  }

  getTrackImage(track: any): string {
    return track?.album?.images?.[0]?.url || 'https://placehold.co/60x60/333/white?text=Art';
  }

  formatDuration(ms: number): string {
    const min = Math.floor(ms / 60000);
    const sec = Math.floor((ms % 60000) / 1000);
    return `${min}:${sec < 10 ? '0' : ''}${sec}`;
  }
}
