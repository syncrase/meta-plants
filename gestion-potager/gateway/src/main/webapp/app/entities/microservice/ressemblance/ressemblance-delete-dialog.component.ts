import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRessemblance } from 'app/shared/model/microservice/ressemblance.model';
import { RessemblanceService } from './ressemblance.service';

@Component({
  templateUrl: './ressemblance-delete-dialog.component.html',
})
export class RessemblanceDeleteDialogComponent {
  ressemblance?: IRessemblance;

  constructor(
    protected ressemblanceService: RessemblanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ressemblanceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('ressemblanceListModification');
      this.activeModal.close();
    });
  }
}
