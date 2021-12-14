import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { APGIPlanteDetailComponent } from './apgi-plante-detail.component';

describe('APGIPlante Management Detail Component', () => {
  let comp: APGIPlanteDetailComponent;
  let fixture: ComponentFixture<APGIPlanteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [APGIPlanteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ aPGIPlante: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(APGIPlanteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(APGIPlanteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aPGIPlante on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.aPGIPlante).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
