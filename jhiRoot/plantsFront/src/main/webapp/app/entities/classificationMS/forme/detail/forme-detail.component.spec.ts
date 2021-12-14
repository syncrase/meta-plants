import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormeDetailComponent } from './forme-detail.component';

describe('Forme Management Detail Component', () => {
  let comp: FormeDetailComponent;
  let fixture: ComponentFixture<FormeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ forme: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FormeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FormeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load forme on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.forme).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
