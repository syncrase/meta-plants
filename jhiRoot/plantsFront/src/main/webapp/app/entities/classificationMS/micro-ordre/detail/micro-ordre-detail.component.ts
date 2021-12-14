import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMicroOrdre } from '../micro-ordre.model';

@Component({
  selector: 'perma-micro-ordre-detail',
  templateUrl: './micro-ordre-detail.component.html',
})
export class MicroOrdreDetailComponent implements OnInit {
  microOrdre: IMicroOrdre | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ microOrdre }) => {
      this.microOrdre = microOrdre;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
