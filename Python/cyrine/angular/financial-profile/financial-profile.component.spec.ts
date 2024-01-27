import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinancialProfileComponent } from './financial-profile.component';

describe('FinancialProfileComponent', () => {
  let component: FinancialProfileComponent;
  let fixture: ComponentFixture<FinancialProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FinancialProfileComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FinancialProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
