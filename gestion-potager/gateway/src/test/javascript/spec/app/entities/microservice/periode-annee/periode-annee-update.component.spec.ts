import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { PeriodeAnneeUpdateComponent } from 'app/entities/microservice/periode-annee/periode-annee-update.component';
import { PeriodeAnneeService } from 'app/entities/microservice/periode-annee/periode-annee.service';
import { PeriodeAnnee } from 'app/shared/model/microservice/periode-annee.model';

describe('Component Tests', () => {
  describe('PeriodeAnnee Management Update Component', () => {
    let comp: PeriodeAnneeUpdateComponent;
    let fixture: ComponentFixture<PeriodeAnneeUpdateComponent>;
    let service: PeriodeAnneeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [PeriodeAnneeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PeriodeAnneeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PeriodeAnneeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PeriodeAnneeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PeriodeAnnee(123);
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
        const entity = new PeriodeAnnee();
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
