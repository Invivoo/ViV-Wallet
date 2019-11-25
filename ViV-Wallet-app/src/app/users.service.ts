import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { User } from './types';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class UsersService {

    constructor(private http: HttpClient) { }

    getUsers(): Observable<User[]> {
        return this.http.get<User[]>('http://localhost:3000/api/v1/users');
    }
}
