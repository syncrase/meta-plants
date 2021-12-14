import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISuperFamille } from '../super-famille.model';

@Component({
  selector: 'perma-super-famille-detail',
  templateUrl: './super-famille-detail.component.html',
})
export class SuperFamilleDetailComponent implements OnInit {
  superFamille: ISuperFamille | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ superFamille }) => {
      this.superFamille = superFamille;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
