import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminManageUsersComponent } from './admin-manage-users.component';
import { DxButtonModule, DxTextBoxModule, DxValidatorModule, DxDataGridModule } from 'devextreme-angular';
import { AppRoutingModule } from '../app-routing.module';
import { BrowserModule } from '@angular/platform-browser';
import { AdminEditUserComponent } from '../admin-edit-user/admin-edit-user.component';
import { AdminMenuComponent } from '../admin-menu/admin-menu.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('AdminManageUsersComponent', () => {
    let component: AdminManageUsersComponent;
    let fixture: ComponentFixture<AdminManageUsersComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                DxButtonModule,
                DxTextBoxModule,
                DxValidatorModule,
                DxDataGridModule,
                AppRoutingModule,
                BrowserModule,
                HttpClientTestingModule,
            ],
            declarations: [AdminManageUsersComponent, AdminEditUserComponent, AdminMenuComponent],
            providers: [AdminEditUserComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AdminManageUsersComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
