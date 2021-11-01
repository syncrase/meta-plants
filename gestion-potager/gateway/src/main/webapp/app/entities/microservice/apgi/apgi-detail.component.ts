import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAPGI } from 'app/shared/model/microservice/apgi.model';

@Component({
  selector: 'gp-apgi-detail',
  templateUrl: './apgi-detail.component.html',
})
export class APGIDetailComponent implements OnInit {
  aPGI: IAPGI | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGI }) => (this.aPGI = aPGI));
  }

  previousState(): void {
    window.history.back();
  }
}
