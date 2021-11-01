import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISol, Sol } from 'app/shared/model/microservice/sol.model';
import { SolService } from './sol.service';

@Component({
  selector: 'gp-sol-update',
  templateUrl: './sol-update.component.html',
})
export class SolUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    acidite: [],
    type: [],
  });

  constructor(protected solService: SolService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sol }) => {
      this.updateForm(sol);
    });
  }

  updateForm(sol: ISol): void {
    this.editForm.patchValue({
      id: sol.id,
      acidite: sol.acidite,
      type: sol.type,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sol = this.createFromForm();
    if (sol.id !== undefined) {
      this.subscribeToSaveResponse(this.solService.update(sol));
    } else {
      this.subscribeToSaveResponse(this.solService.create(sol));
    }
  }

  private createFromForm(): ISol {
    return {
      ...new Sol(),
      id: this.editForm.get(['id'])!.value,
      acidite: this.editForm.get(['acidite'])!.value,
      type: this.editForm.get(['type'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISol>>): void {
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
