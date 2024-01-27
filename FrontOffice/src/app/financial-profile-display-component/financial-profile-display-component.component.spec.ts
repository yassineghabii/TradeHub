import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinancialProfileDisplayComponentComponent } from './financial-profile-display-component.component';

describe('FinancialProfileDisplayComponentComponent', () => {
  let component: FinancialProfileDisplayComponentComponent;
  let fixture: ComponentFixture<FinancialProfileDisplayComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FinancialProfileDisplayComponentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FinancialProfileDisplayComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
