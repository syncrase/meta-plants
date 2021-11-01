import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { CycleDeVieUpdateComponent } from 'app/entities/microservice/cycle-de-vie/cycle-de-vie-update.component';
import { CycleDeVieService } from 'app/entities/microservice/cycle-de-vie/cycle-de-vie.service';
import { CycleDeVie } from 'app/shared/model/microservice/cycle-de-vie.model';

describe('Component Tests', () => {
  describe('CycleDeVie Management Update Component', () => {
    let comp: CycleDeVieUpdateComponent;
    let fixture: ComponentFixture<CycleDeVieUpdateComponent>;
    let service: CycleDeVieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [CycleDeVieUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CycleDeVieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CycleDeVieUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CycleDeVieService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CycleDeVie(123);
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
        const entity = new CycleDeVie();
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
