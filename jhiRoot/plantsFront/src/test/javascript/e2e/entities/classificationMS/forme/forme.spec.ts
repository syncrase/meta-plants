import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { FormeComponentsPage, FormeDeleteDialog, FormeUpdatePage } from './forme.page-object';

const expect = chai.expect;

describe('Forme e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let formeComponentsPage: FormeComponentsPage;
  let formeUpdatePage: FormeUpdatePage;
  let formeDeleteDialog: FormeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Formes', async () => {
    await navBarPage.goToEntity('forme');
    formeComponentsPage = new FormeComponentsPage();
    await browser.wait(ec.visibilityOf(formeComponentsPage.title), 5000);
    expect(await formeComponentsPage.getTitle()).to.eq('Formes');
    await browser.wait(ec.or(ec.visibilityOf(formeComponentsPage.entities), ec.visibilityOf(formeComponentsPage.noResult)), 1000);
  });

  it('should load create Forme page', async () => {
    await formeComponentsPage.clickOnCreateButton();
    formeUpdatePage = new FormeUpdatePage();
    expect(await formeUpdatePage.getPageTitle()).to.eq('Create or edit a Forme');
    await formeUpdatePage.cancel();
  });

  it('should create and save Formes', async () => {
    const nbButtonsBeforeCreate = await formeComponentsPage.countDeleteButtons();

    await formeComponentsPage.clickOnCreateButton();

    await promise.all([
      formeUpdatePage.setNomFrInput('nomFr'),
      formeUpdatePage.setNomLatinInput('nomLatin'),
      formeUpdatePage.sousVarieteSelectLastOption(),
      formeUpdatePage.formeSelectLastOption(),
    ]);

    await formeUpdatePage.save();
    expect(await formeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await formeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Forme', async () => {
    const nbButtonsBeforeDelete = await formeComponentsPage.countDeleteButtons();
    await formeComponentsPage.clickOnLastDeleteButton();

    formeDeleteDialog = new FormeDeleteDialog();
    expect(await formeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Forme?');
    await formeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(formeComponentsPage.title), 5000);

    expect(await formeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
