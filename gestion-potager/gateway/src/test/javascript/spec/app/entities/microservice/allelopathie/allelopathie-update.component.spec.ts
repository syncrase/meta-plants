import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { AllelopathieUpdateComponent } from 'app/entities/microservice/allelopathie/allelopathie-update.component';
import { AllelopathieService } from 'app/entities/microservice/allelopathie/allelopathie.service';
import { Allelopathie } from 'app/shared/model/microservice/allelopathie.model';

describe('Component Tests', () => {
  describe('Allelopathie Management Update Component', () => {
    let comp: AllelopathieUpdateComponent;
    let fixture: ComponentFixture<AllelopathieUpdateComponent>;
    let service: AllelopathieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [AllelopathieUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AllelopathieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AllelopathieUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AllelopathieService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Allelopathie(123);
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
        const entity = new Allelopathie();
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
