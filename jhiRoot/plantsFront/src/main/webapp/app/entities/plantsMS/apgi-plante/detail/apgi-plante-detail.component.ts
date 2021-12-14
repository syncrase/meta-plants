import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAPGIPlante } from '../apgi-plante.model';

@Component({
  selector: 'perma-apgi-plante-detail',
  templateUrl: './apgi-plante-detail.component.html',
})
export class APGIPlanteDetailComponent implements OnInit {
  aPGIPlante: IAPGIPlante | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGIPlante }) => {
      this.aPGIPlante = aPGIPlante;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
