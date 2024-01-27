import { TestBed } from '@angular/core/testing';

import { ProjetIService } from './projet-i.service';

describe('ProjetIService', () => {
  let service: ProjetIService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProjetIService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
