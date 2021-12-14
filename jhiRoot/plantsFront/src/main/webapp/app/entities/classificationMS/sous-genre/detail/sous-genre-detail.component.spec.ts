import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SousGenreDetailComponent } from './sous-genre-detail.component';

describe('SousGenre Management Detail Component', () => {
  let comp: SousGenreDetailComponent;
  let fixture: ComponentFixture<SousGenreDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SousGenreDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sousGenre: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SousGenreDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SousGenreDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sousGenre on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sousGenre).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
