import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SuperDivisionDetailComponent } from './super-division-detail.component';

describe('SuperDivision Management Detail Component', () => {
  let comp: SuperDivisionDetailComponent;
  let fixture: ComponentFixture<SuperDivisionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SuperDivisionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ superDivision: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SuperDivisionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SuperDivisionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load superDivision on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.superDivision).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
