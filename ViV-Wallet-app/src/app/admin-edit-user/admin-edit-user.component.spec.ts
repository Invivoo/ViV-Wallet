import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';

import { AdminEditUserComponent } from './admin-edit-user.component';
import { DxButtonModule, DxTextBoxModule, DxValidatorModule } from 'devextreme-angular';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { UsersService } from '../users.service';
import { of } from 'rxjs';
import { User } from '../types';

const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
const saveUserSpy = jasmine.createSpyObj(['subscribe']);
const usersSpy = jasmine.createSpyObj('UsersService', {
    saveUser: saveUserSpy,
    getUser: jasmine.createSpyObj({ subscribe: of(new User()) })
});

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
                { provide: UsersService, useValue: usersSpy },
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

    it('should not save when form is not valid', () => {
        let event = {
            validationGroup: {
                validate: () => { return { isValid: false }; }
            },
        };

        component.confirm(event);
        expect(saveUserSpy.subscribe).not.toHaveBeenCalled();
    });

    it('should  save when form is  valid', () => {
        let event = {
            validationGroup: {
                validate: () => { return { isValid: true }; }
            },
        };

        component.confirm(event);
        expect(saveUserSpy.subscribe).toHaveBeenCalled();
    });


});
