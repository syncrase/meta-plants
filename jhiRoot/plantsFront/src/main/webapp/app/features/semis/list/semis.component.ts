import { Component, OnInit } from "@angular/core";
import { HttpHeaders, HttpResponse } from "@angular/common/http";
import { ActivatedRoute, Router } from "@angular/router";
import { combineLatest } from "rxjs";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from "app/config/pagination.constants";
import { IPlante, Plante } from "../../../entities/microservice/plante/plante.model";
import { PlanteService } from "../../../entities/microservice/plante/service/plante.service";

const PLANTES: Plante[] = [
  // {
  //   nomsVernaculaires: [{ nom: "Chou cabus" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 1 }, fin: { numero: 3 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Poireau" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 4 } },
  //       semisSousAbris: { debut: { numero: 0 }, fin: { numero: 0 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Pourpier" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 5 }, fin: { numero: 8 } },
  //       semisSousAbris: { debut: { numero: 1 }, fin: { numero: 4 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Radis 18 jour" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 9 }, fin: { numero: 3 } },
  //       semisSousAbris: { debut: { numero: 1 }, fin: { numero: 3 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Chou cabus coeur de boeuf des vertus" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 10 }, fin: { numero: 3 } },
  //       semisSousAbris: { debut: { numero: 1 }, fin: { numero: 3 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Chou-fleur merveille de toutes saisons" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 4 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 1 }, fin: { numero: 2 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Laitue rouge grenobloise" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 4 }, fin: { numero: 9 } },
  //       semisSousAbris: { debut: { numero: 2 }, fin: { numero: 3 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Laitue reine de mai de pleine terre" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 4 } },
  //       semisSousAbris: { debut: { numero: 12 }, fin: { numero: 2 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Laitue batavia lollo rossa" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 9 } },
  //       semisSousAbris: { debut: { numero: 1 }, fin: { numero: 2 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Radis kocto HF1" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 4 }, fin: { numero: 9 } },
  //       semisSousAbris: { debut: { numero: 2 }, fin: { numero: 4 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Piment fort de cayenne" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 4 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 2 }, fin: { numero: 4 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Aubergine Violetta di firenze" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 4 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 2 }, fin: { numero: 4 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Poireau Monstrueux de carentan" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 2 }, fin: { numero: 5 } },
  //       semisSousAbris: { debut: { numero: 1 }, fin: { numero: 3 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Persil frisé vert foncé ou double" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 2 }, fin: { numero: 9 } },
  //       semisSousAbris: { debut: { numero: 0 }, fin: { numero: 0 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Ciboulette commune ou civette" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 2 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 0 }, fin: { numero: 0 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Artichaut gros vert de laon" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 4 }, fin: { numero: 5 } },
  //       semisSousAbris: { debut: { numero: 2 }, fin: { numero: 3 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Épinard d'été matador" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 2 }, fin: { numero: 4 } },
  //       semisSousAbris: { debut: { numero: 0 }, fin: { numero: 0 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Navet rouge plat hâtif à feuille entière" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 5 } },
  //       semisSousAbris: { debut: { numero: 2 }, fin: { numero: 3 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Laitue pommée grosse blonde paresseuse" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 5 }, fin: { numero: 10 } },
  //       semisSousAbris: { debut: { numero: 2 }, fin: { numero: 4 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Laitue batavia iceberg reine des glaces" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 4 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 2 }, fin: { numero: 3 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Laitue pommée reine de mai" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 5 } },
  //       semisSousAbris: { debut: { numero: 2 }, fin: { numero: 3 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Tomate noire russe" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 4 } },
  //       semisSousAbris: { debut: { numero: 2 }, fin: { numero: 3 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Tomate beefsteak" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 5 } },
  //       semisSousAbris: { debut: { numero: 2 }, fin: { numero: 4 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Pois nain norli grain rond" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 2 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 2 }, fin: { numero: 6 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Fève Aguadulce à très longue cosse" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 2 }, fin: { numero: 12 } },
  //       semisSousAbris: { debut: { numero: 2 }, fin: { numero: 12 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Bourrache" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 3 }, fin: { numero: 6 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Thym ordinaire" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 7 } },
  //       semisSousAbris: { debut: { numero: 3 }, fin: { numero: 7 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Ciboule de chine" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 11 } },
  //       semisSousAbris: { debut: { numero: 3 }, fin: { numero: 11 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Carotte jaune du doubs" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 11 } },
  //       semisSousAbris: { debut: { numero: 3 }, fin: { numero: 11 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Sauge officinale" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 9 } },
  //       semisSousAbris: { debut: { numero: 3 }, fin: { numero: 9 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Concombre vert long maraîcher" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 5 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 3 }, fin: { numero: 5 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Melon ancien vieille france" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 4 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 3 }, fin: { numero: 5 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Laitue cressonnette marocaine" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 8 } },
  //       semisSousAbris: { debut: { numero: 3 }, fin: { numero: 8 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Roquette cultivée" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 9 } },
  //       semisSousAbris: { debut: { numero: 3 }, fin: { numero: 9 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Roquette sauvage" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 9 } },
  //       semisSousAbris: { debut: { numero: 3 }, fin: { numero: 9 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Maïs doux bantam" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 4 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 0 }, fin: { numero: 0 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Menthe poivrée" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 10 } },
  //       semisSousAbris: { debut: { numero: 3 }, fin: { numero: 10 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Sarriette commune" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 4 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 4 }, fin: { numero: 6 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Origan marjolaine vivace" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 4 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 4 }, fin: { numero: 6 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Coriandre" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 9 } },
  //       semisSousAbris: { debut: { numero: 3 }, fin: { numero: 9 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Betterave de détroit améliorée 3" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 4 }, fin: { numero: 7 } },
  //       semisSousAbris: { debut: { numero: 4 }, fin: { numero: 7 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Potiron petit bonnet turc" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 4 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 0 }, fin: { numero: 0 } }
  //     }
  //   }
  // },
  // {
  //   nomsVernaculaires: [{ nom: "Courge pâtisson blanc" }],
  //   cycleDeVie: {
  //     semis: {
  //       semisPleineTerre: { debut: { numero: 4 }, fin: { numero: 6 } },
  //       semisSousAbris: { debut: { numero: 0 }, fin: { numero: 0 } }
  //     }
  //   }
  // },
  {
    nomsVernaculaires: [{ nom: "Chou de chine" }],
    cycleDeVie: {
      semis: {
        semisPleineTerre: { debut: { numero: 5 }, fin: { numero: 9 } },
        semisSousAbris: { debut: { numero: 0 }, fin: { numero: 0 } }
      }
    }
  },
  {
    nomsVernaculaires: [{ nom: "Mâche à grosse graine" }],
    cycleDeVie: {
      semis: {
        semisPleineTerre: { debut: { numero: 7 }, fin: { numero: 9 } },
        semisSousAbris: { debut: { numero: 0 }, fin: { numero: 0 } }
      }
    }
  },
  {
    nomsVernaculaires: [{ nom: "Laitue à couper" }],
    cycleDeVie: {
      semis: {
        semisPleineTerre: { debut: { numero: 3 }, fin: { numero: 8 } },
        semisSousAbris: { debut: { numero: 9 }, fin: { numero: 2 } }
      }
    }
  },
  {
    nomsVernaculaires: [{ nom: "Épinard Géant d'hiver" }],
    cycleDeVie: {
      semis: {
        semisPleineTerre: { debut: { numero: 8 }, fin: { numero: 10 } },
        semisSousAbris: { debut: { numero: 0 }, fin: { numero: 0 } }
      }
    }
  }
];

