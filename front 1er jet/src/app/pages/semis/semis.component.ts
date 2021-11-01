import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { Plante } from './Plante';
import { SortableHeader, SortEvent } from './sortable-header.directive';
import { ToastService } from '../../shared/toast/toast.service';
import { filtreMethodeSemisDefault } from './filtres/methode-semis/methode-semis.component';

const PLANTES: Plante[] = [
  {
    nom: 'Chou cabus',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 1,
    finSemisSousAbris: 3
  },
  {
    nom: 'Poireau',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 4,
    debutSemisSousAbris: 0,
    finSemisSousAbris: 0
  },
  {
    nom: 'Pourpier',
    debutSemisPleineTerre: 5,
    finSemisPleineTerre: 8,
    debutSemisSousAbris: 1,
    finSemisSousAbris: 4
  },
  {
    nom: 'Radis 18 jour',
    debutSemisPleineTerre: 9,
    finSemisPleineTerre: 3,
    debutSemisSousAbris: 1,
    finSemisSousAbris: 3
  },
  {
    nom: 'Chou cabus coeur de boeuf des vertus',
    debutSemisPleineTerre: 10,
    finSemisPleineTerre: 3,
    debutSemisSousAbris: 1,
    finSemisSousAbris: 3
  },
  {
    nom: 'Chou-fleur merveille de toutes saisons',
    debutSemisPleineTerre: 4,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 1,
    finSemisSousAbris: 2
  },
  {
    nom: 'Laitue rouge grenobloise',
    debutSemisPleineTerre: 4,
    finSemisPleineTerre: 9,
    debutSemisSousAbris: 2,
    finSemisSousAbris: 3
  },
  {
    nom: 'Laitue reine de mai de pleine terre',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 4,
    debutSemisSousAbris: 12,
    finSemisSousAbris: 2
  },
  {
    nom: 'Laitue batavia lollo rossa',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 9,
    debutSemisSousAbris: 1,
    finSemisSousAbris: 2
  },
  {
    nom: 'Radis kocto HF1',
    debutSemisPleineTerre: 4,
    finSemisPleineTerre: 9,
    debutSemisSousAbris: 2,
    finSemisSousAbris: 4
  },
  {
    nom: 'Piment fort de cayenne',
    debutSemisPleineTerre: 4,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 2,
    finSemisSousAbris: 4
  },
  {
    nom: 'Aubergine Violetta di firenze',
    debutSemisPleineTerre: 4,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 2,
    finSemisSousAbris: 4
  },
  {
    nom: 'Poireau Monstueux de carentan',
    debutSemisPleineTerre: 2,
    finSemisPleineTerre: 5,
    debutSemisSousAbris: 1,
    finSemisSousAbris: 3
  },
  {
    nom: 'Persil frisé vert foncé ou double',
    debutSemisPleineTerre: 2,
    finSemisPleineTerre: 9,
    debutSemisSousAbris: 0,
    finSemisSousAbris: 0
  },
  {
    nom: 'Ciboulette commune ou civette',
    debutSemisPleineTerre: 2,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 0,
    finSemisSousAbris: 0
  },
  {
    nom: 'Artichaut gros vert de laon',
    debutSemisPleineTerre: 4,
    finSemisPleineTerre: 5,
    debutSemisSousAbris: 2,
    finSemisSousAbris: 3
  },
  {
    nom: 'Epinard d\'été matador',
    debutSemisPleineTerre: 2,
    finSemisPleineTerre: 4,
    debutSemisSousAbris: 0,
    finSemisSousAbris: 0
  },
  {
    nom: 'Navet rouge plat hâtif à feuille entière',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 5,
    debutSemisSousAbris: 2,
    finSemisSousAbris: 3
  },
  {
    nom: 'Laitue pommée grosse bloande paresseuse',
    debutSemisPleineTerre: 5,
    finSemisPleineTerre: 10,
    debutSemisSousAbris: 2,
    finSemisSousAbris: 4
  },
  {
    nom: 'Laitue batavia iceberg reine des glaces',
    debutSemisPleineTerre: 4,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 2,
    finSemisSousAbris: 3
  },
  {
    nom: 'Laitue pommée reine de mai',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 5,
    debutSemisSousAbris: 2,
    finSemisSousAbris: 3
  },
  {
    nom: 'Tomate noire russe',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 4,
    debutSemisSousAbris: 2,
    finSemisSousAbris: 3
  },
  {
    nom: 'Tomate beefsteack',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 5,
    debutSemisSousAbris: 2,
    finSemisSousAbris: 4
  },
  {
    nom: 'Pois nain norli grain rond',
    debutSemisPleineTerre: 2,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 2,
    finSemisSousAbris: 6
  },
  {
    nom: 'Fève Aguadulce à très longue cosse',
    debutSemisPleineTerre: 2,
    finSemisPleineTerre: 12,
    debutSemisSousAbris: 2,
    finSemisSousAbris: 12
  },
  {
    nom: 'Bourrache',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 3,
    finSemisSousAbris: 6
  },
  {
    nom: 'Thym ordinaire',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 7,
    debutSemisSousAbris: 3,
    finSemisSousAbris: 7
  },
  {
    nom: 'Ciboule de chine',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 11,
    debutSemisSousAbris: 3,
    finSemisSousAbris: 11
  },
  {
    nom: 'Caroote jaune du doubs',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 11,
    debutSemisSousAbris: 3,
    finSemisSousAbris: 11
  },
  {
    nom: 'Sauge officinale',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 9,
    debutSemisSousAbris: 3,
    finSemisSousAbris: 9
  },
  {
    nom: 'Concombre vert long maraîcher',
    debutSemisPleineTerre: 5,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 3,
    finSemisSousAbris: 5
  },
  {
    nom: 'Melon ancien vieille france',
    debutSemisPleineTerre: 4,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 3,
    finSemisSousAbris: 5
  },
  {
    nom: 'Laitue cressonnette marocaine',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 8,
    debutSemisSousAbris: 3,
    finSemisSousAbris: 8
  },
  {
    nom: 'Roquette cultivée',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 9,
    debutSemisSousAbris: 3,
    finSemisSousAbris: 9
  },
  {
    nom: 'Roquette sauvage',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 9,
    debutSemisSousAbris: 3,
    finSemisSousAbris: 9
  },
  {
    nom: 'Maïs doux bantam',
    debutSemisPleineTerre: 4,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 0,
    finSemisSousAbris: 0
  },
  {
    nom: 'Menthe poivrée',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 10,
    debutSemisSousAbris: 3,
    finSemisSousAbris: 10
  },
  {
    nom: 'Sariette commune',
    debutSemisPleineTerre: 4,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 4,
    finSemisSousAbris: 6
  },
  {
    nom: 'Origan marjolaine vivace',
    debutSemisPleineTerre: 4,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 4,
    finSemisSousAbris: 6
  },
  {
    nom: 'Coriandre',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 9,
    debutSemisSousAbris: 3,
    finSemisSousAbris: 9
  },
  {
    nom: 'Betterave de détroit améliorée 3',
    debutSemisPleineTerre: 4,
    finSemisPleineTerre: 7,
    debutSemisSousAbris: 4,
    finSemisSousAbris: 7
  },
  {
    nom: 'Potiron petit bonnet turc',
    debutSemisPleineTerre: 4,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 0,
    finSemisSousAbris: 0
  },
  {
    nom: 'Courge pâtisson blanc',
    debutSemisPleineTerre: 4,
    finSemisPleineTerre: 6,
    debutSemisSousAbris: 0,
    finSemisSousAbris: 0
  },
  {
    nom: 'Chou de chine',
    debutSemisPleineTerre: 5,
    finSemisPleineTerre: 9,
    debutSemisSousAbris: 0,
    finSemisSousAbris: 0
  },
  {
    nom: 'Mâche à grosse graine',
    debutSemisPleineTerre: 7,
    finSemisPleineTerre: 9,
    debutSemisSousAbris: 0,
    finSemisSousAbris: 0
  },
  {
    nom: 'Laitue à couper',
    debutSemisPleineTerre: 3,
    finSemisPleineTerre: 8,
    debutSemisSousAbris: 9,
    finSemisSousAbris: 2
  },
  {
    nom: 'Epinard Géant d\'hiver',
    debutSemisPleineTerre: 8,
    finSemisPleineTerre: 10,
    debutSemisSousAbris: 0,
    finSemisSousAbris: 0
  }
]

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
]

