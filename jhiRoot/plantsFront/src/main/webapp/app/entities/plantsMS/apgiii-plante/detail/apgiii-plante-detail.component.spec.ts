import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { APGIIIPlanteDetailComponent } from './apgiii-plante-detail.component';

describe('APGIIIPlante Management Detail Component', () => {
  let comp: APGIIIPlanteDetailComponent;
  let fixture: ComponentFixture<APGIIIPlanteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [APGIIIPlanteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ aPGIIIPlante: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(APGIIIPlanteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(APGIIIPlanteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aPGIIIPlante on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.aPGIIIPlante).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
