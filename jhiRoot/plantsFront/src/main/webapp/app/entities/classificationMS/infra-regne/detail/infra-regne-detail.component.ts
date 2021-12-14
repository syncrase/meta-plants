import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInfraRegne } from '../infra-regne.model';

@Component({
  selector: 'perma-infra-regne-detail',
  templateUrl: './infra-regne-detail.component.html',
})
export class InfraRegneDetailComponent implements OnInit {
  infraRegne: IInfraRegne | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infraRegne }) => {
      this.infraRegne = infraRegne;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
