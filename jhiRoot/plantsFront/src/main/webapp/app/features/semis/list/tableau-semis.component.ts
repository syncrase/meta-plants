import { Component, OnInit } from "@angular/core";
import { HttpHeaders, HttpResponse } from "@angular/common/http";
import { ActivatedRoute, Router } from "@angular/router";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from "app/config/pagination.constants";
import { filter, map, mergeMap, tap } from "rxjs/operators";
// import { flatMap } from "rxjs/internal/operators";
import { combineLatest, EMPTY, Observable } from "rxjs";
import { IPlante } from "../../../entities/plantsMS/plante/plante.model";
import { IMois } from "../../../entities/plantsMS/mois/mois.model";
import { PlanteService } from "../../../entities/plantsMS/plante/service/plante.service";
import { NomVernaculaireService } from "../../../entities/plantsMS/nom-vernaculaire/service/nom-vernaculaire.service";
import { CycleDeVieService } from "../../../entities/plantsMS/cycle-de-vie/service/cycle-de-vie.service";
import { SemisService } from "../../../entities/plantsMS/semis/service/semis.service";
import { PeriodeAnneeService } from "../../../entities/plantsMS/periode-annee/service/periode-annee.service";
import { MoisService } from "../../../entities/plantsMS/mois/service/mois.service";
import { INomVernaculaire } from "../../../entities/plantsMS/nom-vernaculaire/nom-vernaculaire.model";
import { ICycleDeVie } from "../../../entities/plantsMS/cycle-de-vie/cycle-de-vie.model";
import { ISemis } from "../../../entities/plantsMS/semis/semis.model";
import { IPeriodeAnnee } from "../../../entities/plantsMS/periode-annee/periode-annee.model";


@Component({
  selector: "perma-plante",
  templateUrl: "./tableau-semis.component.html",
  styleUrls: ["./tableau-semis.component.css"]
})
export class TableauSemisComponent implements OnInit {
  plantes?: IPlante[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  mois$!: Observable<IMois[]>;

  constructor(
    protected planteService: PlanteService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected nomVernaculaireService: NomVernaculaireService,
    protected cycleDeVieService: CycleDeVieService,
    protected semisService: SemisService,
    protected periodeAnneeService: PeriodeAnneeService,
    protected moisService: MoisService
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
    // TODO Problème, l'appel est fait pour tous les subscribers
    this.mois$ = this.moisService.query({
      page: 0,
      size: 24
    }).pipe(map((res: HttpResponse<IMois[]>) => res.body ?? []));
    this.handleNavigation();
  }

  trackId(index: number, item: IPlante): number {
    return item.id!;
  }

  getNomVernaculaire(plante: IPlante): string {
    if (plante.nomsVernaculaires) {
      return plante.nomsVernaculaires.map(nv => nv.nom).join(",");
    } else {
      return "no name";
    }
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
    this.plantes.forEach((plant) => {
      this.nomVernaculaireService
        .query({
          "plantesId.equals": plant.id
        })
        .pipe(map((res: HttpResponse<INomVernaculaire[]>) => res.body ?? []))
        // Deleted pipe, cf plante-update.component.ts
        .subscribe((nomVernaculaires: INomVernaculaire[]) => (plant.nomsVernaculaires = nomVernaculaires));

      if (plant.cycleDeVie?.id != null) {
        this.cycleDeVieService.find(plant.cycleDeVie.id)
          .pipe(
            map((res: HttpResponse<ICycleDeVie>) => {
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
            if (semis?.semisPleineTerre?.id) {
              this.periodeAnneeService.find(semis.semisPleineTerre.id).subscribe((periodeAnnee: HttpResponse<IPeriodeAnnee>) => {
                if (plant.cycleDeVie?.semis?.semisPleineTerre?.id != null) {
                  plant.cycleDeVie.semis.semisPleineTerre = periodeAnnee.body;
                }
                return periodeAnnee.body ?? {};
              });
            }
            if (semis?.semisSousAbris?.id) {
              this.periodeAnneeService.find(semis.semisSousAbris.id).subscribe((periodeAnnee: HttpResponse<IPeriodeAnnee>) => {
                if (plant.cycleDeVie?.semis?.semisSousAbris?.id != null) {
                  plant.cycleDeVie.semis.semisSousAbris = periodeAnnee.body;
                }
                return periodeAnnee.body ?? {};
              });
            }
          });
      }

    });

    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

}
