import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAPGIIPlante } from '../apgii-plante.model';

@Component({
  selector: 'perma-apgii-plante-detail',
  templateUrl: './apgii-plante-detail.component.html',
})
export class APGIIPlanteDetailComponent implements OnInit {
  aPGIIPlante: IAPGIIPlante | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGIIPlante }) => {
      this.aPGIIPlante = aPGIIPlante;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
