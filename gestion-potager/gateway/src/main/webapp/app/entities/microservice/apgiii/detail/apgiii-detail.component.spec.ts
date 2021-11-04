import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { APGIIIDetailComponent } from './apgiii-detail.component';

describe('APGIII Management Detail Component', () => {
  let comp: APGIIIDetailComponent;
  let fixture: ComponentFixture<APGIIIDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [APGIIIDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ aPGIII: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(APGIIIDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(APGIIIDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aPGIII on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.aPGIII).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
