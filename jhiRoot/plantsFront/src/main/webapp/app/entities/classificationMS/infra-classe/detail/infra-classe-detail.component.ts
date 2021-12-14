import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInfraClasse } from '../infra-classe.model';

@Component({
  selector: 'perma-infra-classe-detail',
  templateUrl: './infra-classe-detail.component.html',
})
export class InfraClasseDetailComponent implements OnInit {
  infraClasse: IInfraClasse | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infraClasse }) => {
      this.infraClasse = infraClasse;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
