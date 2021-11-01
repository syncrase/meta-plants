import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { PlanteComponentsPage, PlanteDeleteDialog, PlanteUpdatePage } from './plante.page-object';

const expect = chai.expect;

describe('Plante e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let planteComponentsPage: PlanteComponentsPage;
  let planteUpdatePage: PlanteUpdatePage;
  let planteDeleteDialog: PlanteDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Plantes', async () => {
    await navBarPage.goToEntity('plante');
    planteComponentsPage = new PlanteComponentsPage();
    await browser.wait(ec.visibilityOf(planteComponentsPage.title), 5000);
    expect(await planteComponentsPage.getTitle()).to.eq('gatewayApp.microservicePlante.home.title');
    await browser.wait(ec.or(ec.visibilityOf(planteComponentsPage.entities), ec.visibilityOf(planteComponentsPage.noResult)), 1000);
  });

  it('should load create Plante page', async () => {
    await planteComponentsPage.clickOnCreateButton();
    planteUpdatePage = new PlanteUpdatePage();
    expect(await planteUpdatePage.getPageTitle()).to.eq('gatewayApp.microservicePlante.home.createOrEditLabel');
    await planteUpdatePage.cancel();
  });

  it('should create and save Plantes', async () => {
    const nbButtonsBeforeCreate = await planteComponentsPage.countDeleteButtons();

    await planteComponentsPage.clickOnCreateButton();

    await promise.all([
      planteUpdatePage.setNomLatinInput('nomLatin'),
      planteUpdatePage.setEntretienInput('entretien'),
      planteUpdatePage.setHistoireInput('histoire'),
      planteUpdatePage.setExpositionInput('exposition'),
      planteUpdatePage.setRusticiteInput('rusticite'),
      planteUpdatePage.cycleDeVieSelectLastOption(),
      planteUpdatePage.classificationSelectLastOption(),
      // planteUpdatePage.nomsVernaculairesSelectLastOption(),
    ]);

    expect(await planteUpdatePage.getNomLatinInput()).to.eq('nomLatin', 'Expected NomLatin value to be equals to nomLatin');
    expect(await planteUpdatePage.getEntretienInput()).to.eq('entretien', 'Expected Entretien value to be equals to entretien');
    expect(await planteUpdatePage.getHistoireInput()).to.eq('histoire', 'Expected Histoire value to be equals to histoire');
    expect(await planteUpdatePage.getExpositionInput()).to.eq('exposition', 'Expected Exposition value to be equals to exposition');
    expect(await planteUpdatePage.getRusticiteInput()).to.eq('rusticite', 'Expected Rusticite value to be equals to rusticite');

    await planteUpdatePage.save();
    expect(await planteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await planteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Plante', async () => {
    const nbButtonsBeforeDelete = await planteComponentsPage.countDeleteButtons();
    await planteComponentsPage.clickOnLastDeleteButton();

    planteDeleteDialog = new PlanteDeleteDialog();
    expect(await planteDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microservicePlante.delete.question');
    await planteDeleteDialog.clickOnConfirmButton();

    expect(await planteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
