import {Component} from '@angular/core';
import {Reaction} from "./reaction";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  reaction: Reaction = new Reaction();
}
