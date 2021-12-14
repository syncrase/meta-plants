import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInfraEmbranchement, InfraEmbranchement } from '../infra-embranchement.model';
import { InfraEmbranchementService } from '../service/infra-embranchement.service';
import { ISousDivision } from 'app/entities/classificationMS/sous-division/sous-division.model';
import { SousDivisionService } from 'app/entities/classificationMS/sous-division/service/sous-division.service';

@Component({
  selector: 'perma-infra-embranchement-update',
  templateUrl: './infra-embranchement-update.component.html',
})
export class InfraEmbranchementUpdateComponent implements OnInit {
  isSaving = false;

  infraEmbranchementsSharedCollection: IInfraEmbranchement[] = [];
  sousDivisionsSharedCollection: ISousDivision[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    sousDivision: [],
    infraEmbranchement: [],
  });

  constructor(
    protected infraEmbranchementService: InfraEmbranchementService,
    protected sousDivisionService: SousDivisionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infraEmbranchement }) => {
      this.updateForm(infraEmbranchement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const infraEmbranchement = this.createFromForm();
    if (infraEmbranchement.id !== undefined) {
      this.subscribeToSaveResponse(this.infraEmbranchementService.update(infraEmbranchement));
    } else {
      this.subscribeToSaveResponse(this.infraEmbranchementService.create(infraEmbranchement));
    }
  }

  trackInfraEmbranchementById(index: number, item: IInfraEmbranchement): number {
    return item.id!;
  }

  trackSousDivisionById(index: number, item: ISousDivision): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInfraEmbranchement>>): void {
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

  protected updateForm(infraEmbranchement: IInfraEmbranchement): void {
    this.editForm.patchValue({
      id: infraEmbranchement.id,
      nomFr: infraEmbranchement.nomFr,
      nomLatin: infraEmbranchement.nomLatin,
      sousDivision: infraEmbranchement.sousDivision,
      infraEmbranchement: infraEmbranchement.infraEmbranchement,
    });

    this.infraEmbranchementsSharedCollection = this.infraEmbranchementService.addInfraEmbranchementToCollectionIfMissing(
      this.infraEmbranchementsSharedCollection,
      infraEmbranchement.infraEmbranchement
    );
    this.sousDivisionsSharedCollection = this.sousDivisionService.addSousDivisionToCollectionIfMissing(
      this.sousDivisionsSharedCollection,
      infraEmbranchement.sousDivision
    );
  }

  protected loadRelationshipsOptions(): void {
    this.infraEmbranchementService
      .query()
      .pipe(map((res: HttpResponse<IInfraEmbranchement[]>) => res.body ?? []))
      .pipe(
        map((infraEmbranchements: IInfraEmbranchement[]) =>
          this.infraEmbranchementService.addInfraEmbranchementToCollectionIfMissing(
            infraEmbranchements,
            this.editForm.get('infraEmbranchement')!.value
          )
        )
      )
      .subscribe((infraEmbranchements: IInfraEmbranchement[]) => (this.infraEmbranchementsSharedCollection = infraEmbranchements));

    this.sousDivisionService
      .query()
      .pipe(map((res: HttpResponse<ISousDivision[]>) => res.body ?? []))
      .pipe(
        map((sousDivisions: ISousDivision[]) =>
          this.sousDivisionService.addSousDivisionToCollectionIfMissing(sousDivisions, this.editForm.get('sousDivision')!.value)
        )
      )
      .subscribe((sousDivisions: ISousDivision[]) => (this.sousDivisionsSharedCollection = sousDivisions));
  }

  protected createFromForm(): IInfraEmbranchement {
    return {
      ...new InfraEmbranchement(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      sousDivision: this.editForm.get(['sousDivision'])!.value,
      infraEmbranchement: this.editForm.get(['infraEmbranchement'])!.value,
    };
  }
}
