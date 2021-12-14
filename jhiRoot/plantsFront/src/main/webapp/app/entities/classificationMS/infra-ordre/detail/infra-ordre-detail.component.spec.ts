import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InfraOrdreDetailComponent } from './infra-ordre-detail.component';

describe('InfraOrdre Management Detail Component', () => {
  let comp: InfraOrdreDetailComponent;
  let fixture: ComponentFixture<InfraOrdreDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InfraOrdreDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ infraOrdre: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InfraOrdreDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InfraOrdreDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load infraOrdre on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.infraOrdre).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
