import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInfraEmbranchement } from '../infra-embranchement.model';

@Component({
  selector: 'perma-infra-embranchement-detail',
  templateUrl: './infra-embranchement-detail.component.html',
})
export class InfraEmbranchementDetailComponent implements OnInit {
  infraEmbranchement: IInfraEmbranchement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infraEmbranchement }) => {
      this.infraEmbranchement = infraEmbranchement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
