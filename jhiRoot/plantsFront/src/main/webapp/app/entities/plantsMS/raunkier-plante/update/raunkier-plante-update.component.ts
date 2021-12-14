import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRaunkierPlante, RaunkierPlante } from '../raunkier-plante.model';
import { RaunkierPlanteService } from '../service/raunkier-plante.service';

@Component({
  selector: 'perma-raunkier-plante-update',
  templateUrl: './raunkier-plante-update.component.html',
})
export class RaunkierPlanteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
  });

  constructor(
    protected raunkierPlanteService: RaunkierPlanteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ raunkierPlante }) => {
      this.updateForm(raunkierPlante);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const raunkierPlante = this.createFromForm();
    if (raunkierPlante.id !== undefined) {
      this.subscribeToSaveResponse(this.raunkierPlanteService.update(raunkierPlante));
    } else {
      this.subscribeToSaveResponse(this.raunkierPlanteService.create(raunkierPlante));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRaunkierPlante>>): void {
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

  protected updateForm(raunkierPlante: IRaunkierPlante): void {
    this.editForm.patchValue({
      id: raunkierPlante.id,
      type: raunkierPlante.type,
    });
  }

  protected createFromForm(): IRaunkierPlante {
    return {
      ...new RaunkierPlante(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
    };
  }
}
