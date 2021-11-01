import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { NomVernaculaireDetailComponent } from 'app/entities/microservice/nom-vernaculaire/nom-vernaculaire-detail.component';
import { NomVernaculaire } from 'app/shared/model/microservice/nom-vernaculaire.model';

describe('Component Tests', () => {
  describe('NomVernaculaire Management Detail Component', () => {
    let comp: NomVernaculaireDetailComponent;
    let fixture: ComponentFixture<NomVernaculaireDetailComponent>;
    const route = ({ data: of({ nomVernaculaire: new NomVernaculaire(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [NomVernaculaireDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(NomVernaculaireDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NomVernaculaireDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load nomVernaculaire on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.nomVernaculaire).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
