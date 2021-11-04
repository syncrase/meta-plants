import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPeriodeAnnee, PeriodeAnnee } from '../periode-annee.model';
import { PeriodeAnneeService } from '../service/periode-annee.service';
import { IMois } from 'app/entities/microservice/mois/mois.model';
import { MoisService } from 'app/entities/microservice/mois/service/mois.service';

@Component({
  selector: 'gp-periode-annee-update',
  templateUrl: './periode-annee-update.component.html',
})
export class PeriodeAnneeUpdateComponent implements OnInit {
  isSaving = false;

  debutsCollection: IMois[] = [];
  finsCollection: IMois[] = [];

  editForm = this.fb.group({
    id: [],
    debut: [null, Validators.required],
    fin: [null, Validators.required],
  });

  constructor(
    protected periodeAnneeService: PeriodeAnneeService,
    protected moisService: MoisService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ periodeAnnee }) => {
      this.updateForm(periodeAnnee);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const periodeAnnee = this.createFromForm();
    if (periodeAnnee.id !== undefined) {
      this.subscribeToSaveResponse(this.periodeAnneeService.update(periodeAnnee));
    } else {
      this.subscribeToSaveResponse(this.periodeAnneeService.create(periodeAnnee));
    }
  }

  trackMoisById(index: number, item: IMois): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeriodeAnnee>>): void {
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

  protected updateForm(periodeAnnee: IPeriodeAnnee): void {
    this.editForm.patchValue({
      id: periodeAnnee.id,
      debut: periodeAnnee.debut,
      fin: periodeAnnee.fin,
    });

    this.debutsCollection = this.moisService.addMoisToCollectionIfMissing(this.debutsCollection, periodeAnnee.debut);
    this.finsCollection = this.moisService.addMoisToCollectionIfMissing(this.finsCollection, periodeAnnee.fin);
  }

  protected loadRelationshipsOptions(): void {
    this.moisService
      .query({ filter: 'periodeannee-is-null' })
      .pipe(map((res: HttpResponse<IMois[]>) => res.body ?? []))
      .pipe(map((mois: IMois[]) => this.moisService.addMoisToCollectionIfMissing(mois, this.editForm.get('debut')!.value)))
      .subscribe((mois: IMois[]) => (this.debutsCollection = mois));

    this.moisService
      .query({ filter: 'periodeannee-is-null' })
      .pipe(map((res: HttpResponse<IMois[]>) => res.body ?? []))
      .pipe(map((mois: IMois[]) => this.moisService.addMoisToCollectionIfMissing(mois, this.editForm.get('fin')!.value)))
      .subscribe((mois: IMois[]) => (this.finsCollection = mois));
  }

  protected createFromForm(): IPeriodeAnnee {
    return {
      ...new PeriodeAnnee(),
      id: this.editForm.get(['id'])!.value,
      debut: this.editForm.get(['debut'])!.value,
      fin: this.editForm.get(['fin'])!.value,
    };
  }
}
