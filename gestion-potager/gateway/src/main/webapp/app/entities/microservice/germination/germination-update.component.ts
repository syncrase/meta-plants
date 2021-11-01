import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IGermination, Germination } from 'app/shared/model/microservice/germination.model';
import { GerminationService } from './germination.service';

@Component({
  selector: 'gp-germination-update',
  templateUrl: './germination-update.component.html',
})
export class GerminationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    tempsDeGermination: [],
    conditionDeGermination: [],
  });

  constructor(protected germinationService: GerminationService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ germination }) => {
      this.updateForm(germination);
    });
  }

  updateForm(germination: IGermination): void {
    this.editForm.patchValue({
      id: germination.id,
      tempsDeGermination: germination.tempsDeGermination,
      conditionDeGermination: germination.conditionDeGermination,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const germination = this.createFromForm();
    if (germination.id !== undefined) {
      this.subscribeToSaveResponse(this.germinationService.update(germination));
    } else {
      this.subscribeToSaveResponse(this.germinationService.create(germination));
    }
  }

  private createFromForm(): IGermination {
    return {
      ...new Germination(),
      id: this.editForm.get(['id'])!.value,
      tempsDeGermination: this.editForm.get(['tempsDeGermination'])!.value,
      conditionDeGermination: this.editForm.get(['conditionDeGermination'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGermination>>): void {
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
