import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INomVernaculaire } from '../nom-vernaculaire.model';

@Component({
  selector: 'gp-nom-vernaculaire-detail',
  templateUrl: './nom-vernaculaire-detail.component.html',
})
export class NomVernaculaireDetailComponent implements OnInit {
  nomVernaculaire: INomVernaculaire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nomVernaculaire }) => {
      this.nomVernaculaire = nomVernaculaire;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
