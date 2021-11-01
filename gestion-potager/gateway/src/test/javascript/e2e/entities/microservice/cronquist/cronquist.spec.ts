import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CronquistComponentsPage, CronquistDeleteDialog, CronquistUpdatePage } from './cronquist.page-object';

const expect = chai.expect;

describe('Cronquist e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cronquistComponentsPage: CronquistComponentsPage;
  let cronquistUpdatePage: CronquistUpdatePage;
  let cronquistDeleteDialog: CronquistDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Cronquists', async () => {
    await navBarPage.goToEntity('cronquist');
    cronquistComponentsPage = new CronquistComponentsPage();
    await browser.wait(ec.visibilityOf(cronquistComponentsPage.title), 5000);
    expect(await cronquistComponentsPage.getTitle()).to.eq('gatewayApp.microserviceCronquist.home.title');
    await browser.wait(ec.or(ec.visibilityOf(cronquistComponentsPage.entities), ec.visibilityOf(cronquistComponentsPage.noResult)), 1000);
  });

  it('should load create Cronquist page', async () => {
    await cronquistComponentsPage.clickOnCreateButton();
    cronquistUpdatePage = new CronquistUpdatePage();
    expect(await cronquistUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceCronquist.home.createOrEditLabel');
    await cronquistUpdatePage.cancel();
  });

  it('should create and save Cronquists', async () => {
    const nbButtonsBeforeCreate = await cronquistComponentsPage.countDeleteButtons();

    await cronquistComponentsPage.clickOnCreateButton();

    await promise.all([
      cronquistUpdatePage.setRegneInput('regne'),
      cronquistUpdatePage.setSousRegneInput('sousRegne'),
      cronquistUpdatePage.setDivisionInput('division'),
      cronquistUpdatePage.setClasseInput('classe'),
      cronquistUpdatePage.setSousClasseInput('sousClasse'),
      cronquistUpdatePage.setOrdreInput('ordre'),
      cronquistUpdatePage.setFamilleInput('famille'),
      cronquistUpdatePage.setGenreInput('genre'),
    ]);

    expect(await cronquistUpdatePage.getRegneInput()).to.eq('regne', 'Expected Regne value to be equals to regne');
    expect(await cronquistUpdatePage.getSousRegneInput()).to.eq('sousRegne', 'Expected SousRegne value to be equals to sousRegne');
    expect(await cronquistUpdatePage.getDivisionInput()).to.eq('division', 'Expected Division value to be equals to division');
    expect(await cronquistUpdatePage.getClasseInput()).to.eq('classe', 'Expected Classe value to be equals to classe');
    expect(await cronquistUpdatePage.getSousClasseInput()).to.eq('sousClasse', 'Expected SousClasse value to be equals to sousClasse');
    expect(await cronquistUpdatePage.getOrdreInput()).to.eq('ordre', 'Expected Ordre value to be equals to ordre');
    expect(await cronquistUpdatePage.getFamilleInput()).to.eq('famille', 'Expected Famille value to be equals to famille');
    expect(await cronquistUpdatePage.getGenreInput()).to.eq('genre', 'Expected Genre value to be equals to genre');

    await cronquistUpdatePage.save();
    expect(await cronquistUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cronquistComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Cronquist', async () => {
    const nbButtonsBeforeDelete = await cronquistComponentsPage.countDeleteButtons();
    await cronquistComponentsPage.clickOnLastDeleteButton();

    cronquistDeleteDialog = new CronquistDeleteDialog();
    expect(await cronquistDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceCronquist.delete.question');
    await cronquistDeleteDialog.clickOnConfirmButton();

    expect(await cronquistComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
