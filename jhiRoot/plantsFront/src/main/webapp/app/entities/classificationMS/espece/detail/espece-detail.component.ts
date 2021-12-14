import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEspece } from '../espece.model';

@Component({
  selector: 'perma-espece-detail',
  templateUrl: './espece-detail.component.html',
})
export class EspeceDetailComponent implements OnInit {
  espece: IEspece | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ espece }) => {
      this.espece = espece;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
