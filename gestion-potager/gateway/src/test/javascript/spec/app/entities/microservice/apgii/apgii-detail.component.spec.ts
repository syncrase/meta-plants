import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { APGIIDetailComponent } from 'app/entities/microservice/apgii/apgii-detail.component';
import { APGII } from 'app/shared/model/microservice/apgii.model';

describe('Component Tests', () => {
  describe('APGII Management Detail Component', () => {
    let comp: APGIIDetailComponent;
    let fixture: ComponentFixture<APGIIDetailComponent>;
    const route = ({ data: of({ aPGII: new APGII(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [APGIIDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(APGIIDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(APGIIDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aPGII on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aPGII).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
