import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { SemisDetailComponent } from 'app/entities/microservice/semis/semis-detail.component';
import { Semis } from 'app/shared/model/microservice/semis.model';

describe('Component Tests', () => {
  describe('Semis Management Detail Component', () => {
    let comp: SemisDetailComponent;
    let fixture: ComponentFixture<SemisDetailComponent>;
    const route = ({ data: of({ semis: new Semis(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [SemisDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SemisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SemisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load semis on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.semis).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
