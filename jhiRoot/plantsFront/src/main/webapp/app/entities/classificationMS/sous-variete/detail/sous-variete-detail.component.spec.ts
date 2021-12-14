import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SousVarieteDetailComponent } from './sous-variete-detail.component';

describe('SousVariete Management Detail Component', () => {
  let comp: SousVarieteDetailComponent;
  let fixture: ComponentFixture<SousVarieteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SousVarieteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sousVariete: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SousVarieteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SousVarieteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sousVariete on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sousVariete).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
