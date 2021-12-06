import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRaunkier, Raunkier } from '../raunkier.model';
import { RaunkierService } from '../service/raunkier.service';

@Component({
  selector: 'jhi-raunkier-update',
  templateUrl: './raunkier-update.component.html',
})
export class RaunkierUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
  });

  constructor(protected raunkierService: RaunkierService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ raunkier }) => {
      this.updateForm(raunkier);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const raunkier = this.createFromForm();
    if (raunkier.id !== undefined) {
      this.subscribeToSaveResponse(this.raunkierService.update(raunkier));
    } else {
      this.subscribeToSaveResponse(this.raunkierService.create(raunkier));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRaunkier>>): void {
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

  protected updateForm(raunkier: IRaunkier): void {
    this.editForm.patchValue({
      id: raunkier.id,
      type: raunkier.type,
    });
  }

  protected createFromForm(): IRaunkier {
    return {
      ...new Raunkier(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
    };
  }
}
