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
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load APGIIS', async () => {
    await navBarPage.goToEntity('apgii');
    aPGIIComponentsPage = new APGIIComponentsPage();
    await browser.wait(ec.visibilityOf(aPGIIComponentsPage.title), 5000);
    expect(await aPGIIComponentsPage.getTitle()).to.eq('APGIIS');
    await browser.wait(ec.or(ec.visibilityOf(aPGIIComponentsPage.entities), ec.visibilityOf(aPGIIComponentsPage.noResult)), 1000);
  });

  it('should load create APGII page', async () => {
    await aPGIIComponentsPage.clickOnCreateButton();
    aPGIIUpdatePage = new APGIIUpdatePage();
    expect(await aPGIIUpdatePage.getPageTitle()).to.eq('Create or edit a APGII');
    await aPGIIUpdatePage.cancel();
  });

  it('should create and save APGIIS', async () => {
    const nbButtonsBeforeCreate = await aPGIIComponentsPage.countDeleteButtons();

    await aPGIIComponentsPage.clickOnCreateButton();

    await promise.all([aPGIIUpdatePage.setOrdreInput('ordre'), aPGIIUpdatePage.setFamilleInput('famille')]);

    await aPGIIUpdatePage.save();
    expect(await aPGIIUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await aPGIIComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last APGII', async () => {
    const nbButtonsBeforeDelete = await aPGIIComponentsPage.countDeleteButtons();
    await aPGIIComponentsPage.clickOnLastDeleteButton();

    aPGIIDeleteDialog = new APGIIDeleteDialog();
    expect(await aPGIIDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this APGII?');
    await aPGIIDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(aPGIIComponentsPage.title), 5000);

    expect(await aPGIIComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
