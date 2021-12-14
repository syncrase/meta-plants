import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CronquistPlanteDetailComponent } from './cronquist-plante-detail.component';

describe('CronquistPlante Management Detail Component', () => {
  let comp: CronquistPlanteDetailComponent;
  let fixture: ComponentFixture<CronquistPlanteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CronquistPlanteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cronquistPlante: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CronquistPlanteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CronquistPlanteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cronquistPlante on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cronquistPlante).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
