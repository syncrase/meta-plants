import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SousClasseDetailComponent } from './sous-classe-detail.component';

describe('SousClasse Management Detail Component', () => {
  let comp: SousClasseDetailComponent;
  let fixture: ComponentFixture<SousClasseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SousClasseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sousClasse: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SousClasseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SousClasseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sousClasse on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sousClasse).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
