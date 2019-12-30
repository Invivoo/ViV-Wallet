import { Component, OnInit, ViewChild } from '@angular/core';
import { User } from '../types';
import { UsersService } from '../users.service';
import { DxDataGridComponent } from 'devextreme-angular';
import { Router } from '@angular/router';

@Component({
    selector: 'wallet-admin-manage-users',
    templateUrl: './admin-manage-users.component.html',
    styleUrls: ['./admin-manage-users.component.scss']
})
export class AdminManageUsersComponent implements OnInit {
    @ViewChild(DxDataGridComponent, { static: false }) dataGrid: DxDataGridComponent;

    users: User[] = [];

    constructor(
        private router: Router,
        private usersService: UsersService) {
    }

    ngOnInit() {

        this.usersService.getUsers()
            .subscribe(users => this.users = users);
    }

    logout() {
        this.router.navigate(['/']);
    }

    edit() {
        const selectedElement = this.dataGrid.instance.getSelectedRowsData()[0] as User;
        if (!selectedElement) {
            return;
        }
        this.router.navigate(['/users/' + selectedElement.id + '/edit']);
    }

    delete() {
        const selectedElement = this.dataGrid.instance.getSelectedRowsData()[0] as User;
        if (!selectedElement) {
            return;
        }
        const isDeleteConfirmed = confirm(`Are you sure you want to delete user ${selectedElement.login}?`);
        if (!isDeleteConfirmed) {
            return;
        }
        this.usersService.deleteUser(selectedElement)
            .subscribe(() => this.ngOnInit());
    }
}
