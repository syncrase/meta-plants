import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SuperOrdreComponentsPage, SuperOrdreDeleteDialog, SuperOrdreUpdatePage } from './super-ordre.page-object';

const expect = chai.expect;

describe('SuperOrdre e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let superOrdreComponentsPage: SuperOrdreComponentsPage;
  let superOrdreUpdatePage: SuperOrdreUpdatePage;
  let superOrdreDeleteDialog: SuperOrdreDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SuperOrdres', async () => {
    await navBarPage.goToEntity('super-ordre');
    superOrdreComponentsPage = new SuperOrdreComponentsPage();
    await browser.wait(ec.visibilityOf(superOrdreComponentsPage.title), 5000);
    expect(await superOrdreComponentsPage.getTitle()).to.eq('Super Ordres');
    await browser.wait(ec.or(ec.visibilityOf(superOrdreComponentsPage.entities), ec.visibilityOf(superOrdreComponentsPage.noResult)), 1000);
  });

  it('should load create SuperOrdre page', async () => {
    await superOrdreComponentsPage.clickOnCreateButton();
    superOrdreUpdatePage = new SuperOrdreUpdatePage();
    expect(await superOrdreUpdatePage.getPageTitle()).to.eq('Create or edit a Super Ordre');
    await superOrdreUpdatePage.cancel();
  });

  it('should create and save SuperOrdres', async () => {
    const nbButtonsBeforeCreate = await superOrdreComponentsPage.countDeleteButtons();

    await superOrdreComponentsPage.clickOnCreateButton();

    await promise.all([
      superOrdreUpdatePage.setNomFrInput('nomFr'),
      superOrdreUpdatePage.setNomLatinInput('nomLatin'),
      superOrdreUpdatePage.infraClasseSelectLastOption(),
      superOrdreUpdatePage.superOrdreSelectLastOption(),
    ]);

    await superOrdreUpdatePage.save();
    expect(await superOrdreUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await superOrdreComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SuperOrdre', async () => {
    const nbButtonsBeforeDelete = await superOrdreComponentsPage.countDeleteButtons();
    await superOrdreComponentsPage.clickOnLastDeleteButton();

    superOrdreDeleteDialog = new SuperOrdreDeleteDialog();
    expect(await superOrdreDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Super Ordre?');
    await superOrdreDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(superOrdreComponentsPage.title), 5000);

    expect(await superOrdreComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
