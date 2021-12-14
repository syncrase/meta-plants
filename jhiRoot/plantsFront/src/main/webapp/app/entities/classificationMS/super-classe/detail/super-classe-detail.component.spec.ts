import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SuperClasseDetailComponent } from './super-classe-detail.component';

describe('SuperClasse Management Detail Component', () => {
  let comp: SuperClasseDetailComponent;
  let fixture: ComponentFixture<SuperClasseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SuperClasseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ superClasse: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SuperClasseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SuperClasseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load superClasse on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.superClasse).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
