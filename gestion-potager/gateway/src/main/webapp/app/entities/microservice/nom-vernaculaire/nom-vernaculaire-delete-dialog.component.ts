import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { INomVernaculaire } from 'app/shared/model/microservice/nom-vernaculaire.model';
import { NomVernaculaireService } from './nom-vernaculaire.service';

@Component({
  templateUrl: './nom-vernaculaire-delete-dialog.component.html',
})
export class NomVernaculaireDeleteDialogComponent {
  nomVernaculaire?: INomVernaculaire;

  constructor(
    protected nomVernaculaireService: NomVernaculaireService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nomVernaculaireService.delete(id).subscribe(() => {
      this.eventManager.broadcast('nomVernaculaireListModification');
      this.activeModal.close();
    });
  }
}
