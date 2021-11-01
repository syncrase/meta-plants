import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { RaunkierUpdateComponent } from 'app/entities/microservice/raunkier/raunkier-update.component';
import { RaunkierService } from 'app/entities/microservice/raunkier/raunkier.service';
import { Raunkier } from 'app/shared/model/microservice/raunkier.model';

describe('Component Tests', () => {
  describe('Raunkier Management Update Component', () => {
    let comp: RaunkierUpdateComponent;
    let fixture: ComponentFixture<RaunkierUpdateComponent>;
    let service: RaunkierService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [RaunkierUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(RaunkierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RaunkierUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RaunkierService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Raunkier(123);
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
        const entity = new Raunkier();
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
