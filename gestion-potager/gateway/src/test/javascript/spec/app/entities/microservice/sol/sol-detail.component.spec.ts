import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { SolDetailComponent } from 'app/entities/microservice/sol/sol-detail.component';
import { Sol } from 'app/shared/model/microservice/sol.model';

describe('Component Tests', () => {
  describe('Sol Management Detail Component', () => {
    let comp: SolDetailComponent;
    let fixture: ComponentFixture<SolDetailComponent>;
    const route = ({ data: of({ sol: new Sol(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [SolDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SolDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SolDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load sol on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sol).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
