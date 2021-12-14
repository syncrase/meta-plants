import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IForme } from '../forme.model';
import { FormeService } from '../service/forme.service';

@Component({
  templateUrl: './forme-delete-dialog.component.html',
})
export class FormeDeleteDialogComponent {
  forme?: IForme;

  constructor(protected formeService: FormeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
