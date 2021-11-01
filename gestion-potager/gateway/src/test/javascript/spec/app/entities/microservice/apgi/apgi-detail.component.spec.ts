import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { APGIDetailComponent } from 'app/entities/microservice/apgi/apgi-detail.component';
import { APGI } from 'app/shared/model/microservice/apgi.model';

describe('Component Tests', () => {
  describe('APGI Management Detail Component', () => {
    let comp: APGIDetailComponent;
    let fixture: ComponentFixture<APGIDetailComponent>;
    const route = ({ data: of({ aPGI: new APGI(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [APGIDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(APGIDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(APGIDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aPGI on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aPGI).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
