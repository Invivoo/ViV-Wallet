import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DxButtonModule, DxTextBoxModule, DxValidatorModule, DxDataGridModule } from 'devextreme-angular';
import { AdminEditUserComponent } from './admin-edit-user/admin-edit-user.component';
import { AdminManageUsersComponent } from './admin-manage-users/admin-manage-users.component';

import { AuthenticationInterceptor } from './http-interceptor';
import { AdminMenuComponent } from './admin-menu/admin-menu.component';
import { BannerComponent } from './banner/banner.component';

@NgModule({
    declarations: [
        AppComponent,
        AdminEditUserComponent,
        AdminManageUsersComponent,
        AdminMenuComponent,
        BannerComponent
    ],
    imports: [
        HttpClientModule,
        BrowserModule,
        AppRoutingModule,
        DxButtonModule,
        DxTextBoxModule,
        DxValidatorModule,
        DxDataGridModule
    ],
    providers: [
        { provide: HTTP_INTERCEPTORS, useClass: AuthenticationInterceptor, multi: true },
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
