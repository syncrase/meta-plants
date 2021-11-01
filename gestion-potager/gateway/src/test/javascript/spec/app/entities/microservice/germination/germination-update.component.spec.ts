import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { GerminationUpdateComponent } from 'app/entities/microservice/germination/germination-update.component';
import { GerminationService } from 'app/entities/microservice/germination/germination.service';
import { Germination } from 'app/shared/model/microservice/germination.model';

describe('Component Tests', () => {
  describe('Germination Management Update Component', () => {
    let comp: GerminationUpdateComponent;
    let fixture: ComponentFixture<GerminationUpdateComponent>;
    let service: GerminationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [GerminationUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(GerminationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GerminationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GerminationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Germination(123);
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
        const entity = new Germination();
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
