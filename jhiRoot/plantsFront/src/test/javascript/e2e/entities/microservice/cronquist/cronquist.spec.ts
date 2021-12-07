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
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Cronquists', async () => {
    await navBarPage.goToEntity('cronquist');
    cronquistComponentsPage = new CronquistComponentsPage();
    await browser.wait(ec.visibilityOf(cronquistComponentsPage.title), 5000);
    expect(await cronquistComponentsPage.getTitle()).to.eq('Cronquists');
    await browser.wait(ec.or(ec.visibilityOf(cronquistComponentsPage.entities), ec.visibilityOf(cronquistComponentsPage.noResult)), 1000);
  });

  it('should load create Cronquist page', async () => {
    await cronquistComponentsPage.clickOnCreateButton();
    cronquistUpdatePage = new CronquistUpdatePage();
    expect(await cronquistUpdatePage.getPageTitle()).to.eq('Create or edit a Cronquist');
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
      cronquistUpdatePage.setEspeceInput('espece'),
    ]);

    await cronquistUpdatePage.save();
    expect(await cronquistUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cronquistComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Cronquist', async () => {
    const nbButtonsBeforeDelete = await cronquistComponentsPage.countDeleteButtons();
    await cronquistComponentsPage.clickOnLastDeleteButton();

    cronquistDeleteDialog = new CronquistDeleteDialog();
    expect(await cronquistDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Cronquist?');
    await cronquistDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cronquistComponentsPage.title), 5000);

    expect(await cronquistComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
