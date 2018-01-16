import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {User} from "./user";
import {environment} from "../environments/environment";
import {Observable} from "rxjs/Observable";
import { catchError, map, tap } from 'rxjs/operators';
import {of} from "rxjs/observable/of";
import {UIMessageService} from "./ui-message.service";

@Injectable()
export class UserService {
  constructor(
    private http: HttpClient,
    private uiMessageService: UIMessageService
  ) {}

  createNewUser(user: User): Observable<User> {
    let url = environment.apiBaseContext + '/users/';

    return this.http.post<User>(url, user).pipe(
      catchError(this.handleError('createNewUser', user))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      console.error(error); // log to console instead

      this.uiMessageService.add(`A problem occurred (${operation} failed: ${error.message})`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
