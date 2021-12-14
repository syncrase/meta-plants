import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { MicroOrdreComponentsPage, MicroOrdreDeleteDialog, MicroOrdreUpdatePage } from './micro-ordre.page-object';

const expect = chai.expect;

describe('MicroOrdre e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let microOrdreComponentsPage: MicroOrdreComponentsPage;
  let microOrdreUpdatePage: MicroOrdreUpdatePage;
  let microOrdreDeleteDialog: MicroOrdreDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load MicroOrdres', async () => {
    await navBarPage.goToEntity('micro-ordre');
    microOrdreComponentsPage = new MicroOrdreComponentsPage();
    await browser.wait(ec.visibilityOf(microOrdreComponentsPage.title), 5000);
    expect(await microOrdreComponentsPage.getTitle()).to.eq('Micro Ordres');
    await browser.wait(ec.or(ec.visibilityOf(microOrdreComponentsPage.entities), ec.visibilityOf(microOrdreComponentsPage.noResult)), 1000);
  });

  it('should load create MicroOrdre page', async () => {
    await microOrdreComponentsPage.clickOnCreateButton();
    microOrdreUpdatePage = new MicroOrdreUpdatePage();
    expect(await microOrdreUpdatePage.getPageTitle()).to.eq('Create or edit a Micro Ordre');
    await microOrdreUpdatePage.cancel();
  });

  it('should create and save MicroOrdres', async () => {
    const nbButtonsBeforeCreate = await microOrdreComponentsPage.countDeleteButtons();

    await microOrdreComponentsPage.clickOnCreateButton();

    await promise.all([
      microOrdreUpdatePage.setNomFrInput('nomFr'),
      microOrdreUpdatePage.setNomLatinInput('nomLatin'),
      microOrdreUpdatePage.infraOrdreSelectLastOption(),
      microOrdreUpdatePage.microOrdreSelectLastOption(),
    ]);

    await microOrdreUpdatePage.save();
    expect(await microOrdreUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await microOrdreComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last MicroOrdre', async () => {
    const nbButtonsBeforeDelete = await microOrdreComponentsPage.countDeleteButtons();
    await microOrdreComponentsPage.clickOnLastDeleteButton();

    microOrdreDeleteDialog = new MicroOrdreDeleteDialog();
    expect(await microOrdreDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Micro Ordre?');
    await microOrdreDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(microOrdreComponentsPage.title), 5000);

    expect(await microOrdreComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
