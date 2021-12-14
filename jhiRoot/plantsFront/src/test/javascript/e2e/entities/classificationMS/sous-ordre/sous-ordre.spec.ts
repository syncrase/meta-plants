import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SousOrdreComponentsPage, SousOrdreDeleteDialog, SousOrdreUpdatePage } from './sous-ordre.page-object';

const expect = chai.expect;

describe('SousOrdre e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sousOrdreComponentsPage: SousOrdreComponentsPage;
  let sousOrdreUpdatePage: SousOrdreUpdatePage;
  let sousOrdreDeleteDialog: SousOrdreDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SousOrdres', async () => {
    await navBarPage.goToEntity('sous-ordre');
    sousOrdreComponentsPage = new SousOrdreComponentsPage();
    await browser.wait(ec.visibilityOf(sousOrdreComponentsPage.title), 5000);
    expect(await sousOrdreComponentsPage.getTitle()).to.eq('Sous Ordres');
    await browser.wait(ec.or(ec.visibilityOf(sousOrdreComponentsPage.entities), ec.visibilityOf(sousOrdreComponentsPage.noResult)), 1000);
  });

  it('should load create SousOrdre page', async () => {
    await sousOrdreComponentsPage.clickOnCreateButton();
    sousOrdreUpdatePage = new SousOrdreUpdatePage();
    expect(await sousOrdreUpdatePage.getPageTitle()).to.eq('Create or edit a Sous Ordre');
    await sousOrdreUpdatePage.cancel();
  });

  it('should create and save SousOrdres', async () => {
    const nbButtonsBeforeCreate = await sousOrdreComponentsPage.countDeleteButtons();

    await sousOrdreComponentsPage.clickOnCreateButton();

    await promise.all([
      sousOrdreUpdatePage.setNomFrInput('nomFr'),
      sousOrdreUpdatePage.setNomLatinInput('nomLatin'),
      sousOrdreUpdatePage.ordreSelectLastOption(),
      sousOrdreUpdatePage.sousOrdreSelectLastOption(),
    ]);

    await sousOrdreUpdatePage.save();
    expect(await sousOrdreUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sousOrdreComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SousOrdre', async () => {
    const nbButtonsBeforeDelete = await sousOrdreComponentsPage.countDeleteButtons();
    await sousOrdreComponentsPage.clickOnLastDeleteButton();

    sousOrdreDeleteDialog = new SousOrdreDeleteDialog();
    expect(await sousOrdreDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Sous Ordre?');
    await sousOrdreDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(sousOrdreComponentsPage.title), 5000);

    expect(await sousOrdreComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
