import {Injectable} from '@angular/core';
import {Reaction} from "./reaction";
import {Http} from "@angular/http";
import {environment} from "../environments/environment";

import 'rxjs/add/operator/toPromise';

@Injectable()
export class ReactionService {
  constructor(private http: Http,) {
  }

  createNewReaction(reaction: Reaction, reactToReactionId: string): Promise<Reaction> {
    let url = environment.apiBaseContext + '/reactions/';
    if (reactToReactionId) {
      url += '?reactToReactionId=' + encodeURIComponent(reactToReactionId);
    }
    return this.http.post(url, reaction)
      .toPromise()
      .then(response => response.json() as Reaction)
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

  findRoot(): Promise<Reaction[]> {
    return this.http.get(environment.apiBaseContext + '/reactions/roots')
      .toPromise()
      .then(response => response.json() as Reaction[])
      .catch(this.handleError);
  }

  findByIdLoadingDepth(id: string, depth: number): Promise<Reaction> {
    return this.http.get(environment.apiBaseContext + '/reactions/' + encodeURIComponent(id) + '?reactedToDepth=' + depth)
      .toPromise()
      .then(response => response.json() as Reaction)
      .catch(this.handleError);
  }
}
