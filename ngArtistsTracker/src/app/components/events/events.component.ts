import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ArtistEvent } from '../../models/event';
import { EventService } from '../../services/event.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-events',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './events.component.html',
  styleUrl: './events.component.css',
})
export class EventsComponent implements OnInit {
  upcomingEvents: ArtistEvent[] = [];
  trackedEvents: ArtistEvent[] = [];
  searchResults: ArtistEvent[] = [];
  searchKeyword: string = '';
  activeTab: string = 'upcoming';
  trackedIds: Set<number> = new Set();

  constructor(
    private eventService: EventService,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    this.loadUpcomingEvents();
    this.loadTrackedEvents();
  }

  loggedIn(): boolean {
    return this.auth.checkLogin();
  }

  loadUpcomingEvents(): void {
    this.eventService.getUpcomingEventsForFavorites().subscribe({
      next: (events) => {
        this.upcomingEvents = events;
      },
      error: (err) => {
        console.error('Error loading upcoming events:', err);
      },
    });
  }

  loadTrackedEvents(): void {
    this.eventService.getTrackedEvents().subscribe({
      next: (events) => {
        this.trackedEvents = events;
        this.trackedIds = new Set(events.map((e) => e.id));
      },
      error: (err) => {
        console.error('Error loading tracked events:', err);
      },
    });
  }

  searchEvents(): void {
    if (!this.searchKeyword.trim()) return;
    this.eventService.searchEvents(this.searchKeyword).subscribe({
      next: (events) => {
        this.searchResults = events;
        this.activeTab = 'search';
      },
      error: (err) => {
        console.error('Error searching events:', err);
      },
    });
  }

  trackEvent(event: ArtistEvent): void {
    this.eventService.trackEvent(event.id).subscribe({
      next: () => {
        this.trackedIds.add(event.id);
        this.loadTrackedEvents();
      },
      error: (err) => {
        console.error('Error tracking event:', err);
      },
    });
  }

  untrackEvent(event: ArtistEvent): void {
    this.eventService.untrackEvent(event.id).subscribe({
      next: () => {
        this.trackedIds.delete(event.id);
        this.loadTrackedEvents();
      },
      error: (err) => {
        console.error('Error untracking event:', err);
      },
    });
  }

  isTracked(event: ArtistEvent): boolean {
    return this.trackedIds.has(event.id);
  }

  setTab(tab: string): void {
    this.activeTab = tab;
  }

  formatDate(dateStr: string): string {
    if (!dateStr) return 'TBD';
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', {
      weekday: 'short',
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  }

  formatPrice(min: number, max: number, currency: string): string {
    if (!min && !max) return 'Price TBD';
    if (min && max) return `$${min} - $${max} ${currency}`;
    if (min) return `From $${min} ${currency}`;
    return `Up to $${max} ${currency}`;
  }

  getTicketSource(source: string): string {
    switch (source?.toLowerCase()) {
      case 'stubhub':
        return 'StubHub';
      case 'ticketmaster':
        return 'Ticketmaster';
      case 'seatgeek':
        return 'SeatGeek';
      default:
        return source || 'Tickets';
    }
  }
}
