import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { APGIIDetailComponent } from './apgii-detail.component';

describe('APGII Management Detail Component', () => {
  let comp: APGIIDetailComponent;
  let fixture: ComponentFixture<APGIIDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [APGIIDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ aPGII: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(APGIIDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(APGIIDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aPGII on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.aPGII).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
