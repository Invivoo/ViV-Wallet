import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from '../types';
import { UsersService } from '../users.service';

@Component({
    selector: 'wallet-admin-edit-user',
    templateUrl: './admin-edit-user.component.html',
    styleUrls: ['./admin-edit-user.component.scss']
})
export class AdminEditUserComponent implements OnInit {

    user: User = {
        id: '7',
        login: 'rleloup',
        fullname: 'Roch Leloup',
        email: 'roch.leloup@invivoo.com'
    };

    constructor(
        private route: ActivatedRoute,
        private usersService: UsersService
    ) {
        this.user.id = route.snapshot.params.id;
    }

    ngOnInit() {
        this.usersService.getUser(this.user.id).subscribe(u => this.user = u);
    }

    confirm() {
        this.usersService.saveUser(this.user).subscribe(() => window.location.href = '/users';);
    }
}
