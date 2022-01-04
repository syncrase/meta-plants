import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassificationNom } from '../classification-nom.model';

@Component({
  selector: 'perma-classification-nom-detail',
  templateUrl: './classification-nom-detail.component.html',
})
export class ClassificationNomDetailComponent implements OnInit {
  classificationNom: IClassificationNom | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classificationNom }) => {
      this.classificationNom = classificationNom;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
