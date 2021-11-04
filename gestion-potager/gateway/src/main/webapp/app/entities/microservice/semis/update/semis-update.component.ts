import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISemis, Semis } from '../semis.model';
import { SemisService } from '../service/semis.service';
import { IPeriodeAnnee } from 'app/entities/microservice/periode-annee/periode-annee.model';
import { PeriodeAnneeService } from 'app/entities/microservice/periode-annee/service/periode-annee.service';
import { ITypeSemis } from 'app/entities/microservice/type-semis/type-semis.model';
import { TypeSemisService } from 'app/entities/microservice/type-semis/service/type-semis.service';
import { IGermination } from 'app/entities/microservice/germination/germination.model';
import { GerminationService } from 'app/entities/microservice/germination/service/germination.service';

@Component({
  selector: 'gp-semis-update',
  templateUrl: './semis-update.component.html',
})
export class SemisUpdateComponent implements OnInit {
  isSaving = false;

  semisPleineTerresCollection: IPeriodeAnnee[] = [];
  semisSousAbrisesCollection: IPeriodeAnnee[] = [];
  typeSemisCollection: ITypeSemis[] = [];
  germinationsCollection: IGermination[] = [];

  editForm = this.fb.group({
    id: [],
    semisPleineTerre: [],
    semisSousAbris: [],
    typeSemis: [],
    germination: [],
  });

  constructor(
    protected semisService: SemisService,
    protected periodeAnneeService: PeriodeAnneeService,
    protected typeSemisService: TypeSemisService,
    protected germinationService: GerminationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ semis }) => {
      this.updateForm(semis);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const semis = this.createFromForm();
    if (semis.id !== undefined) {
      this.subscribeToSaveResponse(this.semisService.update(semis));
    } else {
      this.subscribeToSaveResponse(this.semisService.create(semis));
    }
  }

  trackPeriodeAnneeById(index: number, item: IPeriodeAnnee): number {
    return item.id!;
  }

  trackTypeSemisById(index: number, item: ITypeSemis): number {
    return item.id!;
  }

  trackGerminationById(index: number, item: IGermination): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISemis>>): void {
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

  protected updateForm(semis: ISemis): void {
    this.editForm.patchValue({
      id: semis.id,
      semisPleineTerre: semis.semisPleineTerre,
      semisSousAbris: semis.semisSousAbris,
      typeSemis: semis.typeSemis,
      germination: semis.germination,
    });

    this.semisPleineTerresCollection = this.periodeAnneeService.addPeriodeAnneeToCollectionIfMissing(
      this.semisPleineTerresCollection,
      semis.semisPleineTerre
    );
    this.semisSousAbrisesCollection = this.periodeAnneeService.addPeriodeAnneeToCollectionIfMissing(
      this.semisSousAbrisesCollection,
      semis.semisSousAbris
    );
    this.typeSemisCollection = this.typeSemisService.addTypeSemisToCollectionIfMissing(this.typeSemisCollection, semis.typeSemis);
    this.germinationsCollection = this.germinationService.addGerminationToCollectionIfMissing(
      this.germinationsCollection,
      semis.germination
    );
  }

  protected loadRelationshipsOptions(): void {
    this.periodeAnneeService
      .query({ filter: 'semis-is-null' })
      .pipe(map((res: HttpResponse<IPeriodeAnnee[]>) => res.body ?? []))
      .pipe(
        map((periodeAnnees: IPeriodeAnnee[]) =>
          this.periodeAnneeService.addPeriodeAnneeToCollectionIfMissing(periodeAnnees, this.editForm.get('semisPleineTerre')!.value)
        )
      )
      .subscribe((periodeAnnees: IPeriodeAnnee[]) => (this.semisPleineTerresCollection = periodeAnnees));

    this.periodeAnneeService
      .query({ filter: 'semis-is-null' })
      .pipe(map((res: HttpResponse<IPeriodeAnnee[]>) => res.body ?? []))
      .pipe(
        map((periodeAnnees: IPeriodeAnnee[]) =>
          this.periodeAnneeService.addPeriodeAnneeToCollectionIfMissing(periodeAnnees, this.editForm.get('semisSousAbris')!.value)
        )
      )
      .subscribe((periodeAnnees: IPeriodeAnnee[]) => (this.semisSousAbrisesCollection = periodeAnnees));

    this.typeSemisService
      .query({ filter: 'semis-is-null' })
      .pipe(map((res: HttpResponse<ITypeSemis[]>) => res.body ?? []))
      .pipe(
        map((typeSemis: ITypeSemis[]) =>
          this.typeSemisService.addTypeSemisToCollectionIfMissing(typeSemis, this.editForm.get('typeSemis')!.value)
        )
      )
      .subscribe((typeSemis: ITypeSemis[]) => (this.typeSemisCollection = typeSemis));

    this.germinationService
      .query({ filter: 'semis-is-null' })
      .pipe(map((res: HttpResponse<IGermination[]>) => res.body ?? []))
      .pipe(
        map((germinations: IGermination[]) =>
          this.germinationService.addGerminationToCollectionIfMissing(germinations, this.editForm.get('germination')!.value)
        )
      )
      .subscribe((germinations: IGermination[]) => (this.germinationsCollection = germinations));
  }

  protected createFromForm(): ISemis {
    return {
      ...new Semis(),
      id: this.editForm.get(['id'])!.value,
      semisPleineTerre: this.editForm.get(['semisPleineTerre'])!.value,
      semisSousAbris: this.editForm.get(['semisSousAbris'])!.value,
      typeSemis: this.editForm.get(['typeSemis'])!.value,
      germination: this.editForm.get(['germination'])!.value,
    };
  }
}
