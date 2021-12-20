import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'periode-annee',
        data: { pageTitle: 'PeriodeAnnees' },
        loadChildren: () => import('./plantsMS/periode-annee/periode-annee.module').then(m => m.PlantsMsPeriodeAnneeModule),
      },
      {
        path: 'racine',
        data: { pageTitle: 'Racines' },
        loadChildren: () => import('./plantsMS/racine/racine.module').then(m => m.PlantsMsRacineModule),
      },
      {
        path: 'cronquist-rank',
        data: { pageTitle: 'CronquistRanks' },
        loadChildren: () =>
          import('./classificationMS/cronquist-rank/cronquist-rank.module').then(m => m.ClassificationMsCronquistRankModule),
      },
      {
        path: 'url',
        data: { pageTitle: 'Urls' },
        loadChildren: () => import('./classificationMS/url/url.module').then(m => m.ClassificationMsUrlModule),
      },
      {
        path: 'plante',
        data: { pageTitle: 'Plantes' },
        loadChildren: () => import('./plantsMS/plante/plante.module').then(m => m.PlantsMsPlanteModule),
      },
      {
        path: 'semis',
        data: { pageTitle: 'Semis' },
        loadChildren: () => import('./plantsMS/semis/semis.module').then(m => m.PlantsMsSemisModule),
      },
      {
        path: 'strate',
        data: { pageTitle: 'Strates' },
        loadChildren: () => import('./plantsMS/strate/strate.module').then(m => m.PlantsMsStrateModule),
      },
      {
        path: 'sol',
        data: { pageTitle: 'Sols' },
        loadChildren: () => import('./plantsMS/sol/sol.module').then(m => m.PlantsMsSolModule),
      },
      {
        path: 'temperature',
        data: { pageTitle: 'Temperatures' },
        loadChildren: () => import('./plantsMS/temperature/temperature.module').then(m => m.PlantsMsTemperatureModule),
      },
      {
        path: 'nom-vernaculaire',
        data: { pageTitle: 'NomVernaculaires' },
        loadChildren: () => import('./plantsMS/nom-vernaculaire/nom-vernaculaire.module').then(m => m.PlantsMsNomVernaculaireModule),
      },
      {
        path: 'reproduction',
        data: { pageTitle: 'Reproductions' },
        loadChildren: () => import('./plantsMS/reproduction/reproduction.module').then(m => m.PlantsMsReproductionModule),
      },
      {
        path: 'ressemblance',
        data: { pageTitle: 'Ressemblances' },
        loadChildren: () => import('./plantsMS/ressemblance/ressemblance.module').then(m => m.PlantsMsRessemblanceModule),
      },
      {
        path: 'allelopathie',
        data: { pageTitle: 'Allelopathies' },
        loadChildren: () => import('./plantsMS/allelopathie/allelopathie.module').then(m => m.PlantsMsAllelopathieModule),
      },
      {
        path: 'feuillage',
        data: { pageTitle: 'Feuillages' },
        loadChildren: () => import('./plantsMS/feuillage/feuillage.module').then(m => m.PlantsMsFeuillageModule),
      },
      {
        path: 'mois',
        data: { pageTitle: 'Mois' },
        loadChildren: () => import('./plantsMS/mois/mois.module').then(m => m.PlantsMsMoisModule),
      },
      {
        path: 'germination',
        data: { pageTitle: 'Germinations' },
        loadChildren: () => import('./plantsMS/germination/germination.module').then(m => m.PlantsMsGerminationModule),
      },
      {
        path: 'ensoleillement',
        data: { pageTitle: 'Ensoleillements' },
        loadChildren: () => import('./plantsMS/ensoleillement/ensoleillement.module').then(m => m.PlantsMsEnsoleillementModule),
      },
      {
        path: 'type-semis',
        data: { pageTitle: 'TypeSemis' },
        loadChildren: () => import('./plantsMS/type-semis/type-semis.module').then(m => m.PlantsMsTypeSemisModule),
      },
      {
        path: 'cycle-de-vie',
        data: { pageTitle: 'CycleDeVies' },
        loadChildren: () => import('./plantsMS/cycle-de-vie/cycle-de-vie.module').then(m => m.PlantsMsCycleDeVieModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
