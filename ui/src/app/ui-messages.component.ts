import {Component} from '@angular/core';
import {UIMessageService} from "./ui-message.service";

@Component({
  selector: 'ui-messages',
  templateUrl: './ui-messages.component.html'
})
export class UIMessagesComponent {

  constructor(public uiMessageService: UIMessageService) {
  }

}
