import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IRessemblance, Ressemblance } from 'app/shared/model/microservice/ressemblance.model';
import { RessemblanceService } from './ressemblance.service';
import { IPlante } from 'app/shared/model/microservice/plante.model';
import { PlanteService } from 'app/entities/microservice/plante/plante.service';

@Component({
  selector: 'gp-ressemblance-update',
  templateUrl: './ressemblance-update.component.html',
})
export class RessemblanceUpdateComponent implements OnInit {
  isSaving = false;
  plantes: IPlante[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    confusionId: [],
  });

  constructor(
    protected ressemblanceService: RessemblanceService,
    protected planteService: PlanteService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ressemblance }) => {
      this.updateForm(ressemblance);

      this.planteService.query().subscribe((res: HttpResponse<IPlante[]>) => (this.plantes = res.body || []));
    });
  }

  updateForm(ressemblance: IRessemblance): void {
    this.editForm.patchValue({
      id: ressemblance.id,
      description: ressemblance.description,
      confusionId: ressemblance.confusionId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ressemblance = this.createFromForm();
    if (ressemblance.id !== undefined) {
      this.subscribeToSaveResponse(this.ressemblanceService.update(ressemblance));
    } else {
      this.subscribeToSaveResponse(this.ressemblanceService.create(ressemblance));
    }
  }

  private createFromForm(): IRessemblance {
    return {
      ...new Ressemblance(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      confusionId: this.editForm.get(['confusionId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRessemblance>>): void {
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

  trackById(index: number, item: IPlante): any {
    return item.id;
  }
}
