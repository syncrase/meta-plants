import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { FeuillageComponentsPage, FeuillageDeleteDialog, FeuillageUpdatePage } from './feuillage.page-object';

const expect = chai.expect;

describe('Feuillage e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let feuillageComponentsPage: FeuillageComponentsPage;
  let feuillageUpdatePage: FeuillageUpdatePage;
  let feuillageDeleteDialog: FeuillageDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Feuillages', async () => {
    await navBarPage.goToEntity('feuillage');
    feuillageComponentsPage = new FeuillageComponentsPage();
    await browser.wait(ec.visibilityOf(feuillageComponentsPage.title), 5000);
    expect(await feuillageComponentsPage.getTitle()).to.eq('gatewayApp.microserviceFeuillage.home.title');
    await browser.wait(ec.or(ec.visibilityOf(feuillageComponentsPage.entities), ec.visibilityOf(feuillageComponentsPage.noResult)), 1000);
  });

  it('should load create Feuillage page', async () => {
    await feuillageComponentsPage.clickOnCreateButton();
    feuillageUpdatePage = new FeuillageUpdatePage();
    expect(await feuillageUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceFeuillage.home.createOrEditLabel');
    await feuillageUpdatePage.cancel();
  });

  it('should create and save Feuillages', async () => {
    const nbButtonsBeforeCreate = await feuillageComponentsPage.countDeleteButtons();

    await feuillageComponentsPage.clickOnCreateButton();

    await promise.all([feuillageUpdatePage.setTypeInput('type')]);

    await feuillageUpdatePage.save();
    expect(await feuillageUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await feuillageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Feuillage', async () => {
    const nbButtonsBeforeDelete = await feuillageComponentsPage.countDeleteButtons();
    await feuillageComponentsPage.clickOnLastDeleteButton();

    feuillageDeleteDialog = new FeuillageDeleteDialog();
    expect(await feuillageDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceFeuillage.delete.question');
    await feuillageDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(feuillageComponentsPage.title), 5000);

    expect(await feuillageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
