// spotify.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class SpotifyService {
  private clientId: string = 'a93962aa13c948e2be74922153604b4a';
  private clientSecret: string = '4a1fff2920f54ff190fbcd7a8bd2fb07';
  private tokenUrl: string = 'https://accounts.spotify.com/api/token';
  private searchUrl: string = 'https://api.spotify.com/v1/search';

  constructor(private http: HttpClient) {}

  getToken(): Observable<string> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      Authorization: 'Basic ' + btoa(this.clientId + ':' + this.clientSecret),
    });
    const body = 'grant_type=client_credentials';
    return this.http
      .post<any>(this.tokenUrl, body, { headers })
      .pipe(map((data) => data.access_token));
  }

  searchTrack(token: string, query: string): Observable<any> {
    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + token,
    });
    return this.http
      .get<any>(
        `${this.searchUrl}?q=${encodeURIComponent(query)}&type=track&limit=1`,
        { headers }
      )
      .pipe(map((data) => data.tracks.items[0]));
  }

  searchArtist(token: string, query: string): Observable<any> {
    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + token,
    });
    return this.http
      .get<any>(
        `${this.searchUrl}?q=${encodeURIComponent(query)}&type=artist&limit=1`,
        { headers }
      )
      .pipe(map((data) => data.artists.items[0]));
  }
}
