import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InfraEmbranchementDetailComponent } from './infra-embranchement-detail.component';

describe('InfraEmbranchement Management Detail Component', () => {
  let comp: InfraEmbranchementDetailComponent;
  let fixture: ComponentFixture<InfraEmbranchementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InfraEmbranchementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ infraEmbranchement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InfraEmbranchementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InfraEmbranchementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load infraEmbranchement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.infraEmbranchement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
