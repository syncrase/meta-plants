import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { RaunkierDetailComponent } from 'app/entities/microservice/raunkier/raunkier-detail.component';
import { Raunkier } from 'app/shared/model/microservice/raunkier.model';

describe('Component Tests', () => {
  describe('Raunkier Management Detail Component', () => {
    let comp: RaunkierDetailComponent;
    let fixture: ComponentFixture<RaunkierDetailComponent>;
    const route = ({ data: of({ raunkier: new Raunkier(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [RaunkierDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(RaunkierDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RaunkierDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load raunkier on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.raunkier).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
