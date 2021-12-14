import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SousRegneDetailComponent } from './sous-regne-detail.component';

describe('SousRegne Management Detail Component', () => {
  let comp: SousRegneDetailComponent;
  let fixture: ComponentFixture<SousRegneDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SousRegneDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sousRegne: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SousRegneDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SousRegneDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sousRegne on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sousRegne).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
