import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'type-semis',
        data: { pageTitle: 'gatewayApp.microserviceTypeSemis.home.title' },
        loadChildren: () => import('./microservice/type-semis/type-semis.module').then(m => m.MicroserviceTypeSemisModule),
      },
      {
        path: 'feuillage',
        data: { pageTitle: 'gatewayApp.microserviceFeuillage.home.title' },
        loadChildren: () => import('./microservice/feuillage/feuillage.module').then(m => m.MicroserviceFeuillageModule),
      },
      {
        path: 'temperature',
        data: { pageTitle: 'gatewayApp.microserviceTemperature.home.title' },
        loadChildren: () => import('./microservice/temperature/temperature.module').then(m => m.MicroserviceTemperatureModule),
      },
      {
        path: 'reproduction',
        data: { pageTitle: 'gatewayApp.microserviceReproduction.home.title' },
        loadChildren: () => import('./microservice/reproduction/reproduction.module').then(m => m.MicroserviceReproductionModule),
      },
      {
        path: 'raunkier',
        data: { pageTitle: 'gatewayApp.microserviceRaunkier.home.title' },
        loadChildren: () => import('./microservice/raunkier/raunkier.module').then(m => m.MicroserviceRaunkierModule),
      },
      {
        path: 'plante',
        data: { pageTitle: 'gatewayApp.microservicePlante.home.title' },
        loadChildren: () => import('./microservice/plante/plante.module').then(m => m.MicroservicePlanteModule),
      },
      {
        path: 'sol',
        data: { pageTitle: 'gatewayApp.microserviceSol.home.title' },
        loadChildren: () => import('./microservice/sol/sol.module').then(m => m.MicroserviceSolModule),
      },
      {
        path: 'mois',
        data: { pageTitle: 'gatewayApp.microserviceMois.home.title' },
        loadChildren: () => import('./microservice/mois/mois.module').then(m => m.MicroserviceMoisModule),
      },
      {
        path: 'apgiv',
        data: { pageTitle: 'gatewayApp.microserviceAPgiv.home.title' },
        loadChildren: () => import('./microservice/apgiv/apgiv.module').then(m => m.MicroserviceAPGIVModule),
      },
      {
        path: 'cycle-de-vie',
        data: { pageTitle: 'gatewayApp.microserviceCycleDeVie.home.title' },
        loadChildren: () => import('./microservice/cycle-de-vie/cycle-de-vie.module').then(m => m.MicroserviceCycleDeVieModule),
      },
      {
        path: 'exposition',
        data: { pageTitle: 'gatewayApp.microserviceExposition.home.title' },
        loadChildren: () => import('./microservice/exposition/exposition.module').then(m => m.MicroserviceExpositionModule),
      },
      {
        path: 'apgiii',
        data: { pageTitle: 'gatewayApp.microserviceAPgiii.home.title' },
        loadChildren: () => import('./microservice/apgiii/apgiii.module').then(m => m.MicroserviceAPGIIIModule),
      },
      {
        path: 'strate',
        data: { pageTitle: 'gatewayApp.microserviceStrate.home.title' },
        loadChildren: () => import('./microservice/strate/strate.module').then(m => m.MicroserviceStrateModule),
      },
      {
        path: 'nom-vernaculaire',
        data: { pageTitle: 'gatewayApp.microserviceNomVernaculaire.home.title' },
        loadChildren: () =>
          import('./microservice/nom-vernaculaire/nom-vernaculaire.module').then(m => m.MicroserviceNomVernaculaireModule),
      },
      {
        path: 'apgi',
        data: { pageTitle: 'gatewayApp.microserviceAPgi.home.title' },
        loadChildren: () => import('./microservice/apgi/apgi.module').then(m => m.MicroserviceAPGIModule),
      },
      {
        path: 'periode-annee',
        data: { pageTitle: 'gatewayApp.microservicePeriodeAnnee.home.title' },
        loadChildren: () => import('./microservice/periode-annee/periode-annee.module').then(m => m.MicroservicePeriodeAnneeModule),
      },
      {
        path: 'apgii',
        data: { pageTitle: 'gatewayApp.microserviceAPgii.home.title' },
        loadChildren: () => import('./microservice/apgii/apgii.module').then(m => m.MicroserviceAPGIIModule),
      },
      {
        path: 'ressemblance',
        data: { pageTitle: 'gatewayApp.microserviceRessemblance.home.title' },
        loadChildren: () => import('./microservice/ressemblance/ressemblance.module').then(m => m.MicroserviceRessemblanceModule),
      },
      {
        path: 'classification',
        data: { pageTitle: 'gatewayApp.microserviceClassification.home.title' },
        loadChildren: () => import('./microservice/classification/classification.module').then(m => m.MicroserviceClassificationModule),
      },
      {
        path: 'allelopathie',
        data: { pageTitle: 'gatewayApp.microserviceAllelopathie.home.title' },
        loadChildren: () => import('./microservice/allelopathie/allelopathie.module').then(m => m.MicroserviceAllelopathieModule),
      },
      {
        path: 'semis',
        data: { pageTitle: 'gatewayApp.microserviceSemis.home.title' },
        loadChildren: () => import('./microservice/semis/semis.module').then(m => m.MicroserviceSemisModule),
      },
      {
        path: 'cronquist',
        data: { pageTitle: 'gatewayApp.microserviceCronquist.home.title' },
        loadChildren: () => import('./microservice/cronquist/cronquist.module').then(m => m.MicroserviceCronquistModule),
      },
      {
        path: 'racine',
        data: { pageTitle: 'gatewayApp.microserviceRacine.home.title' },
        loadChildren: () => import('./microservice/racine/racine.module').then(m => m.MicroserviceRacineModule),
      },
      {
        path: 'germination',
        data: { pageTitle: 'gatewayApp.microserviceGermination.home.title' },
        loadChildren: () => import('./microservice/germination/germination.module').then(m => m.MicroserviceGerminationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
