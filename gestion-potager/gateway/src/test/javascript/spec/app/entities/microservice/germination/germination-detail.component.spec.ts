import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { GerminationDetailComponent } from 'app/entities/microservice/germination/germination-detail.component';
import { Germination } from 'app/shared/model/microservice/germination.model';

describe('Component Tests', () => {
  describe('Germination Management Detail Component', () => {
    let comp: GerminationDetailComponent;
    let fixture: ComponentFixture<GerminationDetailComponent>;
    const route = ({ data: of({ germination: new Germination(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [GerminationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(GerminationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GerminationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load germination on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.germination).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
