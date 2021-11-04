import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAPGII } from '../apgii.model';

@Component({
  selector: 'gp-apgii-detail',
  templateUrl: './apgii-detail.component.html',
})
export class APGIIDetailComponent implements OnInit {
  aPGII: IAPGII | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGII }) => {
      this.aPGII = aPGII;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
