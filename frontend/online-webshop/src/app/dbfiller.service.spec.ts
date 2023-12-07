import { TestBed } from '@angular/core/testing';

import { DbfillerService } from './dbfiller.service';

describe('DbfillerService', () => {
  let service: DbfillerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DbfillerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
