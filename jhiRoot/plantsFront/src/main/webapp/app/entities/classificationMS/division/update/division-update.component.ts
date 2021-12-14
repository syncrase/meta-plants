import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDivision, Division } from '../division.model';
import { DivisionService } from '../service/division.service';
import { ISuperDivision } from 'app/entities/classificationMS/super-division/super-division.model';
import { SuperDivisionService } from 'app/entities/classificationMS/super-division/service/super-division.service';

@Component({
  selector: 'perma-division-update',
  templateUrl: './division-update.component.html',
})
export class DivisionUpdateComponent implements OnInit {
  isSaving = false;

  divisionsSharedCollection: IDivision[] = [];
  superDivisionsSharedCollection: ISuperDivision[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    superDivision: [],
    division: [],
  });

  constructor(
    protected divisionService: DivisionService,
    protected superDivisionService: SuperDivisionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ division }) => {
      this.updateForm(division);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const division = this.createFromForm();
    if (division.id !== undefined) {
      this.subscribeToSaveResponse(this.divisionService.update(division));
    } else {
      this.subscribeToSaveResponse(this.divisionService.create(division));
    }
  }

  trackDivisionById(index: number, item: IDivision): number {
    return item.id!;
  }

  trackSuperDivisionById(index: number, item: ISuperDivision): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDivision>>): void {
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

  protected updateForm(division: IDivision): void {
    this.editForm.patchValue({
      id: division.id,
      nomFr: division.nomFr,
      nomLatin: division.nomLatin,
      superDivision: division.superDivision,
      division: division.division,
    });

    this.divisionsSharedCollection = this.divisionService.addDivisionToCollectionIfMissing(
      this.divisionsSharedCollection,
      division.division
    );
    this.superDivisionsSharedCollection = this.superDivisionService.addSuperDivisionToCollectionIfMissing(
      this.superDivisionsSharedCollection,
      division.superDivision
    );
  }

  protected loadRelationshipsOptions(): void {
    this.divisionService
      .query()
      .pipe(map((res: HttpResponse<IDivision[]>) => res.body ?? []))
      .pipe(
        map((divisions: IDivision[]) =>
          this.divisionService.addDivisionToCollectionIfMissing(divisions, this.editForm.get('division')!.value)
        )
      )
      .subscribe((divisions: IDivision[]) => (this.divisionsSharedCollection = divisions));

    this.superDivisionService
      .query()
      .pipe(map((res: HttpResponse<ISuperDivision[]>) => res.body ?? []))
      .pipe(
        map((superDivisions: ISuperDivision[]) =>
          this.superDivisionService.addSuperDivisionToCollectionIfMissing(superDivisions, this.editForm.get('superDivision')!.value)
        )
      )
      .subscribe((superDivisions: ISuperDivision[]) => (this.superDivisionsSharedCollection = superDivisions));
  }

  protected createFromForm(): IDivision {
    return {
      ...new Division(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      superDivision: this.editForm.get(['superDivision'])!.value,
      division: this.editForm.get(['division'])!.value,
    };
  }
}
