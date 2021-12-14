jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SuperOrdreService } from '../service/super-ordre.service';

import { SuperOrdreDeleteDialogComponent } from './super-ordre-delete-dialog.component';

describe('SuperOrdre Management Delete Component', () => {
  let comp: SuperOrdreDeleteDialogComponent;
  let fixture: ComponentFixture<SuperOrdreDeleteDialogComponent>;
  let service: SuperOrdreService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SuperOrdreDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(SuperOrdreDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SuperOrdreDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SuperOrdreService);
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
