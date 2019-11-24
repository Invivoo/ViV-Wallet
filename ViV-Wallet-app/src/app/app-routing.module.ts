import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdminEditUserComponent } from './admin-edit-user/admin-edit-user.component';
import { AdminManageUsersComponent } from './admin-manage-users/admin-manage-users.component';

const routes: Routes = [
    { path: 'users', component: AdminManageUsersComponent },
    { path: 'users/add', component: AdminEditUserComponent },
    { path: 'users/:id/edit', component: AdminEditUserComponent },
];

@NgModule({
    imports: [RouterModule.forRoot(
        routes,
        // { enableTracing: true } // <-- debugging purposes only
    )],
    exports: [RouterModule]
})
export class AppRoutingModule { }
