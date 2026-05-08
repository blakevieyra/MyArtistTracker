export class ArtistEvent {
  id: number;
  name: string;
  venue: string;
  city: string;
  state: string;
  country: string;
  eventDate: string;
  ticketUrl: string;
  minPrice: number;
  maxPrice: number;
  currency: string;
  source: string;
  externalId: string;
  imageUrl: string;
  artist: { id: number; name: string; band: string };
  tracked: boolean;

  constructor(
    id: number = 0,
    name: string = '',
    venue: string = '',
    city: string = '',
    state: string = '',
    country: string = 'US',
    eventDate: string = '',
    ticketUrl: string = '',
    minPrice: number = 0,
    maxPrice: number = 0,
    currency: string = 'USD',
    source: string = 'stubhub',
    externalId: string = '',
    imageUrl: string = '',
    artist: { id: number; name: string; band: string } = { id: 0, name: '', band: '' },
    tracked: boolean = false
  ) {
    this.id = id;
    this.name = name;
    this.venue = venue;
    this.city = city;
    this.state = state;
    this.country = country;
    this.eventDate = eventDate;
    this.ticketUrl = ticketUrl;
    this.minPrice = minPrice;
    this.maxPrice = maxPrice;
    this.currency = currency;
    this.source = source;
    this.externalId = externalId;
    this.imageUrl = imageUrl;
    this.artist = artist;
    this.tracked = tracked;
  }
}
