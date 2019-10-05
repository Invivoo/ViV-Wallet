import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DxButtonModule } from 'devextreme-angular';
import { AdminEditUserComponent } from './admin-edit-user/admin-edit-user.component';

@NgModule({
    declarations: [
        AppComponent,
        AdminEditUserComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        DxButtonModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule { }
