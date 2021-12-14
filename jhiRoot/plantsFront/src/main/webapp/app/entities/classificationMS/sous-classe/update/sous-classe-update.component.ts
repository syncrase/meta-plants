import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISousClasse, SousClasse } from '../sous-classe.model';
import { SousClasseService } from '../service/sous-classe.service';
import { IClasse } from 'app/entities/classificationMS/classe/classe.model';
import { ClasseService } from 'app/entities/classificationMS/classe/service/classe.service';

@Component({
  selector: 'perma-sous-classe-update',
  templateUrl: './sous-classe-update.component.html',
})
export class SousClasseUpdateComponent implements OnInit {
  isSaving = false;

  sousClassesSharedCollection: ISousClasse[] = [];
  classesSharedCollection: IClasse[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    classe: [],
    sousClasse: [],
  });

  constructor(
    protected sousClasseService: SousClasseService,
    protected classeService: ClasseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousClasse }) => {
      this.updateForm(sousClasse);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousClasse = this.createFromForm();
    if (sousClasse.id !== undefined) {
      this.subscribeToSaveResponse(this.sousClasseService.update(sousClasse));
    } else {
      this.subscribeToSaveResponse(this.sousClasseService.create(sousClasse));
    }
  }

  trackSousClasseById(index: number, item: ISousClasse): number {
    return item.id!;
  }

  trackClasseById(index: number, item: IClasse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousClasse>>): void {
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

  protected updateForm(sousClasse: ISousClasse): void {
    this.editForm.patchValue({
      id: sousClasse.id,
      nomFr: sousClasse.nomFr,
      nomLatin: sousClasse.nomLatin,
      classe: sousClasse.classe,
      sousClasse: sousClasse.sousClasse,
    });

    this.sousClassesSharedCollection = this.sousClasseService.addSousClasseToCollectionIfMissing(
      this.sousClassesSharedCollection,
      sousClasse.sousClasse
    );
    this.classesSharedCollection = this.classeService.addClasseToCollectionIfMissing(this.classesSharedCollection, sousClasse.classe);
  }

  protected loadRelationshipsOptions(): void {
    this.sousClasseService
      .query()
      .pipe(map((res: HttpResponse<ISousClasse[]>) => res.body ?? []))
      .pipe(
        map((sousClasses: ISousClasse[]) =>
          this.sousClasseService.addSousClasseToCollectionIfMissing(sousClasses, this.editForm.get('sousClasse')!.value)
        )
      )
      .subscribe((sousClasses: ISousClasse[]) => (this.sousClassesSharedCollection = sousClasses));

    this.classeService
      .query()
      .pipe(map((res: HttpResponse<IClasse[]>) => res.body ?? []))
      .pipe(map((classes: IClasse[]) => this.classeService.addClasseToCollectionIfMissing(classes, this.editForm.get('classe')!.value)))
      .subscribe((classes: IClasse[]) => (this.classesSharedCollection = classes));
  }

  protected createFromForm(): ISousClasse {
    return {
      ...new SousClasse(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      classe: this.editForm.get(['classe'])!.value,
      sousClasse: this.editForm.get(['sousClasse'])!.value,
    };
  }
}
