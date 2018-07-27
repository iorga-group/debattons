import { Injectable } from '@angular/core';
import { Reaction } from './reaction';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { catchError } from 'rxjs/operators';
import { UserService } from './user/user.service';

@Injectable()
export class ReactionService {

  constructor(
    private http: HttpClient,
    private userService: UserService,
  ) { }

  createNewReaction(reaction: Reaction, reactToReactionId?: string): Observable<Reaction> {
    let url = environment.apiBaseContext + '/reactions/';
    if (reactToReactionId) {
      url += '?reactToReactionId=' + encodeURIComponent(reactToReactionId);
    }
    return this.http.post(url, reaction)
      .pipe(
        catchError(this.handleError('createNewReaction', null))
      );
  }

  findRoot(): Observable<Reaction[]> {
    return this.http.get<Reaction[]>(environment.apiBaseContext + '/reactions/roots')
      .pipe(
        catchError(this.handleError('findRoot', []))
      );
  }

  findByIdLoadingDepth(id: string, depth: number): Observable<Reaction> {
    return this.http.get(`${environment.apiBaseContext}/reactions/${encodeURIComponent(id)}?reactedToDepth=${depth}`)
      .pipe(
        catchError(this.handleError('findByIdLoadingDepth', null))
      );
  }

  agreeWithById(reactToReactionId: string): Observable<void> {
    return this.http.post(`${environment.apiBaseContext}/reactions/${encodeURIComponent(reactToReactionId)}?reactionType=agree`, null)
      .pipe(
        catchError(this.handleError('agreeWithById', null))
      );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      //this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
