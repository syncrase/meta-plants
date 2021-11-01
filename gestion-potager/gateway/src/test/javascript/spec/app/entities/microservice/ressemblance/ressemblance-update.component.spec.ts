import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { RessemblanceUpdateComponent } from 'app/entities/microservice/ressemblance/ressemblance-update.component';
import { RessemblanceService } from 'app/entities/microservice/ressemblance/ressemblance.service';
import { Ressemblance } from 'app/shared/model/microservice/ressemblance.model';

describe('Component Tests', () => {
  describe('Ressemblance Management Update Component', () => {
    let comp: RessemblanceUpdateComponent;
    let fixture: ComponentFixture<RessemblanceUpdateComponent>;
    let service: RessemblanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [RessemblanceUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(RessemblanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RessemblanceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RessemblanceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Ressemblance(123);
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
        const entity = new Ressemblance();
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
