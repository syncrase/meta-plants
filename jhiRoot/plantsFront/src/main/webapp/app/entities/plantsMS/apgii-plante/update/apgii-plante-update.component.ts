import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAPGIIPlante, APGIIPlante } from '../apgii-plante.model';
import { APGIIPlanteService } from '../service/apgii-plante.service';

@Component({
  selector: 'perma-apgii-plante-update',
  templateUrl: './apgii-plante-update.component.html',
})
export class APGIIPlanteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    ordre: [null, [Validators.required]],
    famille: [null, [Validators.required]],
  });

  constructor(protected aPGIIPlanteService: APGIIPlanteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGIIPlante }) => {
      this.updateForm(aPGIIPlante);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aPGIIPlante = this.createFromForm();
    if (aPGIIPlante.id !== undefined) {
      this.subscribeToSaveResponse(this.aPGIIPlanteService.update(aPGIIPlante));
    } else {
      this.subscribeToSaveResponse(this.aPGIIPlanteService.create(aPGIIPlante));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAPGIIPlante>>): void {
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

  protected updateForm(aPGIIPlante: IAPGIIPlante): void {
    this.editForm.patchValue({
      id: aPGIIPlante.id,
      ordre: aPGIIPlante.ordre,
      famille: aPGIIPlante.famille,
    });
  }

  protected createFromForm(): IAPGIIPlante {
    return {
      ...new APGIIPlante(),
      id: this.editForm.get(['id'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
      famille: this.editForm.get(['famille'])!.value,
    };
  }
}
