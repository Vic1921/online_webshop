import { TestBed } from '@angular/core/testing';

import { EventEmitterService } from './eventemitter.service';

describe('EventemitterService', () => {
  let service: EventEmitterService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EventEmitterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
