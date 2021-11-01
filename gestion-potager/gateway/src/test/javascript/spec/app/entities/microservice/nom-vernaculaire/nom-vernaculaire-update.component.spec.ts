import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { NomVernaculaireUpdateComponent } from 'app/entities/microservice/nom-vernaculaire/nom-vernaculaire-update.component';
import { NomVernaculaireService } from 'app/entities/microservice/nom-vernaculaire/nom-vernaculaire.service';
import { NomVernaculaire } from 'app/shared/model/microservice/nom-vernaculaire.model';

describe('Component Tests', () => {
  describe('NomVernaculaire Management Update Component', () => {
    let comp: NomVernaculaireUpdateComponent;
    let fixture: ComponentFixture<NomVernaculaireUpdateComponent>;
    let service: NomVernaculaireService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [NomVernaculaireUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(NomVernaculaireUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NomVernaculaireUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NomVernaculaireService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new NomVernaculaire(123);
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
        const entity = new NomVernaculaire();
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
