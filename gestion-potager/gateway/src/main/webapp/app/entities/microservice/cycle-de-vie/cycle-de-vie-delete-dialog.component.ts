import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICycleDeVie } from 'app/shared/model/microservice/cycle-de-vie.model';
import { CycleDeVieService } from './cycle-de-vie.service';

@Component({
  templateUrl: './cycle-de-vie-delete-dialog.component.html',
})
export class CycleDeVieDeleteDialogComponent {
  cycleDeVie?: ICycleDeVie;

  constructor(
    protected cycleDeVieService: CycleDeVieService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cycleDeVieService.delete(id).subscribe(() => {
      this.eventManager.broadcast('cycleDeVieListModification');
      this.activeModal.close();
    });
  }
}
