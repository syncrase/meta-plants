import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SousOrdreDetailComponent } from './sous-ordre-detail.component';

describe('SousOrdre Management Detail Component', () => {
  let comp: SousOrdreDetailComponent;
  let fixture: ComponentFixture<SousOrdreDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SousOrdreDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sousOrdre: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SousOrdreDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SousOrdreDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sousOrdre on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sousOrdre).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
