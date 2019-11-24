import { Component, OnInit, ViewChild } from '@angular/core';
import { User, Role } from '../types';
import { LoginService } from '../login.service';
import { DxDataGridComponent } from 'devextreme-angular';

@Component({
    selector: 'wallet-admin-manage-users',
    templateUrl: './admin-manage-users.component.html',
    styleUrls: ['./admin-manage-users.component.scss']
})
export class AdminManageUsersComponent implements OnInit {
    @ViewChild(DxDataGridComponent, { static: false }) dataGrid: DxDataGridComponent;

    user: User =
        { id: '7', login: 'tmontgomery', name: 'MONTGOMERY Théophile', email: 'theophile.montgomery@invivoo.com' };

    users: User[] = [
        { id: '1', login: 'rleloup', name: 'Roch Leloup', email: 'roch.leloup@invivoo.com' },
        { id: '2', login: 'dboudet', name: 'Djeferson Boudet', email: 'djeverson.boudet@invivoo.com' },
        { id: '3', login: 'rflondin', name: 'Roger Flodin', email: 'roget.flondin@invivoo.com' },
    ];

    currentRoleName: string;

    constructor(private loginService: LoginService) { }

    ngOnInit() {
        switch (this.loginService.getCurrentRole()) {
            case Role.Admin:
                this.currentRoleName = 'System Administrator';
                break;
            default:
                this.currentRoleName = 'UNKNOWN';
                break;
        }
    }

    logout() {
        window.location.href = '/';
    }

    add() {

    }

    delete() {
        const selectedElement = this.dataGrid.instance.getSelectedRowsData()[0] as User;
        if (selectedElement) {
            confirm(`Are you sure you want to delete user ${selectedElement.login}?`);
        }
    }
}
