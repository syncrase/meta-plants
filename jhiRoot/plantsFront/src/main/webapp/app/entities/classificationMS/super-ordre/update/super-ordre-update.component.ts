import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISuperOrdre, SuperOrdre } from '../super-ordre.model';
import { SuperOrdreService } from '../service/super-ordre.service';
import { IInfraClasse } from 'app/entities/classificationMS/infra-classe/infra-classe.model';
import { InfraClasseService } from 'app/entities/classificationMS/infra-classe/service/infra-classe.service';

@Component({
  selector: 'perma-super-ordre-update',
  templateUrl: './super-ordre-update.component.html',
})
export class SuperOrdreUpdateComponent implements OnInit {
  isSaving = false;

  superOrdresSharedCollection: ISuperOrdre[] = [];
  infraClassesSharedCollection: IInfraClasse[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    infraClasse: [],
    superOrdre: [],
  });

  constructor(
    protected superOrdreService: SuperOrdreService,
    protected infraClasseService: InfraClasseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ superOrdre }) => {
      this.updateForm(superOrdre);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const superOrdre = this.createFromForm();
    if (superOrdre.id !== undefined) {
      this.subscribeToSaveResponse(this.superOrdreService.update(superOrdre));
    } else {
      this.subscribeToSaveResponse(this.superOrdreService.create(superOrdre));
    }
  }

  trackSuperOrdreById(index: number, item: ISuperOrdre): number {
    return item.id!;
  }

  trackInfraClasseById(index: number, item: IInfraClasse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISuperOrdre>>): void {
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

  protected updateForm(superOrdre: ISuperOrdre): void {
    this.editForm.patchValue({
      id: superOrdre.id,
      nomFr: superOrdre.nomFr,
      nomLatin: superOrdre.nomLatin,
      infraClasse: superOrdre.infraClasse,
      superOrdre: superOrdre.superOrdre,
    });

    this.superOrdresSharedCollection = this.superOrdreService.addSuperOrdreToCollectionIfMissing(
      this.superOrdresSharedCollection,
      superOrdre.superOrdre
    );
    this.infraClassesSharedCollection = this.infraClasseService.addInfraClasseToCollectionIfMissing(
      this.infraClassesSharedCollection,
      superOrdre.infraClasse
    );
  }

  protected loadRelationshipsOptions(): void {
    this.superOrdreService
      .query()
      .pipe(map((res: HttpResponse<ISuperOrdre[]>) => res.body ?? []))
      .pipe(
        map((superOrdres: ISuperOrdre[]) =>
          this.superOrdreService.addSuperOrdreToCollectionIfMissing(superOrdres, this.editForm.get('superOrdre')!.value)
        )
      )
      .subscribe((superOrdres: ISuperOrdre[]) => (this.superOrdresSharedCollection = superOrdres));

    this.infraClasseService
      .query()
      .pipe(map((res: HttpResponse<IInfraClasse[]>) => res.body ?? []))
      .pipe(
        map((infraClasses: IInfraClasse[]) =>
          this.infraClasseService.addInfraClasseToCollectionIfMissing(infraClasses, this.editForm.get('infraClasse')!.value)
        )
      )
      .subscribe((infraClasses: IInfraClasse[]) => (this.infraClassesSharedCollection = infraClasses));
  }

  protected createFromForm(): ISuperOrdre {
    return {
      ...new SuperOrdre(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      infraClasse: this.editForm.get(['infraClasse'])!.value,
      superOrdre: this.editForm.get(['superOrdre'])!.value,
    };
  }
}
