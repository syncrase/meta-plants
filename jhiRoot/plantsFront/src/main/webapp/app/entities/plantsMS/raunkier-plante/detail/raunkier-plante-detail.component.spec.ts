import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RaunkierPlanteDetailComponent } from './raunkier-plante-detail.component';

describe('RaunkierPlante Management Detail Component', () => {
  let comp: RaunkierPlanteDetailComponent;
  let fixture: ComponentFixture<RaunkierPlanteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RaunkierPlanteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ raunkierPlante: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RaunkierPlanteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RaunkierPlanteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load raunkierPlante on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.raunkierPlante).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
