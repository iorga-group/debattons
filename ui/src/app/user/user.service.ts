import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs/Observable";
import { catchError, map, tap } from 'rxjs/operators';
import {of} from "rxjs/observable/of";
import {UIMessageService} from "../ui-message.service";
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { User } from './user';

@Injectable()
export class UserService {
  displayLoginScreen = new BehaviorSubject(false);
  private user = new BehaviorSubject<User>(null);

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

  login(user: User) {
    this.displayLoginScreen.next(false);
    this.user.next(user);
  }

  waitForUserDisplayingLoginScreenIfNecessary(): Observable<User> {
    return Observable.create(subscriber => {
      const subscription = this.user.subscribe(user => {
        if (user === null) {
          this.displayLoginScreen.next(true); // ask the user to fill in his/her credentials
        } else {
          setTimeout(() => { // Inside a setTimeout because the first passage here just after subscription, "subscription" variable has not yet the time to be defined
            subscription.unsubscribe(); // in order to only subscribe to the first user thanks to https://stackoverflow.com/a/34839338/535203
            subscriber.next(user);
            subscriber.end();
          });
        }
      })
    });
  }
}
