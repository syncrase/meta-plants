import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { SolUpdateComponent } from 'app/entities/microservice/sol/sol-update.component';
import { SolService } from 'app/entities/microservice/sol/sol.service';
import { Sol } from 'app/shared/model/microservice/sol.model';

describe('Component Tests', () => {
  describe('Sol Management Update Component', () => {
    let comp: SolUpdateComponent;
    let fixture: ComponentFixture<SolUpdateComponent>;
    let service: SolService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [SolUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SolUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SolUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SolService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Sol(123);
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
        const entity = new Sol();
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
