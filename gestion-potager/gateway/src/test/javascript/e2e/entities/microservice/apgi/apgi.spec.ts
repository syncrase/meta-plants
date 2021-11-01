import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { APGIComponentsPage, APGIDeleteDialog, APGIUpdatePage } from './apgi.page-object';

const expect = chai.expect;

describe('APGI e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let aPGIComponentsPage: APGIComponentsPage;
  let aPGIUpdatePage: APGIUpdatePage;
  let aPGIDeleteDialog: APGIDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load APGIS', async () => {
    await navBarPage.goToEntity('apgi');
    aPGIComponentsPage = new APGIComponentsPage();
    await browser.wait(ec.visibilityOf(aPGIComponentsPage.title), 5000);
    expect(await aPGIComponentsPage.getTitle()).to.eq('gatewayApp.microserviceAPgi.home.title');
    await browser.wait(ec.or(ec.visibilityOf(aPGIComponentsPage.entities), ec.visibilityOf(aPGIComponentsPage.noResult)), 1000);
  });

  it('should load create APGI page', async () => {
    await aPGIComponentsPage.clickOnCreateButton();
    aPGIUpdatePage = new APGIUpdatePage();
    expect(await aPGIUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceAPgi.home.createOrEditLabel');
    await aPGIUpdatePage.cancel();
  });

  it('should create and save APGIS', async () => {
    const nbButtonsBeforeCreate = await aPGIComponentsPage.countDeleteButtons();

    await aPGIComponentsPage.clickOnCreateButton();

    await promise.all([aPGIUpdatePage.setOrdreInput('ordre'), aPGIUpdatePage.setFamilleInput('famille')]);

    expect(await aPGIUpdatePage.getOrdreInput()).to.eq('ordre', 'Expected Ordre value to be equals to ordre');
    expect(await aPGIUpdatePage.getFamilleInput()).to.eq('famille', 'Expected Famille value to be equals to famille');

    await aPGIUpdatePage.save();
    expect(await aPGIUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await aPGIComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last APGI', async () => {
    const nbButtonsBeforeDelete = await aPGIComponentsPage.countDeleteButtons();
    await aPGIComponentsPage.clickOnLastDeleteButton();

    aPGIDeleteDialog = new APGIDeleteDialog();
    expect(await aPGIDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceAPgi.delete.question');
    await aPGIDeleteDialog.clickOnConfirmButton();

    expect(await aPGIComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
