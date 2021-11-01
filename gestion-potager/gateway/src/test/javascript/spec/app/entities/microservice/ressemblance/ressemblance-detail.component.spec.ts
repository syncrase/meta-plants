import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { RessemblanceDetailComponent } from 'app/entities/microservice/ressemblance/ressemblance-detail.component';
import { Ressemblance } from 'app/shared/model/microservice/ressemblance.model';

describe('Component Tests', () => {
  describe('Ressemblance Management Detail Component', () => {
    let comp: RessemblanceDetailComponent;
    let fixture: ComponentFixture<RessemblanceDetailComponent>;
    const route = ({ data: of({ ressemblance: new Ressemblance(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [RessemblanceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(RessemblanceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RessemblanceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ressemblance on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ressemblance).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
