import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { NavigationComponent } from './components/navigation/navigation.component';
import { LoginComponent } from './components/login/login.component';
import { LogoutComponent } from './components/logout/logout.component';
import { RegisterComponent } from './components/register/register.component';
import { SongsComponent } from './components/songs/songs.component';
import { AboutComponent } from './components/about/about.component';
import { ArtistsComponent } from './components/artists/artists.component';
import { CarouselComponent } from './components/carousel/carousel.component';
import { EventsComponent } from './components/events/events.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HomeComponent,
    NavigationComponent,
    LoginComponent,
    LogoutComponent,
    RegisterComponent,
    ArtistsComponent,
    SongsComponent,
    AboutComponent,
    CarouselComponent,
    EventsComponent
  ],

  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'ngArtistsTracker';
}
