import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { APGIIIDetailComponent } from 'app/entities/microservice/apgiii/apgiii-detail.component';
import { APGIII } from 'app/shared/model/microservice/apgiii.model';

describe('Component Tests', () => {
  describe('APGIII Management Detail Component', () => {
    let comp: APGIIIDetailComponent;
    let fixture: ComponentFixture<APGIIIDetailComponent>;
    const route = ({ data: of({ aPGIII: new APGIII(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [APGIIIDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(APGIIIDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(APGIIIDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aPGIII on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aPGIII).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
