import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassification } from 'app/shared/model/microservice/classification.model';

@Component({
  selector: 'gp-classification-detail',
  templateUrl: './classification-detail.component.html',
})
export class ClassificationDetailComponent implements OnInit {
  classification: IClassification | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classification }) => (this.classification = classification));
  }

  previousState(): void {
    window.history.back();
  }
}