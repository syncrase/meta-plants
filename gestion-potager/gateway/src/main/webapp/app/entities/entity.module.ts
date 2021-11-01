import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'mois',
        loadChildren: () => import('./microservice/mois/mois.module').then(m => m.MicroserviceMoisModule),
      },
      {
        path: 'periode-annee',
        loadChildren: () => import('./microservice/periode-annee/periode-annee.module').then(m => m.MicroservicePeriodeAnneeModule),
      },
      {
        path: 'plante',
        loadChildren: () => import('./microservice/plante/plante.module').then(m => m.MicroservicePlanteModule),
      },
      {
        path: 'sol',
        loadChildren: () => import('./microservice/sol/sol.module').then(m => m.MicroserviceSolModule),
      },
      {
        path: 'nom-vernaculaire',
        loadChildren: () =>
          import('./microservice/nom-vernaculaire/nom-vernaculaire.module').then(m => m.MicroserviceNomVernaculaireModule),
      },
      {
        path: 'classification',
        loadChildren: () => import('./microservice/classification/classification.module').then(m => m.MicroserviceClassificationModule),
      },
      {
        path: 'cronquist',
        loadChildren: () => import('./microservice/cronquist/cronquist.module').then(m => m.MicroserviceCronquistModule),
      },
      {
        path: 'raunkier',
        loadChildren: () => import('./microservice/raunkier/raunkier.module').then(m => m.MicroserviceRaunkierModule),
      },
      {
        path: 'apgi',
        loadChildren: () => import('./microservice/apgi/apgi.module').then(m => m.MicroserviceAPGIModule),
      },
      {
        path: 'apgii',
        loadChildren: () => import('./microservice/apgii/apgii.module').then(m => m.MicroserviceAPGIIModule),
      },
      {
        path: 'apgiii',
        loadChildren: () => import('./microservice/apgiii/apgiii.module').then(m => m.MicroserviceAPGIIIModule),
      },
      {
        path: 'apgiv',
        loadChildren: () => import('./microservice/apgiv/apgiv.module').then(m => m.MicroserviceAPGIVModule),
      },
      {
        path: 'semis',
        loadChildren: () => import('./microservice/semis/semis.module').then(m => m.MicroserviceSemisModule),
      },
      {
        path: 'type-semis',
        loadChildren: () => import('./microservice/type-semis/type-semis.module').then(m => m.MicroserviceTypeSemisModule),
      },
      {
        path: 'cycle-de-vie',
        loadChildren: () => import('./microservice/cycle-de-vie/cycle-de-vie.module').then(m => m.MicroserviceCycleDeVieModule),
      },
      {
        path: 'germination',
        loadChildren: () => import('./microservice/germination/germination.module').then(m => m.MicroserviceGerminationModule),
      },
      {
        path: 'allelopathie',
        loadChildren: () => import('./microservice/allelopathie/allelopathie.module').then(m => m.MicroserviceAllelopathieModule),
      },
      {
        path: 'ressemblance',
        loadChildren: () => import('./microservice/ressemblance/ressemblance.module').then(m => m.MicroserviceRessemblanceModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class GatewayEntityModule {}
