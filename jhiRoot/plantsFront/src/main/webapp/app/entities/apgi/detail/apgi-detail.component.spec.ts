import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { APGIDetailComponent } from './apgi-detail.component';

describe('APGI Management Detail Component', () => {
  let comp: APGIDetailComponent;
  let fixture: ComponentFixture<APGIDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [APGIDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ aPGI: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(APGIDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(APGIDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aPGI on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.aPGI).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
