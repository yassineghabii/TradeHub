import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrdreachatComponent } from './ordreachat.component';

describe('OrdreachatComponent', () => {
  let component: OrdreachatComponent;
  let fixture: ComponentFixture<OrdreachatComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrdreachatComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrdreachatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
