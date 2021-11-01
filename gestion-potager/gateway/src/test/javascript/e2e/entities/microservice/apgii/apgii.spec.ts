import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { APGIIComponentsPage, APGIIDeleteDialog, APGIIUpdatePage } from './apgii.page-object';

const expect = chai.expect;

describe('APGII e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let aPGIIComponentsPage: APGIIComponentsPage;
  let aPGIIUpdatePage: APGIIUpdatePage;
  let aPGIIDeleteDialog: APGIIDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load APGIIS', async () => {
    await navBarPage.goToEntity('apgii');
    aPGIIComponentsPage = new APGIIComponentsPage();
    await browser.wait(ec.visibilityOf(aPGIIComponentsPage.title), 5000);
    expect(await aPGIIComponentsPage.getTitle()).to.eq('gatewayApp.microserviceAPgii.home.title');
    await browser.wait(ec.or(ec.visibilityOf(aPGIIComponentsPage.entities), ec.visibilityOf(aPGIIComponentsPage.noResult)), 1000);
  });

  it('should load create APGII page', async () => {
    await aPGIIComponentsPage.clickOnCreateButton();
    aPGIIUpdatePage = new APGIIUpdatePage();
    expect(await aPGIIUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceAPgii.home.createOrEditLabel');
    await aPGIIUpdatePage.cancel();
  });

  it('should create and save APGIIS', async () => {
    const nbButtonsBeforeCreate = await aPGIIComponentsPage.countDeleteButtons();

    await aPGIIComponentsPage.clickOnCreateButton();

    await promise.all([aPGIIUpdatePage.setOrdreInput('ordre'), aPGIIUpdatePage.setFamilleInput('famille')]);

    expect(await aPGIIUpdatePage.getOrdreInput()).to.eq('ordre', 'Expected Ordre value to be equals to ordre');
    expect(await aPGIIUpdatePage.getFamilleInput()).to.eq('famille', 'Expected Famille value to be equals to famille');

    await aPGIIUpdatePage.save();
    expect(await aPGIIUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await aPGIIComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last APGII', async () => {
    const nbButtonsBeforeDelete = await aPGIIComponentsPage.countDeleteButtons();
    await aPGIIComponentsPage.clickOnLastDeleteButton();

    aPGIIDeleteDialog = new APGIIDeleteDialog();
    expect(await aPGIIDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceAPgii.delete.question');
    await aPGIIDeleteDialog.clickOnConfirmButton();

    expect(await aPGIIComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
