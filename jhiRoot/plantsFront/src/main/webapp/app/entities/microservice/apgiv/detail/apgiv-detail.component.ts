import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAPGIV } from '../apgiv.model';

@Component({
  selector: 'perma-apgiv-detail',
  templateUrl: './apgiv-detail.component.html',
})
export class APGIVDetailComponent implements OnInit {
  aPGIV: IAPGIV | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGIV }) => {
      this.aPGIV = aPGIV;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
