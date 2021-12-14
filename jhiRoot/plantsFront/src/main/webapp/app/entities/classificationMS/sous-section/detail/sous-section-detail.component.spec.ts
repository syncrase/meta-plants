import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SousSectionDetailComponent } from './sous-section-detail.component';

describe('SousSection Management Detail Component', () => {
  let comp: SousSectionDetailComponent;
  let fixture: ComponentFixture<SousSectionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SousSectionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sousSection: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SousSectionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SousSectionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sousSection on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sousSection).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
