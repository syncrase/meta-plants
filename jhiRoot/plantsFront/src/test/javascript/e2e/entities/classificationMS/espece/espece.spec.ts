import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { EspeceComponentsPage, EspeceDeleteDialog, EspeceUpdatePage } from './espece.page-object';

const expect = chai.expect;

describe('Espece e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let especeComponentsPage: EspeceComponentsPage;
  let especeUpdatePage: EspeceUpdatePage;
  let especeDeleteDialog: EspeceDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Especes', async () => {
    await navBarPage.goToEntity('espece');
    especeComponentsPage = new EspeceComponentsPage();
    await browser.wait(ec.visibilityOf(especeComponentsPage.title), 5000);
    expect(await especeComponentsPage.getTitle()).to.eq('Especes');
    await browser.wait(ec.or(ec.visibilityOf(especeComponentsPage.entities), ec.visibilityOf(especeComponentsPage.noResult)), 1000);
  });

  it('should load create Espece page', async () => {
    await especeComponentsPage.clickOnCreateButton();
    especeUpdatePage = new EspeceUpdatePage();
    expect(await especeUpdatePage.getPageTitle()).to.eq('Create or edit a Espece');
    await especeUpdatePage.cancel();
  });

  it('should create and save Especes', async () => {
    const nbButtonsBeforeCreate = await especeComponentsPage.countDeleteButtons();

    await especeComponentsPage.clickOnCreateButton();

    await promise.all([
      especeUpdatePage.setNomFrInput('nomFr'),
      especeUpdatePage.setNomLatinInput('nomLatin'),
      especeUpdatePage.sousSectionSelectLastOption(),
      especeUpdatePage.especeSelectLastOption(),
    ]);

    await especeUpdatePage.save();
    expect(await especeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await especeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Espece', async () => {
    const nbButtonsBeforeDelete = await especeComponentsPage.countDeleteButtons();
    await especeComponentsPage.clickOnLastDeleteButton();

    especeDeleteDialog = new EspeceDeleteDialog();
    expect(await especeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Espece?');
    await especeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(especeComponentsPage.title), 5000);

    expect(await especeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
