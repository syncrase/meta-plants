import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IRaunkier, Raunkier } from 'app/shared/model/microservice/raunkier.model';
import { RaunkierService } from './raunkier.service';

@Component({
  selector: 'gp-raunkier-update',
  templateUrl: './raunkier-update.component.html',
})
export class RaunkierUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
  });

  constructor(protected raunkierService: RaunkierService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ raunkier }) => {
      this.updateForm(raunkier);
    });
  }

  updateForm(raunkier: IRaunkier): void {
    this.editForm.patchValue({
      id: raunkier.id,
      type: raunkier.type,
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

  private createFromForm(): IRaunkier {
    return {
      ...new Raunkier(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRaunkier>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
