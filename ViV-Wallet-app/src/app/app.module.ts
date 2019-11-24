import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DxButtonModule, DxTextBoxModule } from 'devextreme-angular';
import { AdminEditUserComponent } from './admin-edit-user/admin-edit-user.component';
import { AdminManageUsersComponent } from './admin-manage-users/admin-manage-users.component';

@NgModule({
    declarations: [
        AppComponent,
        AdminEditUserComponent,
        AdminManageUsersComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        DxButtonModule,
        DxTextBoxModule,
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule { }
