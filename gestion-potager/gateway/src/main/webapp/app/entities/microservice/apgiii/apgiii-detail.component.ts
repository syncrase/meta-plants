import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAPGIII } from 'app/shared/model/microservice/apgiii.model';

@Component({
  selector: 'gp-apgiii-detail',
  templateUrl: './apgiii-detail.component.html',
})
export class APGIIIDetailComponent implements OnInit {
  aPGIII: IAPGIII | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGIII }) => (this.aPGIII = aPGIII));
  }

  previousState(): void {
    window.history.back();
  }
}
