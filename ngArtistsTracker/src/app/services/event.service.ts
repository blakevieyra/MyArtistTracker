import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { ArtistEvent } from '../models/event';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class EventService {
  private url = environment.baseUrl;

  constructor(private http: HttpClient, private auth: AuthService) {}

  getHttpOptions() {
    return {
      headers: {
        Authorization: 'Basic ' + this.auth.getCredentials(),
        'X-Requested-With': 'XMLHttpRequest',
      },
    };
  }

  getUpcomingEventsForFavorites(): Observable<ArtistEvent[]> {
    return this.http
      .get<ArtistEvent[]>(this.url + 'api/events', this.getHttpOptions())
      .pipe(
        catchError((err: any) => {
          console.log(err);
          return throwError(
            () => new Error('EventService.getUpcomingEvents(): error: ' + err)
          );
        })
      );
  }

  getTrackedEvents(): Observable<ArtistEvent[]> {
    return this.http
      .get<ArtistEvent[]>(this.url + 'api/events/tracked', this.getHttpOptions())
      .pipe(
        catchError((err: any) => {
          console.log(err);
          return throwError(
            () => new Error('EventService.getTrackedEvents(): error: ' + err)
          );
        })
      );
  }

  getEventsForArtist(artistId: number): Observable<ArtistEvent[]> {
    return this.http
      .get<ArtistEvent[]>(
        this.url + 'api/artists/' + artistId + '/events',
        this.getHttpOptions()
      )
      .pipe(
        catchError((err: any) => {
          console.log(err);
          return throwError(
            () => new Error('EventService.getEventsForArtist(): error: ' + err)
          );
        })
      );
  }

  searchEvents(keyword: string): Observable<ArtistEvent[]> {
    return this.http
      .get<ArtistEvent[]>(
        this.url + 'api/events/search/' + keyword,
        this.getHttpOptions()
      )
      .pipe(
        catchError((err: any) => {
          console.log(err);
          return throwError(
            () => new Error('EventService.searchEvents(): error: ' + err)
          );
        })
      );
  }

  trackEvent(eventId: number): Observable<ArtistEvent> {
    return this.http
      .post<ArtistEvent>(
        this.url + 'api/events/' + eventId + '/track',
        {},
        this.getHttpOptions()
      )
      .pipe(
        catchError((err: any) => {
          console.log(err);
          return throwError(
            () => new Error('EventService.trackEvent(): error: ' + err)
          );
        })
      );
  }

  untrackEvent(eventId: number): Observable<void> {
    return this.http
      .delete<void>(
        this.url + 'api/events/' + eventId + '/track',
        this.getHttpOptions()
      )
      .pipe(
        catchError((err: any) => {
          console.log(err);
          return throwError(
            () => new Error('EventService.untrackEvent(): error: ' + err)
          );
        })
      );
  }
}
