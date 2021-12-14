import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRameau } from '../rameau.model';

@Component({
  selector: 'perma-rameau-detail',
  templateUrl: './rameau-detail.component.html',
})
export class RameauDetailComponent implements OnInit {
  rameau: IRameau | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rameau }) => {
      this.rameau = rameau;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
