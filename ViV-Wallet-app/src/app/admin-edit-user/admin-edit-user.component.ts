import { Component, OnInit } from '@angular/core';
import { User } from '../types';

@Component({
    selector: 'wallet-admin-edit-user',
    templateUrl: './admin-edit-user.component.html',
    styleUrls: ['./admin-edit-user.component.scss']
})
export class AdminEditUserComponent implements OnInit {

    user: User = {
        login: 'rleloup',
        name: "Roch Leloup",
        email: "roch.leloup@invivoo.com"
    }

    constructor() { }

    ngOnInit() {
    }

    helloWorld() {
        alert('Hello world!');
    }
}
