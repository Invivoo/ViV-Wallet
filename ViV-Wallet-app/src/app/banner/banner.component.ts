import { Component, OnInit } from '@angular/core';
import { Role, User } from '../types';
import { LoginService } from '../login.service';

@Component({
    selector: 'wallet-banner',
    templateUrl: './banner.component.html',
    styleUrls: ['./banner.component.scss']
})
export class BannerComponent implements OnInit {

    user: User =
        { id: '7', login: 'tmontgomery', fullname: 'MONTGOMERY Théophile', email: 'theophile.montgomery@invivoo.com' };

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

}
