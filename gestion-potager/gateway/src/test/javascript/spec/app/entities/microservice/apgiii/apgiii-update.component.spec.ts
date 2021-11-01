import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { APGIIIUpdateComponent } from 'app/entities/microservice/apgiii/apgiii-update.component';
import { APGIIIService } from 'app/entities/microservice/apgiii/apgiii.service';
import { APGIII } from 'app/shared/model/microservice/apgiii.model';

describe('Component Tests', () => {
  describe('APGIII Management Update Component', () => {
    let comp: APGIIIUpdateComponent;
    let fixture: ComponentFixture<APGIIIUpdateComponent>;
    let service: APGIIIService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [APGIIIUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(APGIIIUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(APGIIIUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(APGIIIService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new APGIII(123);
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
        const entity = new APGIII();
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
