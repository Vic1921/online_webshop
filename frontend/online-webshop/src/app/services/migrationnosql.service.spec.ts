import { TestBed } from '@angular/core/testing';

import { MigrationNoSQLService } from './migrationnosql.service';

describe('MigrationNoSQLService', () => {
  let service: MigrationNoSQLService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MigrationNoSQLService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
