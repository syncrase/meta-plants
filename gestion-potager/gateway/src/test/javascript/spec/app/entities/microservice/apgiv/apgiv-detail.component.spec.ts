import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { APGIVDetailComponent } from 'app/entities/microservice/apgiv/apgiv-detail.component';
import { APGIV } from 'app/shared/model/microservice/apgiv.model';

describe('Component Tests', () => {
  describe('APGIV Management Detail Component', () => {
    let comp: APGIVDetailComponent;
    let fixture: ComponentFixture<APGIVDetailComponent>;
    const route = ({ data: of({ aPGIV: new APGIV(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [APGIVDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(APGIVDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(APGIVDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aPGIV on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aPGIV).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
