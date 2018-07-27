import {Component} from "@angular/core";
import {User} from "./user";
import {UserService} from "./user.service";

@Component({
  selector: 'create-account',
  templateUrl: './create-account.component.html',
})
export class CreateAccountComponent {
  user: User = new User();
  repeatPassword: string;

  constructor(
    private userService: UserService
  ) {}

  createAccount() {
    this.userService.createNewUser(this.user).subscribe(user => {this.user = user});
  }
}
