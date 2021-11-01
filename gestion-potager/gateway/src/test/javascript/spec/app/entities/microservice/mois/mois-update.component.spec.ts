import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { MoisUpdateComponent } from 'app/entities/microservice/mois/mois-update.component';
import { MoisService } from 'app/entities/microservice/mois/mois.service';
import { Mois } from 'app/shared/model/microservice/mois.model';

describe('Component Tests', () => {
  describe('Mois Management Update Component', () => {
    let comp: MoisUpdateComponent;
    let fixture: ComponentFixture<MoisUpdateComponent>;
    let service: MoisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [MoisUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(MoisUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MoisUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MoisService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Mois(123);
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
        const entity = new Mois();
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
