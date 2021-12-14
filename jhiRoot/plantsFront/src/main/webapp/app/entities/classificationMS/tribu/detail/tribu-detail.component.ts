import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITribu } from '../tribu.model';

@Component({
  selector: 'perma-tribu-detail',
  templateUrl: './tribu-detail.component.html',
})
export class TribuDetailComponent implements OnInit {
  tribu: ITribu | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tribu }) => {
      this.tribu = tribu;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