const estContenuDans = (x: number, start: number, end: number) => x >= start && x <= end;

const NA = (start: number, end: number) =>
  start === 0 && end === 0;

const compare = (v1: string | number, v2: string | number) => v1 < v2 ? -1 : v1 > v2 ? 1 : 0;

@Component({
  selector: 'ptg-semis',
  templateUrl: './semis.component.html',
  styleUrls: ['./semis.component.scss']
})
export class SemisComponent implements OnInit {


  plantes = PLANTES;
  mois = MOIS;



  constructor(public toastService: ToastService) {
    console.log('constructor SemisComponent');
    this.filtreMethodeSemis = filtreMethodeSemisDefault;
  }

  ngOnInit(): void {
    this.toastService.show('I am a success toast', { classname: 'bg-success text-light', delay: 10000 });
    console.log('ngOnInit SemisComponent');
  }

  getSemisPleineTerrePossible = (mois: number, plante: Plante) =>
    NA(plante.debutSemisPleineTerre, plante.finSemisPleineTerre) ? 'grey' :
      estContenuDans(mois, plante.debutSemisPleineTerre, plante.finSemisPleineTerre) ? 'green' : 'red'

  getSemisSousAbrisPossible = (mois: number, plante: Plante) => NA(plante.debutSemisSousAbris, plante.finSemisSousAbris) ? 'grey' :
    estContenuDans(mois, plante.debutSemisSousAbris, plante.finSemisSousAbris) ? 'green' : 'red'

  /**
   * Tri du tableau
   */

  @ViewChildren(SortableHeader)
  headers!: QueryList<SortableHeader>;

  onSort({ column, direction }: SortEvent) {

    // resetting other headers
    this.headers?.forEach(header => {
      if (header.sortable !== column) {
        header.direction = '';
      }
    });

    // sorting countries
    if (direction === '' || column === '') {
      this.plantes = PLANTES;
    } else {
      this.plantes = [...PLANTES].sort((a, b) => {
        const res = compare(a[column], b[column]);
        return direction === 'asc' ? res : -res;
      });
    }
  }

  /**
   * Gestion des filtres
   */

  filtreMethodeSemis!: { pleineTerre: boolean, sousAbris: boolean };

  onMethodeSemisChange(event: any): void {
    // console.log(this.filtres.filtreMethodeSemis.value);
    this.filtreMethodeSemis = event;
  }

  onPeriodeChange(event: any): void {
    // console.log(this.filtres.filtreMethodeSemis.value);
    const debut = MOIS.filter(m => m.nom === event.debut)[0].numero;
    const fin = MOIS.filter(m => m.nom === event.fin)[0].numero;
    this.mois = MOIS.slice(+debut - 1, fin);
  }


}
