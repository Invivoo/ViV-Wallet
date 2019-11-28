import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Router} from '@angular/router';
import {User} from '../types';
import {UsersService} from '../users.service';
import {isNullOrUndefined} from 'util';

@Component({
             selector: 'wallet-admin-edit-user',
             templateUrl: './admin-edit-user.component.html',
             styleUrls: ['./admin-edit-user.component.scss']
           })
export class AdminEditUserComponent implements OnInit {

  user: User;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private usersService: UsersService
  ) {
    const id = route.snapshot.params.id;
    this.initUser(id);
  }

  ngOnInit() {
  }

  confirm() {
    this.usersService.saveUser(this.user)
        .subscribe(() => this.router.navigate(['/users']));
  }

  private initUser(id) {
    if (isNullOrUndefined(id)) {
      this.user = new User();
    } else {
      this.usersService.getUser(id)
          .subscribe(u => this.user = u);
    }
  }
}
