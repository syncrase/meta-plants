import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { APGIIIComponentsPage, APGIIIDeleteDialog, APGIIIUpdatePage } from './apgiii.page-object';

const expect = chai.expect;

describe('APGIII e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let aPGIIIComponentsPage: APGIIIComponentsPage;
  let aPGIIIUpdatePage: APGIIIUpdatePage;
  let aPGIIIDeleteDialog: APGIIIDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load APGIIIS', async () => {
    await navBarPage.goToEntity('apgiii');
    aPGIIIComponentsPage = new APGIIIComponentsPage();
    await browser.wait(ec.visibilityOf(aPGIIIComponentsPage.title), 5000);
    expect(await aPGIIIComponentsPage.getTitle()).to.eq('gatewayApp.microserviceAPgiii.home.title');
    await browser.wait(ec.or(ec.visibilityOf(aPGIIIComponentsPage.entities), ec.visibilityOf(aPGIIIComponentsPage.noResult)), 1000);
  });

  it('should load create APGIII page', async () => {
    await aPGIIIComponentsPage.clickOnCreateButton();
    aPGIIIUpdatePage = new APGIIIUpdatePage();
    expect(await aPGIIIUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceAPgiii.home.createOrEditLabel');
    await aPGIIIUpdatePage.cancel();
  });

  it('should create and save APGIIIS', async () => {
    const nbButtonsBeforeCreate = await aPGIIIComponentsPage.countDeleteButtons();

    await aPGIIIComponentsPage.clickOnCreateButton();

    await promise.all([aPGIIIUpdatePage.setOrdreInput('ordre'), aPGIIIUpdatePage.setFamilleInput('famille')]);

    expect(await aPGIIIUpdatePage.getOrdreInput()).to.eq('ordre', 'Expected Ordre value to be equals to ordre');
    expect(await aPGIIIUpdatePage.getFamilleInput()).to.eq('famille', 'Expected Famille value to be equals to famille');

    await aPGIIIUpdatePage.save();
    expect(await aPGIIIUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await aPGIIIComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last APGIII', async () => {
    const nbButtonsBeforeDelete = await aPGIIIComponentsPage.countDeleteButtons();
    await aPGIIIComponentsPage.clickOnLastDeleteButton();

    aPGIIIDeleteDialog = new APGIIIDeleteDialog();
    expect(await aPGIIIDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceAPgiii.delete.question');
    await aPGIIIDeleteDialog.clickOnConfirmButton();

    expect(await aPGIIIComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
