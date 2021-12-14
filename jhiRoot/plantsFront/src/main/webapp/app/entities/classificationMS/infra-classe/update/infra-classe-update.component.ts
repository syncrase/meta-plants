import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInfraClasse, InfraClasse } from '../infra-classe.model';
import { InfraClasseService } from '../service/infra-classe.service';
import { ISousClasse } from 'app/entities/classificationMS/sous-classe/sous-classe.model';
import { SousClasseService } from 'app/entities/classificationMS/sous-classe/service/sous-classe.service';

@Component({
  selector: 'perma-infra-classe-update',
  templateUrl: './infra-classe-update.component.html',
})
export class InfraClasseUpdateComponent implements OnInit {
  isSaving = false;

  infraClassesSharedCollection: IInfraClasse[] = [];
  sousClassesSharedCollection: ISousClasse[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    sousClasse: [],
    infraClasse: [],
  });

  constructor(
    protected infraClasseService: InfraClasseService,
    protected sousClasseService: SousClasseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infraClasse }) => {
      this.updateForm(infraClasse);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const infraClasse = this.createFromForm();
    if (infraClasse.id !== undefined) {
      this.subscribeToSaveResponse(this.infraClasseService.update(infraClasse));
    } else {
      this.subscribeToSaveResponse(this.infraClasseService.create(infraClasse));
    }
  }

  trackInfraClasseById(index: number, item: IInfraClasse): number {
    return item.id!;
  }

  trackSousClasseById(index: number, item: ISousClasse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInfraClasse>>): void {
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

  protected updateForm(infraClasse: IInfraClasse): void {
    this.editForm.patchValue({
      id: infraClasse.id,
      nomFr: infraClasse.nomFr,
      nomLatin: infraClasse.nomLatin,
      sousClasse: infraClasse.sousClasse,
      infraClasse: infraClasse.infraClasse,
    });

    this.infraClassesSharedCollection = this.infraClasseService.addInfraClasseToCollectionIfMissing(
      this.infraClassesSharedCollection,
      infraClasse.infraClasse
    );
    this.sousClassesSharedCollection = this.sousClasseService.addSousClasseToCollectionIfMissing(
      this.sousClassesSharedCollection,
      infraClasse.sousClasse
    );
  }

  protected loadRelationshipsOptions(): void {
    this.infraClasseService
      .query()
      .pipe(map((res: HttpResponse<IInfraClasse[]>) => res.body ?? []))
      .pipe(
        map((infraClasses: IInfraClasse[]) =>
          this.infraClasseService.addInfraClasseToCollectionIfMissing(infraClasses, this.editForm.get('infraClasse')!.value)
        )
      )
      .subscribe((infraClasses: IInfraClasse[]) => (this.infraClassesSharedCollection = infraClasses));

    this.sousClasseService
      .query()
      .pipe(map((res: HttpResponse<ISousClasse[]>) => res.body ?? []))
      .pipe(
        map((sousClasses: ISousClasse[]) =>
          this.sousClasseService.addSousClasseToCollectionIfMissing(sousClasses, this.editForm.get('sousClasse')!.value)
        )
      )
      .subscribe((sousClasses: ISousClasse[]) => (this.sousClassesSharedCollection = sousClasses));
  }

  protected createFromForm(): IInfraClasse {
    return {
      ...new InfraClasse(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      sousClasse: this.editForm.get(['sousClasse'])!.value,
      infraClasse: this.editForm.get(['infraClasse'])!.value,
    };
  }
}
