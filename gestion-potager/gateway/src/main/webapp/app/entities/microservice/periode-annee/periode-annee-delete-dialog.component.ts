import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPeriodeAnnee } from 'app/shared/model/microservice/periode-annee.model';
import { PeriodeAnneeService } from './periode-annee.service';

@Component({
  templateUrl: './periode-annee-delete-dialog.component.html',
})
export class PeriodeAnneeDeleteDialogComponent {
  periodeAnnee?: IPeriodeAnnee;

  constructor(
    protected periodeAnneeService: PeriodeAnneeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.periodeAnneeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('periodeAnneeListModification');
      this.activeModal.close();
    });
  }
}
