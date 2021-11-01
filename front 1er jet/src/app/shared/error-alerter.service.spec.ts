import { TestBed } from '@angular/core/testing';

import { ErrorAlerterService } from '../ngoverride/error-alerter.service';

describe('ErrorAlerterService', () => {
  let service: ErrorAlerterService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ErrorAlerterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
