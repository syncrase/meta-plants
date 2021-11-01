import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { SemisUpdateComponent } from 'app/entities/microservice/semis/semis-update.component';
import { SemisService } from 'app/entities/microservice/semis/semis.service';
import { Semis } from 'app/shared/model/microservice/semis.model';

describe('Component Tests', () => {
  describe('Semis Management Update Component', () => {
    let comp: SemisUpdateComponent;
    let fixture: ComponentFixture<SemisUpdateComponent>;
    let service: SemisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [SemisUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SemisUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SemisUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SemisService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Semis(123);
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
        const entity = new Semis();
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
