import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISuperFamille, SuperFamille } from '../super-famille.model';
import { SuperFamilleService } from '../service/super-famille.service';
import { IMicroOrdre } from 'app/entities/classificationMS/micro-ordre/micro-ordre.model';
import { MicroOrdreService } from 'app/entities/classificationMS/micro-ordre/service/micro-ordre.service';

@Component({
  selector: 'perma-super-famille-update',
  templateUrl: './super-famille-update.component.html',
})
export class SuperFamilleUpdateComponent implements OnInit {
  isSaving = false;

  superFamillesSharedCollection: ISuperFamille[] = [];
  microOrdresSharedCollection: IMicroOrdre[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    microOrdre: [],
    superFamille: [],
  });

  constructor(
    protected superFamilleService: SuperFamilleService,
    protected microOrdreService: MicroOrdreService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ superFamille }) => {
      this.updateForm(superFamille);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const superFamille = this.createFromForm();
    if (superFamille.id !== undefined) {
      this.subscribeToSaveResponse(this.superFamilleService.update(superFamille));
    } else {
      this.subscribeToSaveResponse(this.superFamilleService.create(superFamille));
    }
  }

  trackSuperFamilleById(index: number, item: ISuperFamille): number {
    return item.id!;
  }

  trackMicroOrdreById(index: number, item: IMicroOrdre): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISuperFamille>>): void {
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

  protected updateForm(superFamille: ISuperFamille): void {
    this.editForm.patchValue({
      id: superFamille.id,
      nomFr: superFamille.nomFr,
      nomLatin: superFamille.nomLatin,
      microOrdre: superFamille.microOrdre,
      superFamille: superFamille.superFamille,
    });

    this.superFamillesSharedCollection = this.superFamilleService.addSuperFamilleToCollectionIfMissing(
      this.superFamillesSharedCollection,
      superFamille.superFamille
    );
    this.microOrdresSharedCollection = this.microOrdreService.addMicroOrdreToCollectionIfMissing(
      this.microOrdresSharedCollection,
      superFamille.microOrdre
    );
  }

  protected loadRelationshipsOptions(): void {
    this.superFamilleService
      .query()
      .pipe(map((res: HttpResponse<ISuperFamille[]>) => res.body ?? []))
      .pipe(
        map((superFamilles: ISuperFamille[]) =>
          this.superFamilleService.addSuperFamilleToCollectionIfMissing(superFamilles, this.editForm.get('superFamille')!.value)
        )
      )
      .subscribe((superFamilles: ISuperFamille[]) => (this.superFamillesSharedCollection = superFamilles));

    this.microOrdreService
      .query()
      .pipe(map((res: HttpResponse<IMicroOrdre[]>) => res.body ?? []))
      .pipe(
        map((microOrdres: IMicroOrdre[]) =>
          this.microOrdreService.addMicroOrdreToCollectionIfMissing(microOrdres, this.editForm.get('microOrdre')!.value)
        )
      )
      .subscribe((microOrdres: IMicroOrdre[]) => (this.microOrdresSharedCollection = microOrdres));
  }

  protected createFromForm(): ISuperFamille {
    return {
      ...new SuperFamille(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      microOrdre: this.editForm.get(['microOrdre'])!.value,
      superFamille: this.editForm.get(['superFamille'])!.value,
    };
  }
}
