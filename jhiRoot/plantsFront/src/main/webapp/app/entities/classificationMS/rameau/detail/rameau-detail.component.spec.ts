import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RameauDetailComponent } from './rameau-detail.component';

describe('Rameau Management Detail Component', () => {
  let comp: RameauDetailComponent;
  let fixture: ComponentFixture<RameauDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RameauDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rameau: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RameauDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RameauDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rameau on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rameau).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
