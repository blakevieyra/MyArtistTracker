import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class SpotifyService {
  private clientId: string = 'a93962aa13c948e2be74922153604b4a';
  private clientSecret: string = '4a1fff2920f54ff190fbcd7a8bd2fb07';
  private tokenUrl: string = 'https://accounts.spotify.com/api/token';
  private searchUrl: string = 'https://api.spotify.com/v1/search';
  private apiUrl: string = 'https://api.spotify.com/v1';

  constructor(private http: HttpClient) {}

  private getHeaders(token: string): HttpHeaders {
    return new HttpHeaders({ Authorization: 'Bearer ' + token });
  }

  getToken(): Observable<string> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      Authorization: 'Basic ' + btoa(this.clientId + ':' + this.clientSecret),
    });
    const body = 'grant_type=client_credentials';
    return this.http
      .post<any>(this.tokenUrl, body, { headers })
      .pipe(
        map((data) => data.access_token),
        catchError(() => of(''))
      );
  }

  searchTrack(token: string, query: string): Observable<any> {
    return this.http
      .get<any>(
        `${this.searchUrl}?q=${encodeURIComponent(query)}&type=track&limit=1`,
        { headers: this.getHeaders(token) }
      )
      .pipe(
        map((data) => data.tracks.items[0]),
        catchError(() => of(null))
      );
  }

  searchArtist(token: string, query: string): Observable<any> {
    return this.http
      .get<any>(
        `${this.searchUrl}?q=${encodeURIComponent(query)}&type=artist&limit=1`,
        { headers: this.getHeaders(token) }
      )
      .pipe(
        map((data) => data.artists.items[0]),
        catchError(() => of(null))
      );
  }

  searchArtists(token: string, query: string, limit: number = 10): Observable<any[]> {
    return this.http
      .get<any>(
        `${this.searchUrl}?q=${encodeURIComponent(query)}&type=artist&limit=${limit}`,
        { headers: this.getHeaders(token) }
      )
      .pipe(
        map((data) => data.artists.items || []),
        catchError(() => of([]))
      );
  }

  searchTracks(token: string, query: string, limit: number = 10): Observable<any[]> {
    return this.http
      .get<any>(
        `${this.searchUrl}?q=${encodeURIComponent(query)}&type=track&limit=${limit}`,
        { headers: this.getHeaders(token) }
      )
      .pipe(
        map((data) => data.tracks.items || []),
        catchError(() => of([]))
      );
  }

  getRelatedArtists(token: string, artistId: string): Observable<any[]> {
    return this.http
      .get<any>(
        `${this.apiUrl}/artists/${artistId}/related-artists`,
        { headers: this.getHeaders(token) }
      )
      .pipe(
        map((data) => data.artists || []),
        catchError(() => of([]))
      );
  }

  getArtistTopTracks(token: string, artistId: string): Observable<any[]> {
    return this.http
      .get<any>(
        `${this.apiUrl}/artists/${artistId}/top-tracks?market=US`,
        { headers: this.getHeaders(token) }
      )
      .pipe(
        map((data) => data.tracks || []),
        catchError(() => of([]))
      );
  }

  getArtistAlbums(token: string, artistId: string): Observable<any[]> {
    return this.http
      .get<any>(
        `${this.apiUrl}/artists/${artistId}/albums?include_groups=album,single&market=US&limit=20`,
        { headers: this.getHeaders(token) }
      )
      .pipe(
        map((data) => data.items || []),
        catchError(() => of([]))
      );
  }
}
