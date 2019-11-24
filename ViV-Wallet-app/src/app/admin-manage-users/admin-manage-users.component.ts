import { Component, OnInit } from '@angular/core';
import { User } from '../types';

@Component({
    selector: 'wallet-admin-manage-users',
    templateUrl: './admin-manage-users.component.html',
    styleUrls: ['./admin-manage-users.component.scss']
})
export class AdminManageUsersComponent implements OnInit {
    users: User[] = [
        { login: 'rleloup', name: "Roch Leloup", email: "roch.leloup@invivoo.com" },
        { login: 'dboudet', name: "Djeferson Boudet", email: "djeverson.boudet@invivoo.com" },
        { login: 'rflondin', name: "Roger Flodin", email: "roget.flondin@invivoo.com" },
    ];


    constructor() { }

    ngOnInit() {
    }

}
