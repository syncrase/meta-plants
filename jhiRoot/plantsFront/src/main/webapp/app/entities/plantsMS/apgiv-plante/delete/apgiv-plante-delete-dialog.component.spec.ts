jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { APGIVPlanteService } from '../service/apgiv-plante.service';

import { APGIVPlanteDeleteDialogComponent } from './apgiv-plante-delete-dialog.component';

describe('APGIVPlante Management Delete Component', () => {
  let comp: APGIVPlanteDeleteDialogComponent;
  let fixture: ComponentFixture<APGIVPlanteDeleteDialogComponent>;
  let service: APGIVPlanteService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [APGIVPlanteDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(APGIVPlanteDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(APGIVPlanteDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(APGIVPlanteService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
