import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { CronquistUpdateComponent } from 'app/entities/microservice/cronquist/cronquist-update.component';
import { CronquistService } from 'app/entities/microservice/cronquist/cronquist.service';
import { Cronquist } from 'app/shared/model/microservice/cronquist.model';

describe('Component Tests', () => {
  describe('Cronquist Management Update Component', () => {
    let comp: CronquistUpdateComponent;
    let fixture: ComponentFixture<CronquistUpdateComponent>;
    let service: CronquistService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [CronquistUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CronquistUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CronquistUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CronquistService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Cronquist(123);
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
        const entity = new Cronquist();
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
