import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { CycleDeVieDetailComponent } from 'app/entities/microservice/cycle-de-vie/cycle-de-vie-detail.component';
import { CycleDeVie } from 'app/shared/model/microservice/cycle-de-vie.model';

describe('Component Tests', () => {
  describe('CycleDeVie Management Detail Component', () => {
    let comp: CycleDeVieDetailComponent;
    let fixture: ComponentFixture<CycleDeVieDetailComponent>;
    const route = ({ data: of({ cycleDeVie: new CycleDeVie(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [CycleDeVieDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CycleDeVieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CycleDeVieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cycleDeVie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cycleDeVie).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
