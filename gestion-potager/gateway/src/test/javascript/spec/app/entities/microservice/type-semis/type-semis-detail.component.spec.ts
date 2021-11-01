import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { TypeSemisDetailComponent } from 'app/entities/microservice/type-semis/type-semis-detail.component';
import { TypeSemis } from 'app/shared/model/microservice/type-semis.model';

describe('Component Tests', () => {
  describe('TypeSemis Management Detail Component', () => {
    let comp: TypeSemisDetailComponent;
    let fixture: ComponentFixture<TypeSemisDetailComponent>;
    const route = ({ data: of({ typeSemis: new TypeSemis(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [TypeSemisDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TypeSemisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TypeSemisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load typeSemis on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.typeSemis).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
