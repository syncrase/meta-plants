import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SousEspeceDetailComponent } from './sous-espece-detail.component';

describe('SousEspece Management Detail Component', () => {
  let comp: SousEspeceDetailComponent;
  let fixture: ComponentFixture<SousEspeceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SousEspeceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sousEspece: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SousEspeceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SousEspeceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sousEspece on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sousEspece).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
