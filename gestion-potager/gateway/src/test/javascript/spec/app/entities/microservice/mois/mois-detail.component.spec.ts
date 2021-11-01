import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { MoisDetailComponent } from 'app/entities/microservice/mois/mois-detail.component';
import { Mois } from 'app/shared/model/microservice/mois.model';

describe('Component Tests', () => {
  describe('Mois Management Detail Component', () => {
    let comp: MoisDetailComponent;
    let fixture: ComponentFixture<MoisDetailComponent>;
    const route = ({ data: of({ mois: new Mois(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [MoisDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(MoisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MoisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mois on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mois).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
