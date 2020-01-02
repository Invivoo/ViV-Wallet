import { Injectable } from '@angular/core';
import {
    HttpEvent, HttpInterceptor, HttpHandler, HttpRequest
} from '@angular/common/http';

import { Observable } from 'rxjs';
import { LoginService } from './login.service';

@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {

    constructor(private login: LoginService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler):
        Observable<HttpEvent<any>> {
        const authReq = req.clone({
            setHeaders: { 'Authorization': `Bearer ${this.login.getJwtToken()}` }
        });
        return next.handle(authReq);
    }
}
