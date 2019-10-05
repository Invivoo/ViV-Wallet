import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'admin-edit-user',
    templateUrl: './admin-edit-user.component.html',
    styleUrls: ['./admin-edit-user.component.scss']
})
export class AdminEditUserComponent implements OnInit {

    constructor() { }

    ngOnInit() {
    }

    helloWorld() {
        alert('Hello world!');
    }
}
