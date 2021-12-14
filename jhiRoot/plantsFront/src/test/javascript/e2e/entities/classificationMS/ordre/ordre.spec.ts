import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { OrdreComponentsPage, OrdreDeleteDialog, OrdreUpdatePage } from './ordre.page-object';

const expect = chai.expect;

describe('Ordre e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ordreComponentsPage: OrdreComponentsPage;
  let ordreUpdatePage: OrdreUpdatePage;
  let ordreDeleteDialog: OrdreDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Ordres', async () => {
    await navBarPage.goToEntity('ordre');
    ordreComponentsPage = new OrdreComponentsPage();
    await browser.wait(ec.visibilityOf(ordreComponentsPage.title), 5000);
    expect(await ordreComponentsPage.getTitle()).to.eq('Ordres');
    await browser.wait(ec.or(ec.visibilityOf(ordreComponentsPage.entities), ec.visibilityOf(ordreComponentsPage.noResult)), 1000);
  });

  it('should load create Ordre page', async () => {
    await ordreComponentsPage.clickOnCreateButton();
    ordreUpdatePage = new OrdreUpdatePage();
    expect(await ordreUpdatePage.getPageTitle()).to.eq('Create or edit a Ordre');
    await ordreUpdatePage.cancel();
  });

  it('should create and save Ordres', async () => {
    const nbButtonsBeforeCreate = await ordreComponentsPage.countDeleteButtons();

    await ordreComponentsPage.clickOnCreateButton();

    await promise.all([
      ordreUpdatePage.setNomFrInput('nomFr'),
      ordreUpdatePage.setNomLatinInput('nomLatin'),
      ordreUpdatePage.superOrdreSelectLastOption(),
      ordreUpdatePage.ordreSelectLastOption(),
    ]);

    await ordreUpdatePage.save();
    expect(await ordreUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ordreComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Ordre', async () => {
    const nbButtonsBeforeDelete = await ordreComponentsPage.countDeleteButtons();
    await ordreComponentsPage.clickOnLastDeleteButton();

    ordreDeleteDialog = new OrdreDeleteDialog();
    expect(await ordreDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Ordre?');
    await ordreDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(ordreComponentsPage.title), 5000);

    expect(await ordreComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
