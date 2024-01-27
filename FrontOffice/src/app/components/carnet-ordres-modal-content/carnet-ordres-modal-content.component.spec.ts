import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarnetOrdresModalContentComponent } from './carnet-ordres-modal-content.component';

describe('CarnetOrdresModalContentComponent', () => {
  let component: CarnetOrdresModalContentComponent;
  let fixture: ComponentFixture<CarnetOrdresModalContentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CarnetOrdresModalContentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarnetOrdresModalContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
