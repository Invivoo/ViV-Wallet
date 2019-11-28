import { Component, OnInit, ViewChild } from '@angular/core';
import { User, Role } from '../types';
import { LoginService } from '../login.service';
import { UsersService } from '../users.service';
import { DxDataGridComponent } from 'devextreme-angular';

@Component({
    selector: 'wallet-admin-manage-users',
    templateUrl: './admin-manage-users.component.html',
    styleUrls: ['./admin-manage-users.component.scss']
})
export class AdminManageUsersComponent implements OnInit {
    @ViewChild(DxDataGridComponent, { static: false }) dataGrid: DxDataGridComponent;

    user: User =
        { id: '7', login: 'tmontgomery', fullname: 'MONTGOMERY Théophile', email: 'theophile.montgomery@invivoo.com' };

    users: User[] = [];

    currentRoleName: string;

    constructor(private loginService: LoginService, private usersService: UsersService) { }

    ngOnInit() {
        switch (this.loginService.getCurrentRole()) {
            case Role.Admin:
                this.currentRoleName = 'System Administrator';
                break;
            default:
                this.currentRoleName = 'UNKNOWN';
                break;
        }

        this.usersService.getUsers().subscribe(users => this.users = users);
    }

    logout() {
        window.location.href = '/';
    }

    add() {

    }

    edit() {
        const selectedElement = this.dataGrid.instance.getSelectedRowsData()[0] as User;
        if (selectedElement) {
            window.location.href = '/users/' + selectedElement.id + '/edit';
        }
    }

    delete() {
        const selectedElement = this.dataGrid.instance.getSelectedRowsData()[0] as User;
        if (selectedElement) {
            confirm(`Are you sure you want to delete user ${selectedElement.login}?`);
        }
    }
}
