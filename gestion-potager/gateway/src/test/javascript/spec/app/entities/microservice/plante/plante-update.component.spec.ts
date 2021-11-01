import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { PlanteUpdateComponent } from 'app/entities/microservice/plante/plante-update.component';
import { PlanteService } from 'app/entities/microservice/plante/plante.service';
import { Plante } from 'app/shared/model/microservice/plante.model';

describe('Component Tests', () => {
  describe('Plante Management Update Component', () => {
    let comp: PlanteUpdateComponent;
    let fixture: ComponentFixture<PlanteUpdateComponent>;
    let service: PlanteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [PlanteUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PlanteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlanteUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlanteService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Plante(123);
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
        const entity = new Plante();
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
