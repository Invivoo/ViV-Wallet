import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEditUserComponent } from './admin-edit-user.component';
import { DxButtonModule, DxTextBoxModule } from 'devextreme-angular';

describe('AdminEditUserComponent', () => {
    let component: AdminEditUserComponent;
    let fixture: ComponentFixture<AdminEditUserComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [AdminEditUserComponent],
            imports: [DxButtonModule, DxTextBoxModule]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AdminEditUserComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
