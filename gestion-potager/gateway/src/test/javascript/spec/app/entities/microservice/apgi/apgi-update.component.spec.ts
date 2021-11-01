import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { APGIUpdateComponent } from 'app/entities/microservice/apgi/apgi-update.component';
import { APGIService } from 'app/entities/microservice/apgi/apgi.service';
import { APGI } from 'app/shared/model/microservice/apgi.model';

describe('Component Tests', () => {
  describe('APGI Management Update Component', () => {
    let comp: APGIUpdateComponent;
    let fixture: ComponentFixture<APGIUpdateComponent>;
    let service: APGIService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [APGIUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(APGIUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(APGIUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(APGIService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new APGI(123);
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
        const entity = new APGI();
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
