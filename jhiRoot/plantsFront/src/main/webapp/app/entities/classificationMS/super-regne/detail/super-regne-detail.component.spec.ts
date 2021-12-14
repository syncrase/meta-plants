import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SuperRegneDetailComponent } from './super-regne-detail.component';

describe('SuperRegne Management Detail Component', () => {
  let comp: SuperRegneDetailComponent;
  let fixture: ComponentFixture<SuperRegneDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SuperRegneDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ superRegne: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SuperRegneDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SuperRegneDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load superRegne on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.superRegne).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
