import { Injectable } from '@angular/core';
import { Role } from './types';
import { Observable, of } from 'rxjs';

@Injectable({
    providedIn: 'root'
})

export class LoginService {

    private role: Role | undefined;

    constructor() {
        this.role = Role.Admin;
    }

    getCurrentRole(): Role | undefined {
        return this.role;
    }

    logout() {

    }
}
