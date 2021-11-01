import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { APGIVUpdateComponent } from 'app/entities/microservice/apgiv/apgiv-update.component';
import { APGIVService } from 'app/entities/microservice/apgiv/apgiv.service';
import { APGIV } from 'app/shared/model/microservice/apgiv.model';

describe('Component Tests', () => {
  describe('APGIV Management Update Component', () => {
    let comp: APGIVUpdateComponent;
    let fixture: ComponentFixture<APGIVUpdateComponent>;
    let service: APGIVService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [APGIVUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(APGIVUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(APGIVUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(APGIVService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new APGIV(123);
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
        const entity = new APGIV();
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
