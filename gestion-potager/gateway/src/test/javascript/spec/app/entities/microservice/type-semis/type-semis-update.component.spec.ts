import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { TypeSemisUpdateComponent } from 'app/entities/microservice/type-semis/type-semis-update.component';
import { TypeSemisService } from 'app/entities/microservice/type-semis/type-semis.service';
import { TypeSemis } from 'app/shared/model/microservice/type-semis.model';

describe('Component Tests', () => {
  describe('TypeSemis Management Update Component', () => {
    let comp: TypeSemisUpdateComponent;
    let fixture: ComponentFixture<TypeSemisUpdateComponent>;
    let service: TypeSemisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [TypeSemisUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TypeSemisUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TypeSemisUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TypeSemisService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TypeSemis(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new TypeSemis();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
