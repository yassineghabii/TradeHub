import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjetIComponent } from './projet-i.component';

describe('ProjetIComponent', () => {
  let component: ProjetIComponent;
  let fixture: ComponentFixture<ProjetIComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProjetIComponent ]
    })
        .compileComponents();

    fixture = TestBed.createComponent(ProjetIComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
