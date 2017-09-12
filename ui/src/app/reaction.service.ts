import {Injectable} from '@angular/core';
import {Reaction} from "./reaction";
import {Http} from "@angular/http";
import {environment} from "../environments/environment";

import 'rxjs/add/operator/toPromise';

@Injectable()
export class ReactionService {
  constructor(private http: Http) {
  }

  createNewReaction(reaction: Reaction): Promise<Reaction> {
    return this.http.post(environment.apiBaseContext + '/reaction/', reaction)
      .toPromise()
      .then(response => response.json() as Reaction)
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}
