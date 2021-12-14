import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MicroOrdreDetailComponent } from './micro-ordre-detail.component';

describe('MicroOrdre Management Detail Component', () => {
  let comp: MicroOrdreDetailComponent;
  let fixture: ComponentFixture<MicroOrdreDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MicroOrdreDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ microOrdre: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MicroOrdreDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MicroOrdreDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load microOrdre on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.microOrdre).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
