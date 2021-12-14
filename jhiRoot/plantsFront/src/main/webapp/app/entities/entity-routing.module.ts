import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'super-famille',
        data: { pageTitle: 'SuperFamilles' },
        loadChildren: () => import('./classificationMS/super-famille/super-famille.module').then(m => m.ClassificationMsSuperFamilleModule),
      },
      {
        path: 'sous-division',
        data: { pageTitle: 'SousDivisions' },
        loadChildren: () => import('./classificationMS/sous-division/sous-division.module').then(m => m.ClassificationMsSousDivisionModule),
      },
      {
        path: 'sous-tribu',
        data: { pageTitle: 'SousTribus' },
        loadChildren: () => import('./classificationMS/sous-tribu/sous-tribu.module').then(m => m.ClassificationMsSousTribuModule),
      },
      {
        path: 'variete',
        data: { pageTitle: 'Varietes' },
        loadChildren: () => import('./classificationMS/variete/variete.module').then(m => m.ClassificationMsVarieteModule),
      },
      {
        path: 'apgi-plante',
        data: { pageTitle: 'APGIPlantes' },
        loadChildren: () => import('./plantsMS/apgi-plante/apgi-plante.module').then(m => m.PlantsMsAPGIPlanteModule),
      },
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
        path: 'infra-classe',
        data: { pageTitle: 'InfraClasses' },
        loadChildren: () => import('./classificationMS/infra-classe/infra-classe.module').then(m => m.ClassificationMsInfraClasseModule),
      },
      {
        path: 'sous-ordre',
        data: { pageTitle: 'SousOrdres' },
        loadChildren: () => import('./classificationMS/sous-ordre/sous-ordre.module').then(m => m.ClassificationMsSousOrdreModule),
      },
      {
        path: 'tribu',
        data: { pageTitle: 'Tribus' },
        loadChildren: () => import('./classificationMS/tribu/tribu.module').then(m => m.ClassificationMsTribuModule),
      },
      {
        path: 'forme',
        data: { pageTitle: 'Formes' },
        loadChildren: () => import('./classificationMS/forme/forme.module').then(m => m.ClassificationMsFormeModule),
      },
      {
        path: 'apgiv-plante',
        data: { pageTitle: 'APGIVPlantes' },
        loadChildren: () => import('./plantsMS/apgiv-plante/apgiv-plante.module').then(m => m.PlantsMsAPGIVPlanteModule),
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
        path: 'section',
        data: { pageTitle: 'Sections' },
        loadChildren: () => import('./classificationMS/section/section.module').then(m => m.ClassificationMsSectionModule),
      },
      {
        path: 'strate',
        data: { pageTitle: 'Strates' },
        loadChildren: () => import('./plantsMS/strate/strate.module').then(m => m.PlantsMsStrateModule),
      },
      {
        path: 'super-classe',
        data: { pageTitle: 'SuperClasses' },
        loadChildren: () => import('./classificationMS/super-classe/super-classe.module').then(m => m.ClassificationMsSuperClasseModule),
      },
      {
        path: 'clade',
        data: { pageTitle: 'Clades' },
        loadChildren: () => import('./plantsMS/clade/clade.module').then(m => m.PlantsMsCladeModule),
      },
      {
        path: 'sous-espece',
        data: { pageTitle: 'SousEspeces' },
        loadChildren: () => import('./classificationMS/sous-espece/sous-espece.module').then(m => m.ClassificationMsSousEspeceModule),
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
        path: 'apgii-plante',
        data: { pageTitle: 'APGIIPlantes' },
        loadChildren: () => import('./plantsMS/apgii-plante/apgii-plante.module').then(m => m.PlantsMsAPGIIPlanteModule),
      },
      {
        path: 'infra-regne',
        data: { pageTitle: 'InfraRegnes' },
        loadChildren: () => import('./classificationMS/infra-regne/infra-regne.module').then(m => m.ClassificationMsInfraRegneModule),
      },
      {
        path: 'apgiii-plante',
        data: { pageTitle: 'APGIIIPlantes' },
        loadChildren: () => import('./plantsMS/apgiii-plante/apgiii-plante.module').then(m => m.PlantsMsAPGIIIPlanteModule),
      },
      {
        path: 'genre',
        data: { pageTitle: 'Genres' },
        loadChildren: () => import('./classificationMS/genre/genre.module').then(m => m.ClassificationMsGenreModule),
      },
      {
        path: 'regne',
        data: { pageTitle: 'Regnes' },
        loadChildren: () => import('./classificationMS/regne/regne.module').then(m => m.ClassificationMsRegneModule),
      },
      {
        path: 'sous-variete',
        data: { pageTitle: 'SousVarietes' },
        loadChildren: () => import('./classificationMS/sous-variete/sous-variete.module').then(m => m.ClassificationMsSousVarieteModule),
      },
      {
        path: 'nom-vernaculaire',
        data: { pageTitle: 'NomVernaculaires' },
        loadChildren: () => import('./plantsMS/nom-vernaculaire/nom-vernaculaire.module').then(m => m.PlantsMsNomVernaculaireModule),
      },
      {
        path: 'sous-famille',
        data: { pageTitle: 'SousFamilles' },
        loadChildren: () => import('./classificationMS/sous-famille/sous-famille.module').then(m => m.ClassificationMsSousFamilleModule),
      },
      {
        path: 'super-ordre',
        data: { pageTitle: 'SuperOrdres' },
        loadChildren: () => import('./classificationMS/super-ordre/super-ordre.module').then(m => m.ClassificationMsSuperOrdreModule),
      },
      {
        path: 'sous-classe',
        data: { pageTitle: 'SousClasses' },
        loadChildren: () => import('./classificationMS/sous-classe/sous-classe.module').then(m => m.ClassificationMsSousClasseModule),
      },
      {
        path: 'reproduction',
        data: { pageTitle: 'Reproductions' },
        loadChildren: () => import('./plantsMS/reproduction/reproduction.module').then(m => m.PlantsMsReproductionModule),
      },
      {
        path: 'infra-embranchement',
        data: { pageTitle: 'InfraEmbranchements' },
        loadChildren: () =>
          import('./classificationMS/infra-embranchement/infra-embranchement.module').then(m => m.ClassificationMsInfraEmbranchementModule),
      },
      {
        path: 'sous-forme',
        data: { pageTitle: 'SousFormes' },
        loadChildren: () => import('./classificationMS/sous-forme/sous-forme.module').then(m => m.ClassificationMsSousFormeModule),
      },
      {
        path: 'ressemblance',
        data: { pageTitle: 'Ressemblances' },
        loadChildren: () => import('./plantsMS/ressemblance/ressemblance.module').then(m => m.PlantsMsRessemblanceModule),
      },
      {
        path: 'cronquist-plante',
        data: { pageTitle: 'CronquistPlantes' },
        loadChildren: () => import('./plantsMS/cronquist-plante/cronquist-plante.module').then(m => m.PlantsMsCronquistPlanteModule),
      },
      {
        path: 'rameau',
        data: { pageTitle: 'Rameaus' },
        loadChildren: () => import('./classificationMS/rameau/rameau.module').then(m => m.ClassificationMsRameauModule),
      },
      {
        path: 'super-division',
        data: { pageTitle: 'SuperDivisions' },
        loadChildren: () =>
          import('./classificationMS/super-division/super-division.module').then(m => m.ClassificationMsSuperDivisionModule),
      },
      {
        path: 'espece',
        data: { pageTitle: 'Especes' },
        loadChildren: () => import('./classificationMS/espece/espece.module').then(m => m.ClassificationMsEspeceModule),
      },
      {
        path: 'classe',
        data: { pageTitle: 'Classes' },
        loadChildren: () => import('./classificationMS/classe/classe.module').then(m => m.ClassificationMsClasseModule),
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
        path: 'sous-regne',
        data: { pageTitle: 'SousRegnes' },
        loadChildren: () => import('./classificationMS/sous-regne/sous-regne.module').then(m => m.ClassificationMsSousRegneModule),
      },
      {
        path: 'mois',
        data: { pageTitle: 'Mois' },
        loadChildren: () => import('./plantsMS/mois/mois.module').then(m => m.PlantsMsMoisModule),
      },
      {
        path: 'infra-ordre',
        data: { pageTitle: 'InfraOrdres' },
        loadChildren: () => import('./classificationMS/infra-ordre/infra-ordre.module').then(m => m.ClassificationMsInfraOrdreModule),
      },
      {
        path: 'classification',
        data: { pageTitle: 'Classifications' },
        loadChildren: () => import('./plantsMS/classification/classification.module').then(m => m.PlantsMsClassificationModule),
      },
      {
        path: 'micro-ordre',
        data: { pageTitle: 'MicroOrdres' },
        loadChildren: () => import('./classificationMS/micro-ordre/micro-ordre.module').then(m => m.ClassificationMsMicroOrdreModule),
      },
      {
        path: 'sous-genre',
        data: { pageTitle: 'SousGenres' },
        loadChildren: () => import('./classificationMS/sous-genre/sous-genre.module').then(m => m.ClassificationMsSousGenreModule),
      },
      {
        path: 'division',
        data: { pageTitle: 'Divisions' },
        loadChildren: () => import('./classificationMS/division/division.module').then(m => m.ClassificationMsDivisionModule),
      },
      {
        path: 'super-regne',
        data: { pageTitle: 'SuperRegnes' },
        loadChildren: () => import('./classificationMS/super-regne/super-regne.module').then(m => m.ClassificationMsSuperRegneModule),
      },
      {
        path: 'sous-section',
        data: { pageTitle: 'SousSections' },
        loadChildren: () => import('./classificationMS/sous-section/sous-section.module').then(m => m.ClassificationMsSousSectionModule),
      },
      {
        path: 'germination',
        data: { pageTitle: 'Germinations' },
        loadChildren: () => import('./plantsMS/germination/germination.module').then(m => m.PlantsMsGerminationModule),
      },
      {
        path: 'ordre',
        data: { pageTitle: 'Ordres' },
        loadChildren: () => import('./classificationMS/ordre/ordre.module').then(m => m.ClassificationMsOrdreModule),
      },
      {
        path: 'ensoleillement',
        data: { pageTitle: 'Ensoleillements' },
        loadChildren: () => import('./plantsMS/ensoleillement/ensoleillement.module').then(m => m.PlantsMsEnsoleillementModule),
      },
      {
        path: 'raunkier-plante',
        data: { pageTitle: 'RaunkierPlantes' },
        loadChildren: () => import('./plantsMS/raunkier-plante/raunkier-plante.module').then(m => m.PlantsMsRaunkierPlanteModule),
      },
      {
        path: 'famille',
        data: { pageTitle: 'Familles' },
        loadChildren: () => import('./classificationMS/famille/famille.module').then(m => m.ClassificationMsFamilleModule),
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
      {
        path: 'micro-embranchement',
        data: { pageTitle: 'MicroEmbranchements' },
        loadChildren: () =>
          import('./classificationMS/micro-embranchement/micro-embranchement.module').then(m => m.ClassificationMsMicroEmbranchementModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
