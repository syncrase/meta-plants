import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClassificationNomDetailComponent } from './classification-nom-detail.component';

describe('ClassificationNom Management Detail Component', () => {
  let comp: ClassificationNomDetailComponent;
  let fixture: ComponentFixture<ClassificationNomDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClassificationNomDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ classificationNom: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ClassificationNomDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ClassificationNomDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load classificationNom on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.classificationNom).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
