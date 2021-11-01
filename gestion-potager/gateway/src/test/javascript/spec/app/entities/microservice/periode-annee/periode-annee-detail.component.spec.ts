import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { PeriodeAnneeDetailComponent } from 'app/entities/microservice/periode-annee/periode-annee-detail.component';
import { PeriodeAnnee } from 'app/shared/model/microservice/periode-annee.model';

describe('Component Tests', () => {
  describe('PeriodeAnnee Management Detail Component', () => {
    let comp: PeriodeAnneeDetailComponent;
    let fixture: ComponentFixture<PeriodeAnneeDetailComponent>;
    const route = ({ data: of({ periodeAnnee: new PeriodeAnnee(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [PeriodeAnneeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PeriodeAnneeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PeriodeAnneeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load periodeAnnee on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.periodeAnnee).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
