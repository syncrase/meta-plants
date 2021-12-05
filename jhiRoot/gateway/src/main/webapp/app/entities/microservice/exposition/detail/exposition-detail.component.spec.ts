import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExpositionDetailComponent } from './exposition-detail.component';

describe('Exposition Management Detail Component', () => {
  let comp: ExpositionDetailComponent;
  let fixture: ComponentFixture<ExpositionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExpositionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ exposition: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ExpositionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ExpositionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load exposition on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.exposition).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
