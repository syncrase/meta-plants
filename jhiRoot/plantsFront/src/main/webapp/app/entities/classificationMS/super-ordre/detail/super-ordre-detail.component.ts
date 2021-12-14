import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISuperOrdre } from '../super-ordre.model';

@Component({
  selector: 'perma-super-ordre-detail',
  templateUrl: './super-ordre-detail.component.html',
})
export class SuperOrdreDetailComponent implements OnInit {
  superOrdre: ISuperOrdre | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ superOrdre }) => {
      this.superOrdre = superOrdre;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
