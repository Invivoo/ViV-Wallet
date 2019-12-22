import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DxButtonModule } from 'devextreme-angular';

import { AdminMenuComponent } from './admin-menu.component';

describe('AdminMenuComponent', () => {
    let component: AdminMenuComponent;
    let fixture: ComponentFixture<AdminMenuComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                DxButtonModule,
            ],
            declarations: [AdminMenuComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AdminMenuComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
