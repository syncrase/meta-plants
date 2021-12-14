import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CladeDetailComponent } from './clade-detail.component';

describe('Clade Management Detail Component', () => {
  let comp: CladeDetailComponent;
  let fixture: ComponentFixture<CladeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CladeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ clade: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CladeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CladeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load clade on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.clade).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
