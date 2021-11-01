import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISemis } from 'app/shared/model/microservice/semis.model';

@Component({
  selector: 'gp-semis-detail',
  templateUrl: './semis-detail.component.html',
})
export class SemisDetailComponent implements OnInit {
  semis: ISemis | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ semis }) => (this.semis = semis));
  }

  previousState(): void {
    window.history.back();
  }
}
