import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LatestblogComponent } from './forget.component';

describe('LatestblogComponent', () => {
  let component: LatestblogComponent;
  let fixture: ComponentFixture<LatestblogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LatestblogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LatestblogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
