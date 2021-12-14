import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAPGIIIPlante } from '../apgiii-plante.model';

@Component({
  selector: 'perma-apgiii-plante-detail',
  templateUrl: './apgiii-plante-detail.component.html',
})
export class APGIIIPlanteDetailComponent implements OnInit {
  aPGIIIPlante: IAPGIIIPlante | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGIIIPlante }) => {
      this.aPGIIIPlante = aPGIIIPlante;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
