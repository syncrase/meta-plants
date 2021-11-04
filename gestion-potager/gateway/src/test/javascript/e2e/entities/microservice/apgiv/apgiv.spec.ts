import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { APGIVComponentsPage, APGIVDeleteDialog, APGIVUpdatePage } from './apgiv.page-object';

const expect = chai.expect;

describe('APGIV e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let aPGIVComponentsPage: APGIVComponentsPage;
  let aPGIVUpdatePage: APGIVUpdatePage;
  let aPGIVDeleteDialog: APGIVDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load APGIVS', async () => {
    await navBarPage.goToEntity('apgiv');
    aPGIVComponentsPage = new APGIVComponentsPage();
    await browser.wait(ec.visibilityOf(aPGIVComponentsPage.title), 5000);
    expect(await aPGIVComponentsPage.getTitle()).to.eq('gatewayApp.microserviceAPgiv.home.title');
    await browser.wait(ec.or(ec.visibilityOf(aPGIVComponentsPage.entities), ec.visibilityOf(aPGIVComponentsPage.noResult)), 1000);
  });

  it('should load create APGIV page', async () => {
    await aPGIVComponentsPage.clickOnCreateButton();
    aPGIVUpdatePage = new APGIVUpdatePage();
    expect(await aPGIVUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceAPgiv.home.createOrEditLabel');
    await aPGIVUpdatePage.cancel();
  });

  it('should create and save APGIVS', async () => {
    const nbButtonsBeforeCreate = await aPGIVComponentsPage.countDeleteButtons();

    await aPGIVComponentsPage.clickOnCreateButton();

    await promise.all([aPGIVUpdatePage.setOrdreInput('ordre'), aPGIVUpdatePage.setFamilleInput('famille')]);

    await aPGIVUpdatePage.save();
    expect(await aPGIVUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await aPGIVComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last APGIV', async () => {
    const nbButtonsBeforeDelete = await aPGIVComponentsPage.countDeleteButtons();
    await aPGIVComponentsPage.clickOnLastDeleteButton();

    aPGIVDeleteDialog = new APGIVDeleteDialog();
    expect(await aPGIVDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceAPgiv.delete.question');
    await aPGIVDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(aPGIVComponentsPage.title), 5000);

    expect(await aPGIVComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
