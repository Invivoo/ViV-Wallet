import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEditUserComponent } from './admin-edit-user.component';

describe('AdminEditUserComponent', () => {
  let component: AdminEditUserComponent;
  let fixture: ComponentFixture<AdminEditUserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminEditUserComponent ]
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
