import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CronquistDetailComponent } from './cronquist-detail.component';

describe('Cronquist Management Detail Component', () => {
  let comp: CronquistDetailComponent;
  let fixture: ComponentFixture<CronquistDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CronquistDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cronquist: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CronquistDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CronquistDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cronquist on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cronquist).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
