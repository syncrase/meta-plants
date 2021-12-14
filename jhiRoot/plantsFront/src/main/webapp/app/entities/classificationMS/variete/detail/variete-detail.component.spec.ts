import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VarieteDetailComponent } from './variete-detail.component';

describe('Variete Management Detail Component', () => {
  let comp: VarieteDetailComponent;
  let fixture: ComponentFixture<VarieteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VarieteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ variete: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VarieteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VarieteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load variete on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.variete).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
