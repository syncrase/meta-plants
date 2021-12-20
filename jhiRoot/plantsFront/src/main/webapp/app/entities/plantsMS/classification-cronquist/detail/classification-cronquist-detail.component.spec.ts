import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClassificationCronquistDetailComponent } from './classification-cronquist-detail.component';

describe('ClassificationCronquist Management Detail Component', () => {
  let comp: ClassificationCronquistDetailComponent;
  let fixture: ComponentFixture<ClassificationCronquistDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClassificationCronquistDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ classificationCronquist: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ClassificationCronquistDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ClassificationCronquistDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load classificationCronquist on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.classificationCronquist).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
