import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { APGIIUpdateComponent } from 'app/entities/microservice/apgii/apgii-update.component';
import { APGIIService } from 'app/entities/microservice/apgii/apgii.service';
import { APGII } from 'app/shared/model/microservice/apgii.model';

describe('Component Tests', () => {
  describe('APGII Management Update Component', () => {
    let comp: APGIIUpdateComponent;
    let fixture: ComponentFixture<APGIIUpdateComponent>;
    let service: APGIIService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [APGIIUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(APGIIUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(APGIIUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(APGIIService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new APGII(123);
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
        const entity = new APGII();
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
