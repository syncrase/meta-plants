import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRameau, Rameau } from '../rameau.model';
import { RameauService } from '../service/rameau.service';
import { ISousRegne } from 'app/entities/classificationMS/sous-regne/sous-regne.model';
import { SousRegneService } from 'app/entities/classificationMS/sous-regne/service/sous-regne.service';

@Component({
  selector: 'perma-rameau-update',
  templateUrl: './rameau-update.component.html',
})
export class RameauUpdateComponent implements OnInit {
  isSaving = false;

  rameausSharedCollection: IRameau[] = [];
  sousRegnesSharedCollection: ISousRegne[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    sousRegne: [],
    rameau: [],
  });

  constructor(
    protected rameauService: RameauService,
    protected sousRegneService: SousRegneService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rameau }) => {
      this.updateForm(rameau);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rameau = this.createFromForm();
    if (rameau.id !== undefined) {
      this.subscribeToSaveResponse(this.rameauService.update(rameau));
    } else {
      this.subscribeToSaveResponse(this.rameauService.create(rameau));
    }
  }

  trackRameauById(index: number, item: IRameau): number {
    return item.id!;
  }

  trackSousRegneById(index: number, item: ISousRegne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRameau>>): void {
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

  protected updateForm(rameau: IRameau): void {
    this.editForm.patchValue({
      id: rameau.id,
      nomFr: rameau.nomFr,
      nomLatin: rameau.nomLatin,
      sousRegne: rameau.sousRegne,
      rameau: rameau.rameau,
    });

    this.rameausSharedCollection = this.rameauService.addRameauToCollectionIfMissing(this.rameausSharedCollection, rameau.rameau);
    this.sousRegnesSharedCollection = this.sousRegneService.addSousRegneToCollectionIfMissing(
      this.sousRegnesSharedCollection,
      rameau.sousRegne
    );
  }

  protected loadRelationshipsOptions(): void {
    this.rameauService
      .query()
      .pipe(map((res: HttpResponse<IRameau[]>) => res.body ?? []))
      .pipe(map((rameaus: IRameau[]) => this.rameauService.addRameauToCollectionIfMissing(rameaus, this.editForm.get('rameau')!.value)))
      .subscribe((rameaus: IRameau[]) => (this.rameausSharedCollection = rameaus));

    this.sousRegneService
      .query()
      .pipe(map((res: HttpResponse<ISousRegne[]>) => res.body ?? []))
      .pipe(
        map((sousRegnes: ISousRegne[]) =>
          this.sousRegneService.addSousRegneToCollectionIfMissing(sousRegnes, this.editForm.get('sousRegne')!.value)
        )
      )
      .subscribe((sousRegnes: ISousRegne[]) => (this.sousRegnesSharedCollection = sousRegnes));
  }

  protected createFromForm(): IRameau {
    return {
      ...new Rameau(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      sousRegne: this.editForm.get(['sousRegne'])!.value,
      rameau: this.editForm.get(['rameau'])!.value,
    };
  }
}
