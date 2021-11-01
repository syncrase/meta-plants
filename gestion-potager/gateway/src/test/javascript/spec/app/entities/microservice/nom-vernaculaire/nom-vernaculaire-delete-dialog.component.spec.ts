import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { GatewayTestModule } from '../../../../test.module';
import { MockEventManager } from '../../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../../helpers/mock-active-modal.service';
import { NomVernaculaireDeleteDialogComponent } from 'app/entities/microservice/nom-vernaculaire/nom-vernaculaire-delete-dialog.component';
import { NomVernaculaireService } from 'app/entities/microservice/nom-vernaculaire/nom-vernaculaire.service';

describe('Component Tests', () => {
  describe('NomVernaculaire Management Delete Component', () => {
    let comp: NomVernaculaireDeleteDialogComponent;
    let fixture: ComponentFixture<NomVernaculaireDeleteDialogComponent>;
    let service: NomVernaculaireService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [NomVernaculaireDeleteDialogComponent],
      })
        .overrideTemplate(NomVernaculaireDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NomVernaculaireDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NomVernaculaireService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
