import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CronquistPlanteComponentsPage, CronquistPlanteDeleteDialog, CronquistPlanteUpdatePage } from './cronquist-plante.page-object';

const expect = chai.expect;

describe('CronquistPlante e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cronquistPlanteComponentsPage: CronquistPlanteComponentsPage;
  let cronquistPlanteUpdatePage: CronquistPlanteUpdatePage;
  let cronquistPlanteDeleteDialog: CronquistPlanteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CronquistPlantes', async () => {
    await navBarPage.goToEntity('cronquist-plante');
    cronquistPlanteComponentsPage = new CronquistPlanteComponentsPage();
    await browser.wait(ec.visibilityOf(cronquistPlanteComponentsPage.title), 5000);
    expect(await cronquistPlanteComponentsPage.getTitle()).to.eq('Cronquist Plantes');
    await browser.wait(
      ec.or(ec.visibilityOf(cronquistPlanteComponentsPage.entities), ec.visibilityOf(cronquistPlanteComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CronquistPlante page', async () => {
    await cronquistPlanteComponentsPage.clickOnCreateButton();
    cronquistPlanteUpdatePage = new CronquistPlanteUpdatePage();
    expect(await cronquistPlanteUpdatePage.getPageTitle()).to.eq('Create or edit a Cronquist Plante');
    await cronquistPlanteUpdatePage.cancel();
  });

  it('should create and save CronquistPlantes', async () => {
    const nbButtonsBeforeCreate = await cronquistPlanteComponentsPage.countDeleteButtons();

    await cronquistPlanteComponentsPage.clickOnCreateButton();

    await promise.all([
      cronquistPlanteUpdatePage.setSuperRegneInput('superRegne'),
      cronquistPlanteUpdatePage.setRegneInput('regne'),
      cronquistPlanteUpdatePage.setSousRegneInput('sousRegne'),
      cronquistPlanteUpdatePage.setRameauInput('rameau'),
      cronquistPlanteUpdatePage.setInfraRegneInput('infraRegne'),
      cronquistPlanteUpdatePage.setSuperDivisionInput('superDivision'),
      cronquistPlanteUpdatePage.setDivisionInput('division'),
      cronquistPlanteUpdatePage.setSousDivisionInput('sousDivision'),
      cronquistPlanteUpdatePage.setInfraEmbranchementInput('infraEmbranchement'),
      cronquistPlanteUpdatePage.setMicroEmbranchementInput('microEmbranchement'),
      cronquistPlanteUpdatePage.setSuperClasseInput('superClasse'),
      cronquistPlanteUpdatePage.setClasseInput('classe'),
      cronquistPlanteUpdatePage.setSousClasseInput('sousClasse'),
      cronquistPlanteUpdatePage.setInfraClasseInput('infraClasse'),
      cronquistPlanteUpdatePage.setSuperOrdreInput('superOrdre'),
      cronquistPlanteUpdatePage.setOrdreInput('ordre'),
      cronquistPlanteUpdatePage.setSousOrdreInput('sousOrdre'),
      cronquistPlanteUpdatePage.setInfraOrdreInput('infraOrdre'),
      cronquistPlanteUpdatePage.setMicroOrdreInput('microOrdre'),
      cronquistPlanteUpdatePage.setSuperFamilleInput('superFamille'),
      cronquistPlanteUpdatePage.setFamilleInput('famille'),
      cronquistPlanteUpdatePage.setSousFamilleInput('sousFamille'),
      cronquistPlanteUpdatePage.setTribuInput('tribu'),
      cronquistPlanteUpdatePage.setSousTribuInput('sousTribu'),
      cronquistPlanteUpdatePage.setGenreInput('genre'),
      cronquistPlanteUpdatePage.setSousGenreInput('sousGenre'),
      cronquistPlanteUpdatePage.setSectionInput('section'),
      cronquistPlanteUpdatePage.setSousSectionInput('sousSection'),
      cronquistPlanteUpdatePage.setEspeceInput('espece'),
      cronquistPlanteUpdatePage.setSousEspeceInput('sousEspece'),
      cronquistPlanteUpdatePage.setVarieteInput('variete'),
      cronquistPlanteUpdatePage.setSousVarieteInput('sousVariete'),
      cronquistPlanteUpdatePage.setFormeInput('forme'),
    ]);

    await cronquistPlanteUpdatePage.save();
    expect(await cronquistPlanteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cronquistPlanteComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CronquistPlante', async () => {
    const nbButtonsBeforeDelete = await cronquistPlanteComponentsPage.countDeleteButtons();
    await cronquistPlanteComponentsPage.clickOnLastDeleteButton();

    cronquistPlanteDeleteDialog = new CronquistPlanteDeleteDialog();
    expect(await cronquistPlanteDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Cronquist Plante?');
    await cronquistPlanteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cronquistPlanteComponentsPage.title), 5000);

    expect(await cronquistPlanteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
