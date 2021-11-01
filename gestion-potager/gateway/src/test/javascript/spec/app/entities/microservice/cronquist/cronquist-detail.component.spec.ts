import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { CronquistDetailComponent } from 'app/entities/microservice/cronquist/cronquist-detail.component';
import { Cronquist } from 'app/shared/model/microservice/cronquist.model';

describe('Component Tests', () => {
  describe('Cronquist Management Detail Component', () => {
    let comp: CronquistDetailComponent;
    let fixture: ComponentFixture<CronquistDetailComponent>;
    const route = ({ data: of({ cronquist: new Cronquist(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [CronquistDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CronquistDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CronquistDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cronquist on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cronquist).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
