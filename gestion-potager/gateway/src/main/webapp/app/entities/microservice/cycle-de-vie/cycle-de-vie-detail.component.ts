import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICycleDeVie } from 'app/shared/model/microservice/cycle-de-vie.model';

@Component({
  selector: 'gp-cycle-de-vie-detail',
  templateUrl: './cycle-de-vie-detail.component.html',
})
export class CycleDeVieDetailComponent implements OnInit {
  cycleDeVie: ICycleDeVie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cycleDeVie }) => (this.cycleDeVie = cycleDeVie));
  }

  previousState(): void {
    window.history.back();
  }
}
