import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAPGIPlante, APGIPlante } from '../apgi-plante.model';
import { APGIPlanteService } from '../service/apgi-plante.service';

@Component({
  selector: 'perma-apgi-plante-update',
  templateUrl: './apgi-plante-update.component.html',
})
export class APGIPlanteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    ordre: [null, [Validators.required]],
    famille: [null, [Validators.required]],
  });

  constructor(protected aPGIPlanteService: APGIPlanteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGIPlante }) => {
      this.updateForm(aPGIPlante);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aPGIPlante = this.createFromForm();
    if (aPGIPlante.id !== undefined) {
      this.subscribeToSaveResponse(this.aPGIPlanteService.update(aPGIPlante));
    } else {
      this.subscribeToSaveResponse(this.aPGIPlanteService.create(aPGIPlante));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAPGIPlante>>): void {
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

  protected updateForm(aPGIPlante: IAPGIPlante): void {
    this.editForm.patchValue({
      id: aPGIPlante.id,
      ordre: aPGIPlante.ordre,
      famille: aPGIPlante.famille,
    });
  }

  protected createFromForm(): IAPGIPlante {
    return {
      ...new APGIPlante(),
      id: this.editForm.get(['id'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
      famille: this.editForm.get(['famille'])!.value,
    };
  }
}
