import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAPGIVPlante, APGIVPlante } from '../apgiv-plante.model';
import { APGIVPlanteService } from '../service/apgiv-plante.service';

@Component({
  selector: 'perma-apgiv-plante-update',
  templateUrl: './apgiv-plante-update.component.html',
})
export class APGIVPlanteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    ordre: [null, [Validators.required]],
    famille: [null, [Validators.required]],
  });

  constructor(protected aPGIVPlanteService: APGIVPlanteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGIVPlante }) => {
      this.updateForm(aPGIVPlante);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aPGIVPlante = this.createFromForm();
    if (aPGIVPlante.id !== undefined) {
      this.subscribeToSaveResponse(this.aPGIVPlanteService.update(aPGIVPlante));
    } else {
      this.subscribeToSaveResponse(this.aPGIVPlanteService.create(aPGIVPlante));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAPGIVPlante>>): void {
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

  protected updateForm(aPGIVPlante: IAPGIVPlante): void {
    this.editForm.patchValue({
      id: aPGIVPlante.id,
      ordre: aPGIVPlante.ordre,
      famille: aPGIVPlante.famille,
    });
  }

  protected createFromForm(): IAPGIVPlante {
    return {
      ...new APGIVPlante(),
      id: this.editForm.get(['id'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
      famille: this.editForm.get(['famille'])!.value,
    };
  }
}
