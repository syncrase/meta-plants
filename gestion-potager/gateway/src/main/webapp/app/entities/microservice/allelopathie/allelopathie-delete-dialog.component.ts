import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAllelopathie } from 'app/shared/model/microservice/allelopathie.model';
import { AllelopathieService } from './allelopathie.service';

@Component({
  templateUrl: './allelopathie-delete-dialog.component.html',
})
export class AllelopathieDeleteDialogComponent {
  allelopathie?: IAllelopathie;

  constructor(
    protected allelopathieService: AllelopathieService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.allelopathieService.delete(id).subscribe(() => {
      this.eventManager.broadcast('allelopathieListModification');
      this.activeModal.close();
    });
  }
}
