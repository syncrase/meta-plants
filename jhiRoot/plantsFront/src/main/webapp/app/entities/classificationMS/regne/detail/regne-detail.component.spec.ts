import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RegneDetailComponent } from './regne-detail.component';

describe('Regne Management Detail Component', () => {
  let comp: RegneDetailComponent;
  let fixture: ComponentFixture<RegneDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegneDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ regne: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RegneDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RegneDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load regne on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.regne).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
