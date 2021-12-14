import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SousFormeDetailComponent } from './sous-forme-detail.component';

describe('SousForme Management Detail Component', () => {
  let comp: SousFormeDetailComponent;
  let fixture: ComponentFixture<SousFormeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SousFormeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sousForme: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SousFormeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SousFormeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sousForme on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sousForme).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
