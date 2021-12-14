import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SuperOrdreDetailComponent } from './super-ordre-detail.component';

describe('SuperOrdre Management Detail Component', () => {
  let comp: SuperOrdreDetailComponent;
  let fixture: ComponentFixture<SuperOrdreDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SuperOrdreDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ superOrdre: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SuperOrdreDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SuperOrdreDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load superOrdre on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.superOrdre).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
