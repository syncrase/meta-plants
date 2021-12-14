import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SousDivisionDetailComponent } from './sous-division-detail.component';

describe('SousDivision Management Detail Component', () => {
  let comp: SousDivisionDetailComponent;
  let fixture: ComponentFixture<SousDivisionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SousDivisionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sousDivision: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SousDivisionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SousDivisionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sousDivision on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sousDivision).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
