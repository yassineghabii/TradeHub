import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangePasswordModalComponentComponent } from './change-password-modal-component.component';

describe('ChangePasswordModalComponentComponent', () => {
  let component: ChangePasswordModalComponentComponent;
  let fixture: ComponentFixture<ChangePasswordModalComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChangePasswordModalComponentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChangePasswordModalComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
