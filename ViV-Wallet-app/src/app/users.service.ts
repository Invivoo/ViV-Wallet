import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {User} from './types';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {isNullOrUndefined} from 'util';
import {LoginService} from './login.service';
import {environment} from '../environments/environment';

const usersEndpoint = environment.vivWallet.api.url + '/v1/users';

@Injectable({
              providedIn: 'root'
            })
export class UsersService {

  constructor(private http: HttpClient, private login: LoginService) {
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(usersEndpoint);
  }

  saveUser(user: User): Observable<Object> {
    if (isNullOrUndefined(user.id)) {
      return this.http.post(usersEndpoint,
                            user,
                            {
                              headers: new HttpHeaders(
                                {
                                  'Authorization': this.login.getJwtToken()
                                })
                            });
    }
    return this.http.put(usersEndpoint + '/' + user.id,
                         user,
                         {
                           headers: new HttpHeaders(
                             {
                               'Authorization': this.login.getJwtToken()
                             })
                         });
  }

  getUser(id: string): Observable<User> {
    return this.http.get<User>(usersEndpoint + '/' + id);
  }

  deleteUser(user: User): Observable<any> {
    return this.http.delete(usersEndpoint + '/' + user.id);
  }
}
