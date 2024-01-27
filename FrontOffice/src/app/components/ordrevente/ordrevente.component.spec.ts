import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrdreventeComponent } from './ordrevente.component';

describe('OrdreventeComponent', () => {
  let component: OrdreventeComponent;
  let fixture: ComponentFixture<OrdreventeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrdreventeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrdreventeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
