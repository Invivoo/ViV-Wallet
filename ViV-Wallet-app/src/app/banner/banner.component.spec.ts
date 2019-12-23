import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BannerComponent } from './banner.component';
import { LoginService } from '../login.service';
import { Role } from '../types';

const loginSpy = jasmine.createSpyObj('LoginService', {
    getCurrentRole: Role.Admin
});

describe('BannerComponent', () => {
    let component: BannerComponent;
    let fixture: ComponentFixture<BannerComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [BannerComponent],
            providers: [
                { provide: LoginService, useValue: loginSpy }
            ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(BannerComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('displays current user role', () => {
        expect(fixture.nativeElement.textContent).toContain('System Administrator');
    });
});
