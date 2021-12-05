import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { APGIVDetailComponent } from './apgiv-detail.component';

describe('APGIV Management Detail Component', () => {
  let comp: APGIVDetailComponent;
  let fixture: ComponentFixture<APGIVDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [APGIVDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ aPGIV: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(APGIVDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(APGIVDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aPGIV on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.aPGIV).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
