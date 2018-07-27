// inspired by https://medium.com/@ryanchenkie_40935/angular-authentication-using-the-http-client-and-http-interceptors-2f9d1540eb8

import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { UserService } from './user.service';
import { Observable } from 'rxjs/Observable';
import { catchError, switchMap } from 'rxjs/operators';
import { Injectable, Injector } from '@angular/core';

@Injectable()
export class UnauthorizedInterceptor implements HttpInterceptor {
  // unauthorizedRequest: Array<HttpRequest<any>> = [];
  private userService: UserService;

  constructor(private injector: Injector) {
  }

  private getUserService(): UserService {
    if (!this.userService) {
      this.userService = this.injector.get(UserService); // lazy loading userService to avoid a cyclic dependency error thanks to https://github.com/angular/angular/issues/18224#issuecomment-316957213
    }
    return this.userService;
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError(response => {
        if (response instanceof HttpErrorResponse && response.status === 401) { //FIXME this system always ask for a first request blindly before retrying with the user login
          return this.getUserService()
            .waitForUserDisplayingLoginScreenIfNecessary()
            .pipe(
              switchMap(user => {
                const retryReq = req.clone({setHeaders: {
                  Authorization: `Basic ${btoa(`${user.login}:${user.password}`)}`
                }});
                // retryReq.headers.append('Authorization', `Basic ${btoa(`${user.login}:${user.password}`)}`);
                return next.handle(retryReq); // FIXME in case of wrong login / password, another 401 will be issued and not catched, the login screen will never popup again and all next request will fail in 401
              })
            );
        } else {
          Observable.throw(response);
        }
      })
    )
  }


}
