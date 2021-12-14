import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { APGIIPlanteDetailComponent } from './apgii-plante-detail.component';

describe('APGIIPlante Management Detail Component', () => {
  let comp: APGIIPlanteDetailComponent;
  let fixture: ComponentFixture<APGIIPlanteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [APGIIPlanteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ aPGIIPlante: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(APGIIPlanteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(APGIIPlanteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aPGIIPlante on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.aPGIIPlante).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
