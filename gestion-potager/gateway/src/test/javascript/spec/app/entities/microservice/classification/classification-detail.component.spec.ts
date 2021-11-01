import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { ClassificationDetailComponent } from 'app/entities/microservice/classification/classification-detail.component';
import { Classification } from 'app/shared/model/microservice/classification.model';

describe('Component Tests', () => {
  describe('Classification Management Detail Component', () => {
    let comp: ClassificationDetailComponent;
    let fixture: ComponentFixture<ClassificationDetailComponent>;
    const route = ({ data: of({ classification: new Classification(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [ClassificationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ClassificationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassificationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load classification on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.classification).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
