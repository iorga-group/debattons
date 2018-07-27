import { Component } from '@angular/core';
import { UserService } from './user/user.service';
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  displayLoginScreen: Observable<boolean>;

  constructor(private userService: UserService) {
    this.displayLoginScreen = userService.displayLoginScreen;
  }
}
