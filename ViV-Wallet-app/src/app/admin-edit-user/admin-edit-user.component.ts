import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from '../types';

@Component({
    selector: 'wallet-admin-edit-user',
    templateUrl: './admin-edit-user.component.html',
    styleUrls: ['./admin-edit-user.component.scss']
})
export class AdminEditUserComponent implements OnInit {

    user: User = {
        id: '7',
        login: 'rleloup',
        name: "Roch Leloup",
        email: "roch.leloup@invivoo.com"
    }

    constructor(
        private route: ActivatedRoute
    ) {
        this.user.login = route.snapshot.params.login;
    }

    ngOnInit() {
    }

    confirm() {
        alert('Hello world!');
        window.location.href = "/users";
    }
}
