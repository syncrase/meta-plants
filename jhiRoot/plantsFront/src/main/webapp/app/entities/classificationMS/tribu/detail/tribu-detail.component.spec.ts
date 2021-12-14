import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TribuDetailComponent } from './tribu-detail.component';

describe('Tribu Management Detail Component', () => {
  let comp: TribuDetailComponent;
  let fixture: ComponentFixture<TribuDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TribuDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tribu: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TribuDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TribuDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tribu on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tribu).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
