import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { User } from './types';

@Injectable({
    providedIn: 'root'
})
export class UsersService {

    constructor() { }

    getUsers(): Observable<User[]> {
        return of([
            { id: '1', login: 'rleloup', name: 'Roch Leloup', email: 'roch.leloup@invivoo.com' },
            { id: '2', login: 'dboudet', name: 'Djeferson Boudet', email: 'djeverson.boudet@invivoo.com' },
            { id: '3', login: 'rflondin', name: 'Roger Flodin', email: 'roget.flondin@invivoo.com' },
        ]);
    }
}
