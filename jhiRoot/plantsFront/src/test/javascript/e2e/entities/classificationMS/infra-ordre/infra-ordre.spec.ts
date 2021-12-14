import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { InfraOrdreComponentsPage, InfraOrdreDeleteDialog, InfraOrdreUpdatePage } from './infra-ordre.page-object';

const expect = chai.expect;

describe('InfraOrdre e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let infraOrdreComponentsPage: InfraOrdreComponentsPage;
  let infraOrdreUpdatePage: InfraOrdreUpdatePage;
  let infraOrdreDeleteDialog: InfraOrdreDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load InfraOrdres', async () => {
    await navBarPage.goToEntity('infra-ordre');
    infraOrdreComponentsPage = new InfraOrdreComponentsPage();
    await browser.wait(ec.visibilityOf(infraOrdreComponentsPage.title), 5000);
    expect(await infraOrdreComponentsPage.getTitle()).to.eq('Infra Ordres');
    await browser.wait(ec.or(ec.visibilityOf(infraOrdreComponentsPage.entities), ec.visibilityOf(infraOrdreComponentsPage.noResult)), 1000);
  });

  it('should load create InfraOrdre page', async () => {
    await infraOrdreComponentsPage.clickOnCreateButton();
    infraOrdreUpdatePage = new InfraOrdreUpdatePage();
    expect(await infraOrdreUpdatePage.getPageTitle()).to.eq('Create or edit a Infra Ordre');
    await infraOrdreUpdatePage.cancel();
  });

  it('should create and save InfraOrdres', async () => {
    const nbButtonsBeforeCreate = await infraOrdreComponentsPage.countDeleteButtons();

    await infraOrdreComponentsPage.clickOnCreateButton();

    await promise.all([
      infraOrdreUpdatePage.setNomFrInput('nomFr'),
      infraOrdreUpdatePage.setNomLatinInput('nomLatin'),
      infraOrdreUpdatePage.sousOrdreSelectLastOption(),
      infraOrdreUpdatePage.infraOrdreSelectLastOption(),
    ]);

    await infraOrdreUpdatePage.save();
    expect(await infraOrdreUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await infraOrdreComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last InfraOrdre', async () => {
    const nbButtonsBeforeDelete = await infraOrdreComponentsPage.countDeleteButtons();
    await infraOrdreComponentsPage.clickOnLastDeleteButton();

    infraOrdreDeleteDialog = new InfraOrdreDeleteDialog();
    expect(await infraOrdreDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Infra Ordre?');
    await infraOrdreDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(infraOrdreComponentsPage.title), 5000);

    expect(await infraOrdreComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
