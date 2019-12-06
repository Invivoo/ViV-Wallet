import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';

import { AdminEditUserComponent } from './admin-edit-user.component';
import { DxButtonModule, DxTextBoxModule, DxValidatorModule } from 'devextreme-angular';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';

const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

describe('AdminEditUserComponent', () => {
    let component: AdminEditUserComponent;
    let fixture: ComponentFixture<AdminEditUserComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [AdminEditUserComponent],
            imports: [
                DxButtonModule,
                DxTextBoxModule,
                DxValidatorModule,
                HttpClientTestingModule
            ],
            providers: [
                { provide: Router, useValue: routerSpy },
                { provide: ActivatedRoute, useValue: { snapshot: { params: { id: '123' } } } }
            ]
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
