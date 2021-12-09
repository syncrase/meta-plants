import { Component, OnInit } from "@angular/core";
import { HttpHeaders, HttpResponse } from "@angular/common/http";
import { ActivatedRoute, Router } from "@angular/router";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from "app/config/pagination.constants";
import { IPlante } from "../../../entities/microservice/plante/plante.model";
import { PlanteService } from "../../../entities/microservice/plante/service/plante.service";
import { INomVernaculaire } from "../../../entities/microservice/nom-vernaculaire/nom-vernaculaire.model";
import { filter, map, mergeMap, tap } from "rxjs/operators";
// import { flatMap } from "rxjs/internal/operators";
import { combineLatest, EMPTY } from "rxjs";
import {
  NomVernaculaireService
} from "../../../entities/microservice/nom-vernaculaire/service/nom-vernaculaire.service";
import { CycleDeVieService } from "../../../entities/microservice/cycle-de-vie/service/cycle-de-vie.service";
import { ICycleDeVie } from "../../../entities/microservice/cycle-de-vie/cycle-de-vie.model";
import { SemisService } from "../../../entities/microservice/semis/service/semis.service";
import { ISemis } from "../../../entities/microservice/semis/semis.model";
import { PeriodeAnneeService } from "../../../entities/microservice/periode-annee/service/periode-annee.service";
import { IPeriodeAnnee } from "../../../entities/microservice/periode-annee/periode-annee.model";


@Component({
  selector: "perma-plante",
  templateUrl: "./semis.component.html"
})
export class SemisComponent implements OnInit {
  plantes?: IPlante[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected planteService: PlanteService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected nomVernaculaireService: NomVernaculaireService,
    protected cycleDeVieService: CycleDeVieService,
    protected semisService: SemisService,
    protected periodeAnneeService: PeriodeAnneeService
  ) {
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.planteService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IPlante[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: IPlante): number {
    return item.id!;
  }

  protected sort(): string[] {
    const result = [this.predicate + "," + (this.ascending ? ASC : DESC)];
    if (this.predicate !== "id") {
      result.push("id");
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get("page");
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data["defaultSort"]).split(",");
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(plantes: IPlante[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get("X-Total-Count"));
    this.page = page;
    if (navigate) {
      this.router.navigate(["/tableausemis"], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + "," + (this.ascending ? ASC : DESC)
        }
      });
    }
    this.plantes = plantes ?? [];
    // Load nested data


    this.plantes.forEach(plant => {
      this.nomVernaculaireService
        .query({
          "plantesId.equals": plant.id
        })
        .pipe(map((res: HttpResponse<INomVernaculaire[]>) => res.body ?? []))
        // Deleted pipe, cf plante-update.component.ts
        .subscribe((nomVernaculaires: INomVernaculaire[]) => (plant.nomsVernaculaires = nomVernaculaires));

      this.semisService
        .find(9982).subscribe(sem => {
        // eslint-disable-next-line no-debugger
        debugger;
        // eslint-disable-next-line no-console
        console.log(sem);
      });
      if (plant.cycleDeVie?.id != null) {
        this.cycleDeVieService.find(plant.cycleDeVie.id)
          .pipe(
            map((res: HttpResponse<ICycleDeVie>) => {
              // eslint-disable-next-line no-debugger
              debugger;
              plant.cycleDeVie = res.body;
              return res.body ?? {};
            }),
            mergeMap((cycleDeVie: ICycleDeVie) => {
                if (cycleDeVie.semis?.id != null) {
                  return this.semisService
                    .find(cycleDeVie.semis.id)
                    .pipe(
                      map((semis: HttpResponse<ISemis>) => semis.body ?? {}),
                      filter(semis => !!semis),
                      tap((semis: ISemis) => {
                        if (plant.cycleDeVie?.semis != null) {
                          plant.cycleDeVie.semis = semis;
                        }
                      })
                    );
                }
                return EMPTY;
              }
            )
          )
          .subscribe((semis: any) => {
            this.periodeAnneeService
              .find(semis.semisPleineTerre.id)
              .subscribe((periodeAnnee: HttpResponse<IPeriodeAnnee>) => {
                if (plant.cycleDeVie?.semis?.semisPleineTerre?.id != null) {
                  plant.cycleDeVie.semis.semisPleineTerre = periodeAnnee.body;
                }
                return periodeAnnee.body ?? {};
              });
            this.periodeAnneeService
              .find(semis.semisSousAbris.id)
              .subscribe((periodeAnnee: HttpResponse<IPeriodeAnnee>) => {
                if (plant.cycleDeVie?.semis?.semisSousAbris?.id != null) {
                  plant.cycleDeVie.semis.semisSousAbris = periodeAnnee.body;
                }
                return periodeAnnee.body ?? {};
              });
          });
      }

    });

    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering,@typescript-eslint/explicit-function-return-type
  getNomVernaculaire(plante: IPlante) {
    if (plante.nomsVernaculaires) {
      return plante.nomsVernaculaires.map(nv => nv.nom).join(",");
    } else {
      return "no name";
    }
  }
}
