import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'mois',
        data: { pageTitle: 'Mois' },
        loadChildren: () => import('./microservice/mois/mois.module').then(m => m.PlantsmsMoisModule),
      },
      {
        path: 'periode-annee',
        data: { pageTitle: 'PeriodeAnnees' },
        loadChildren: () => import('./microservice/periode-annee/periode-annee.module').then(m => m.PlantsmsPeriodeAnneeModule),
      },
      {
        path: 'plante',
        data: { pageTitle: 'Plantes' },
        loadChildren: () => import('./microservice/plante/plante.module').then(m => m.PlantsmsPlanteModule),
      },
      {
        path: 'reproduction',
        data: { pageTitle: 'Reproductions' },
        loadChildren: () => import('./microservice/reproduction/reproduction.module').then(m => m.PlantsmsReproductionModule),
      },
      {
        path: 'sol',
        data: { pageTitle: 'Sols' },
        loadChildren: () => import('./microservice/sol/sol.module').then(m => m.PlantsmsSolModule),
      },
      {
        path: 'nom-vernaculaire',
        data: { pageTitle: 'NomVernaculaires' },
        loadChildren: () => import('./microservice/nom-vernaculaire/nom-vernaculaire.module').then(m => m.PlantsmsNomVernaculaireModule),
      },
      {
        path: 'classification',
        data: { pageTitle: 'Classifications' },
        loadChildren: () => import('./microservice/classification/classification.module').then(m => m.PlantsmsClassificationModule),
      },
      {
        path: 'cronquist',
        data: { pageTitle: 'Cronquists' },
        loadChildren: () => import('./microservice/cronquist/cronquist.module').then(m => m.PlantsmsCronquistModule),
      },
      {
        path: 'raunkier',
        data: { pageTitle: 'Raunkiers' },
        loadChildren: () => import('./microservice/raunkier/raunkier.module').then(m => m.PlantsmsRaunkierModule),
      },
      {
        path: 'apgi',
        data: { pageTitle: 'APGIS' },
        loadChildren: () => import('./microservice/apgi/apgi.module').then(m => m.PlantsmsAPGIModule),
      },
      {
        path: 'apgii',
        data: { pageTitle: 'APGIIS' },
        loadChildren: () => import('./microservice/apgii/apgii.module').then(m => m.PlantsmsAPGIIModule),
      },
      {
        path: 'apgiii',
        data: { pageTitle: 'APGIIIS' },
        loadChildren: () => import('./microservice/apgiii/apgiii.module').then(m => m.PlantsmsAPGIIIModule),
      },
      {
        path: 'apgiv',
        data: { pageTitle: 'APGIVS' },
        loadChildren: () => import('./microservice/apgiv/apgiv.module').then(m => m.PlantsmsAPGIVModule),
      },
      {
        path: 'semis',
        data: { pageTitle: 'Semis' },
        loadChildren: () => import('./microservice/semis/semis.module').then(m => m.PlantsmsSemisModule),
      },
      {
        path: 'type-semis',
        data: { pageTitle: 'TypeSemis' },
        loadChildren: () => import('./microservice/type-semis/type-semis.module').then(m => m.PlantsmsTypeSemisModule),
      },
      {
        path: 'cycle-de-vie',
        data: { pageTitle: 'CycleDeVies' },
        loadChildren: () => import('./microservice/cycle-de-vie/cycle-de-vie.module').then(m => m.PlantsmsCycleDeVieModule),
      },
      {
        path: 'germination',
        data: { pageTitle: 'Germinations' },
        loadChildren: () => import('./microservice/germination/germination.module').then(m => m.PlantsmsGerminationModule),
      },
      {
        path: 'allelopathie',
        data: { pageTitle: 'Allelopathies' },
        loadChildren: () => import('./microservice/allelopathie/allelopathie.module').then(m => m.PlantsmsAllelopathieModule),
      },
      {
        path: 'ressemblance',
        data: { pageTitle: 'Ressemblances' },
        loadChildren: () => import('./microservice/ressemblance/ressemblance.module').then(m => m.PlantsmsRessemblanceModule),
      },
      {
        path: 'ensoleillement',
        data: { pageTitle: 'Ensoleillements' },
        loadChildren: () => import('./microservice/ensoleillement/ensoleillement.module').then(m => m.PlantsmsEnsoleillementModule),
      },
      {
        path: 'temperature',
        data: { pageTitle: 'Temperatures' },
        loadChildren: () => import('./microservice/temperature/temperature.module').then(m => m.PlantsmsTemperatureModule),
      },
      {
        path: 'racine',
        data: { pageTitle: 'Racines' },
        loadChildren: () => import('./microservice/racine/racine.module').then(m => m.PlantsmsRacineModule),
      },
      {
        path: 'strate',
        data: { pageTitle: 'Strates' },
        loadChildren: () => import('./microservice/strate/strate.module').then(m => m.PlantsmsStrateModule),
      },
      {
        path: 'feuillage',
        data: { pageTitle: 'Feuillages' },
        loadChildren: () => import('./microservice/feuillage/feuillage.module').then(m => m.PlantsmsFeuillageModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
