import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EspeceDetailComponent } from './espece-detail.component';

describe('Espece Management Detail Component', () => {
  let comp: EspeceDetailComponent;
  let fixture: ComponentFixture<EspeceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EspeceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ espece: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EspeceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EspeceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load espece on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.espece).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
