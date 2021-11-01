import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRessemblance } from 'app/shared/model/microservice/ressemblance.model';

@Component({
  selector: 'gp-ressemblance-detail',
  templateUrl: './ressemblance-detail.component.html',
})
export class RessemblanceDetailComponent implements OnInit {
  ressemblance: IRessemblance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ressemblance }) => (this.ressemblance = ressemblance));
  }

  previousState(): void {
    window.history.back();
  }
}
