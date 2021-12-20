import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassificationCronquist } from '../classification-cronquist.model';

@Component({
  selector: 'perma-classification-cronquist-detail',
  templateUrl: './classification-cronquist-detail.component.html',
})
export class ClassificationCronquistDetailComponent implements OnInit {
  classificationCronquist: IClassificationCronquist | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classificationCronquist }) => {
      this.classificationCronquist = classificationCronquist;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
