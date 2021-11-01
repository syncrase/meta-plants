import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { AllelopathieDetailComponent } from 'app/entities/microservice/allelopathie/allelopathie-detail.component';
import { Allelopathie } from 'app/shared/model/microservice/allelopathie.model';

describe('Component Tests', () => {
  describe('Allelopathie Management Detail Component', () => {
    let comp: AllelopathieDetailComponent;
    let fixture: ComponentFixture<AllelopathieDetailComponent>;
    const route = ({ data: of({ allelopathie: new Allelopathie(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [AllelopathieDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AllelopathieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AllelopathieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load allelopathie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.allelopathie).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
