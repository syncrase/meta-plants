import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISousDivision } from '../sous-division.model';

@Component({
  selector: 'perma-sous-division-detail',
  templateUrl: './sous-division-detail.component.html',
})
export class SousDivisionDetailComponent implements OnInit {
  sousDivision: ISousDivision | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousDivision }) => {
      this.sousDivision = sousDivision;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
