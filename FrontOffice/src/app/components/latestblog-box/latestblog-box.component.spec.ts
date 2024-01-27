import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LatestblogBoxComponent } from './latestblog-box.component';

describe('LatestblogBoxComponent', () => {
  let component: LatestblogBoxComponent;
  let fixture: ComponentFixture<LatestblogBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LatestblogBoxComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LatestblogBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
