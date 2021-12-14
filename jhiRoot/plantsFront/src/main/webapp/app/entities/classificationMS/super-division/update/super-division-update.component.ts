import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISuperDivision, SuperDivision } from '../super-division.model';
import { SuperDivisionService } from '../service/super-division.service';
import { IInfraRegne } from 'app/entities/classificationMS/infra-regne/infra-regne.model';
import { InfraRegneService } from 'app/entities/classificationMS/infra-regne/service/infra-regne.service';

@Component({
  selector: 'perma-super-division-update',
  templateUrl: './super-division-update.component.html',
})
export class SuperDivisionUpdateComponent implements OnInit {
  isSaving = false;

  superDivisionsSharedCollection: ISuperDivision[] = [];
  infraRegnesSharedCollection: IInfraRegne[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    infraRegne: [],
    superDivision: [],
  });

  constructor(
    protected superDivisionService: SuperDivisionService,
    protected infraRegneService: InfraRegneService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ superDivision }) => {
      this.updateForm(superDivision);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const superDivision = this.createFromForm();
    if (superDivision.id !== undefined) {
      this.subscribeToSaveResponse(this.superDivisionService.update(superDivision));
    } else {
      this.subscribeToSaveResponse(this.superDivisionService.create(superDivision));
    }
  }

  trackSuperDivisionById(index: number, item: ISuperDivision): number {
    return item.id!;
  }

  trackInfraRegneById(index: number, item: IInfraRegne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISuperDivision>>): void {
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

  protected updateForm(superDivision: ISuperDivision): void {
    this.editForm.patchValue({
      id: superDivision.id,
      nomFr: superDivision.nomFr,
      nomLatin: superDivision.nomLatin,
      infraRegne: superDivision.infraRegne,
      superDivision: superDivision.superDivision,
    });

    this.superDivisionsSharedCollection = this.superDivisionService.addSuperDivisionToCollectionIfMissing(
      this.superDivisionsSharedCollection,
      superDivision.superDivision
    );
    this.infraRegnesSharedCollection = this.infraRegneService.addInfraRegneToCollectionIfMissing(
      this.infraRegnesSharedCollection,
      superDivision.infraRegne
    );
  }

  protected loadRelationshipsOptions(): void {
    this.superDivisionService
      .query()
      .pipe(map((res: HttpResponse<ISuperDivision[]>) => res.body ?? []))
      .pipe(
        map((superDivisions: ISuperDivision[]) =>
          this.superDivisionService.addSuperDivisionToCollectionIfMissing(superDivisions, this.editForm.get('superDivision')!.value)
        )
      )
      .subscribe((superDivisions: ISuperDivision[]) => (this.superDivisionsSharedCollection = superDivisions));

    this.infraRegneService
      .query()
      .pipe(map((res: HttpResponse<IInfraRegne[]>) => res.body ?? []))
      .pipe(
        map((infraRegnes: IInfraRegne[]) =>
          this.infraRegneService.addInfraRegneToCollectionIfMissing(infraRegnes, this.editForm.get('infraRegne')!.value)
        )
      )
      .subscribe((infraRegnes: IInfraRegne[]) => (this.infraRegnesSharedCollection = infraRegnes));
  }

  protected createFromForm(): ISuperDivision {
    return {
      ...new SuperDivision(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      infraRegne: this.editForm.get(['infraRegne'])!.value,
      superDivision: this.editForm.get(['superDivision'])!.value,
    };
  }
}
