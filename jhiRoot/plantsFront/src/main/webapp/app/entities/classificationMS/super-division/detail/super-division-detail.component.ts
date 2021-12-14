import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISuperDivision } from '../super-division.model';

@Component({
  selector: 'perma-super-division-detail',
  templateUrl: './super-division-detail.component.html',
})
export class SuperDivisionDetailComponent implements OnInit {
  superDivision: ISuperDivision | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ superDivision }) => {
      this.superDivision = superDivision;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
