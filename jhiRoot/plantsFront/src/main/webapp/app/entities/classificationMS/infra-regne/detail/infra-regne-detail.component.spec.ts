import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InfraRegneDetailComponent } from './infra-regne-detail.component';

describe('InfraRegne Management Detail Component', () => {
  let comp: InfraRegneDetailComponent;
  let fixture: ComponentFixture<InfraRegneDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InfraRegneDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ infraRegne: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InfraRegneDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InfraRegneDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load infraRegne on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.infraRegne).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
