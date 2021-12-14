import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InfraClasseDetailComponent } from './infra-classe-detail.component';

describe('InfraClasse Management Detail Component', () => {
  let comp: InfraClasseDetailComponent;
  let fixture: ComponentFixture<InfraClasseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InfraClasseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ infraClasse: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InfraClasseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InfraClasseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load infraClasse on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.infraClasse).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
