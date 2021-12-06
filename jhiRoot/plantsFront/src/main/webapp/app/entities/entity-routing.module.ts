import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'mois',
        data: { pageTitle: 'Mois' },
        loadChildren: () => import('./mois/mois.module').then(m => m.MoisModule),
      },
      {
        path: 'periode-annee',
        data: { pageTitle: 'PeriodeAnnees' },
        loadChildren: () => import('./periode-annee/periode-annee.module').then(m => m.PeriodeAnneeModule),
      },
      {
        path: 'plante',
        data: { pageTitle: 'Plantes' },
        loadChildren: () => import('./plante/plante.module').then(m => m.PlanteModule),
      },
      {
        path: 'reproduction',
        data: { pageTitle: 'Reproductions' },
        loadChildren: () => import('./reproduction/reproduction.module').then(m => m.ReproductionModule),
      },
      {
        path: 'sol',
        data: { pageTitle: 'Sols' },
        loadChildren: () => import('./sol/sol.module').then(m => m.SolModule),
      },
      {
        path: 'nom-vernaculaire',
        data: { pageTitle: 'NomVernaculaires' },
        loadChildren: () => import('./nom-vernaculaire/nom-vernaculaire.module').then(m => m.NomVernaculaireModule),
      },
      {
        path: 'classification',
        data: { pageTitle: 'Classifications' },
        loadChildren: () => import('./classification/classification.module').then(m => m.ClassificationModule),
      },
      {
        path: 'cronquist',
        data: { pageTitle: 'Cronquists' },
        loadChildren: () => import('./cronquist/cronquist.module').then(m => m.CronquistModule),
      },
      {
        path: 'raunkier',
        data: { pageTitle: 'Raunkiers' },
        loadChildren: () => import('./raunkier/raunkier.module').then(m => m.RaunkierModule),
      },
      {
        path: 'apgi',
        data: { pageTitle: 'APGIS' },
        loadChildren: () => import('./apgi/apgi.module').then(m => m.APGIModule),
      },
      {
        path: 'apgii',
        data: { pageTitle: 'APGIIS' },
        loadChildren: () => import('./apgii/apgii.module').then(m => m.APGIIModule),
      },
      {
        path: 'apgiii',
        data: { pageTitle: 'APGIIIS' },
        loadChildren: () => import('./apgiii/apgiii.module').then(m => m.APGIIIModule),
      },
      {
        path: 'apgiv',
        data: { pageTitle: 'APGIVS' },
        loadChildren: () => import('./apgiv/apgiv.module').then(m => m.APGIVModule),
      },
      {
        path: 'semis',
        data: { pageTitle: 'Semis' },
        loadChildren: () => import('./semis/semis.module').then(m => m.SemisModule),
      },
      {
        path: 'type-semis',
        data: { pageTitle: 'TypeSemis' },
        loadChildren: () => import('./type-semis/type-semis.module').then(m => m.TypeSemisModule),
      },
      {
        path: 'cycle-de-vie',
        data: { pageTitle: 'CycleDeVies' },
        loadChildren: () => import('./cycle-de-vie/cycle-de-vie.module').then(m => m.CycleDeVieModule),
      },
      {
        path: 'germination',
        data: { pageTitle: 'Germinations' },
        loadChildren: () => import('./germination/germination.module').then(m => m.GerminationModule),
      },
      {
        path: 'allelopathie',
        data: { pageTitle: 'Allelopathies' },
        loadChildren: () => import('./allelopathie/allelopathie.module').then(m => m.AllelopathieModule),
      },
      {
        path: 'ressemblance',
        data: { pageTitle: 'Ressemblances' },
        loadChildren: () => import('./ressemblance/ressemblance.module').then(m => m.RessemblanceModule),
      },
      {
        path: 'ensoleillement',
        data: { pageTitle: 'Ensoleillements' },
        loadChildren: () => import('./ensoleillement/ensoleillement.module').then(m => m.EnsoleillementModule),
      },
      {
        path: 'temperature',
        data: { pageTitle: 'Temperatures' },
        loadChildren: () => import('./temperature/temperature.module').then(m => m.TemperatureModule),
      },
      {
        path: 'racine',
        data: { pageTitle: 'Racines' },
        loadChildren: () => import('./racine/racine.module').then(m => m.RacineModule),
      },
      {
        path: 'strate',
        data: { pageTitle: 'Strates' },
        loadChildren: () => import('./strate/strate.module').then(m => m.StrateModule),
      },
      {
        path: 'feuillage',
        data: { pageTitle: 'Feuillages' },
        loadChildren: () => import('./feuillage/feuillage.module').then(m => m.FeuillageModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
