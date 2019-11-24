import { Component, OnInit } from '@angular/core';
import { User } from '../types';

@Component({
    selector: 'wallet-admin-manage-users',
    templateUrl: './admin-manage-users.component.html',
    styleUrls: ['./admin-manage-users.component.scss']
})
export class AdminManageUsersComponent implements OnInit {

    user: User =
        { id: '7', login: 'tmontgomery', name: "MONTGOMERY Théophile", email: "theophile.montgomery@invivoo.com" };

    users: User[] = [
        { id: '1', login: 'rleloup', name: "Roch Leloup", email: "roch.leloup@invivoo.com" },
        { id: '2', login: 'dboudet', name: "Djeferson Boudet", email: "djeverson.boudet@invivoo.com" },
        { id: '3', login: 'rflondin', name: "Roger Flodin", email: "roget.flondin@invivoo.com" },
    ];


    constructor() { }

    ngOnInit() {
    }

}