export const MOIS: any[] = [
  {
    numero: 1,
    nom: 'Janvier'
  },
  {
    numero: 2,
    nom: 'Février'
  },
  {
    numero: 3,
    nom: 'Mars'
  },
  {
    numero: 4,
    nom: 'Avril'
  },
  {
    numero: 5,
    nom: 'Mai'
  },
  {
    numero: 6,
    nom: 'Juin'
  },
  {
    numero: 7,
    nom: 'Juillet'
  },
  {
    numero: 8,
    nom: 'Août'
  },
  {
    numero: 9,
    nom: 'Septembre'
  },
  {
    numero: 10,
    nom: 'Octobre'
  },
  {
    numero: 11,
    nom: 'Novembre'
  },
  {
    numero: 12,
    nom: 'Décembre'
  }
];


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
    protected modalService: NgbModal
  ) {
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    // const plantes = PLANTES;
    // const mois = MOIS;

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

  // delete(plante: IPlante): void {
  //   const modalRef = this.modalService.open(PlanteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
  //   modalRef.componentInstance.plante = plante;
  //   // unsubscribe not needed because closed completes on modal close
  //   modalRef.closed.subscribe(reason => {
  //     if (reason === 'deleted') {
  //       this.loadPage();
  //     }
  //   });
  // }

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

  protected onSuccess(data: IPlante[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get("X-Total-Count"));
    this.page = page;
    if (navigate) {
      this.router.navigate(["/plante"], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + "," + (this.ascending ? ASC : DESC)
        }
      });
    }
    this.plantes = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
