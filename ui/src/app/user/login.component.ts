import { Component, OnInit } from '@angular/core';
import { User } from './user';
import { UserService } from './user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  user: User = new User();

  constructor(
    private userService: UserService
  ) { }

  login() {
    this.userService.login(this.user);
  }
}
