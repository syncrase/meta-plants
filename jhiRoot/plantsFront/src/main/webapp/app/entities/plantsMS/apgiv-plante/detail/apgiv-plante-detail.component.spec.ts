import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { APGIVPlanteDetailComponent } from './apgiv-plante-detail.component';

describe('APGIVPlante Management Detail Component', () => {
  let comp: APGIVPlanteDetailComponent;
  let fixture: ComponentFixture<APGIVPlanteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [APGIVPlanteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ aPGIVPlante: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(APGIVPlanteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(APGIVPlanteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aPGIVPlante on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.aPGIVPlante).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
