import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IClade, Clade } from '../clade.model';
import { CladeService } from '../service/clade.service';

@Component({
  selector: 'perma-clade-update',
  templateUrl: './clade-update.component.html',
})
export class CladeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
  });

  constructor(protected cladeService: CladeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clade }) => {
      this.updateForm(clade);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clade = this.createFromForm();
    if (clade.id !== undefined) {
      this.subscribeToSaveResponse(this.cladeService.update(clade));
    } else {
      this.subscribeToSaveResponse(this.cladeService.create(clade));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClade>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(clade: IClade): void {
    this.editForm.patchValue({
      id: clade.id,
      nom: clade.nom,
    });
  }

  protected createFromForm(): IClade {
    return {
      ...new Clade(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
    };
  }
}
