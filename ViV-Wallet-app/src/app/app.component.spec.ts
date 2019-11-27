import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { AdminEditUserComponent } from './admin-edit-user/admin-edit-user.component';
import { DxButtonModule, DxTextBoxModule } from 'devextreme-angular';

describe('AppComponent', () => {
    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                RouterTestingModule,
                DxButtonModule,
                DxTextBoxModule
            ],
            declarations: [
                AppComponent,
                AdminEditUserComponent
            ],
        }).compileComponents();
    }));

    it('should create the app', () => {
        const fixture = TestBed.createComponent(AppComponent);
        const app = fixture.debugElement.componentInstance;
        expect(app).toBeTruthy();
    });
});
