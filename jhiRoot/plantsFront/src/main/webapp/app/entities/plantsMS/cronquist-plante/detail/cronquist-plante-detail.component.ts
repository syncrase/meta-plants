import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICronquistPlante } from '../cronquist-plante.model';

@Component({
  selector: 'perma-cronquist-plante-detail',
  templateUrl: './cronquist-plante-detail.component.html',
})
export class CronquistPlanteDetailComponent implements OnInit {
  cronquistPlante: ICronquistPlante | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cronquistPlante }) => {
      this.cronquistPlante = cronquistPlante;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
