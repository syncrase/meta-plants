import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAPGIII } from '../apgiii.model';

@Component({
  selector: 'perma-apgiii-detail',
  templateUrl: './apgiii-detail.component.html',
})
export class APGIIIDetailComponent implements OnInit {
  aPGIII: IAPGIII | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGIII }) => {
      this.aPGIII = aPGIII;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
