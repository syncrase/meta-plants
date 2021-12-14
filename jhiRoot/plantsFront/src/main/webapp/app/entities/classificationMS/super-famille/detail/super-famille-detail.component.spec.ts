import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SuperFamilleDetailComponent } from './super-famille-detail.component';

describe('SuperFamille Management Detail Component', () => {
  let comp: SuperFamilleDetailComponent;
  let fixture: ComponentFixture<SuperFamilleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SuperFamilleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ superFamille: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SuperFamilleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SuperFamilleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load superFamille on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.superFamille).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
