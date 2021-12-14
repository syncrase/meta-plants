import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SousTribuComponentsPage, SousTribuDeleteDialog, SousTribuUpdatePage } from './sous-tribu.page-object';

const expect = chai.expect;

describe('SousTribu e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sousTribuComponentsPage: SousTribuComponentsPage;
  let sousTribuUpdatePage: SousTribuUpdatePage;
  let sousTribuDeleteDialog: SousTribuDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SousTribus', async () => {
    await navBarPage.goToEntity('sous-tribu');
    sousTribuComponentsPage = new SousTribuComponentsPage();
    await browser.wait(ec.visibilityOf(sousTribuComponentsPage.title), 5000);
    expect(await sousTribuComponentsPage.getTitle()).to.eq('Sous Tribus');
    await browser.wait(ec.or(ec.visibilityOf(sousTribuComponentsPage.entities), ec.visibilityOf(sousTribuComponentsPage.noResult)), 1000);
  });

  it('should load create SousTribu page', async () => {
    await sousTribuComponentsPage.clickOnCreateButton();
    sousTribuUpdatePage = new SousTribuUpdatePage();
    expect(await sousTribuUpdatePage.getPageTitle()).to.eq('Create or edit a Sous Tribu');
    await sousTribuUpdatePage.cancel();
  });

  it('should create and save SousTribus', async () => {
    const nbButtonsBeforeCreate = await sousTribuComponentsPage.countDeleteButtons();

    await sousTribuComponentsPage.clickOnCreateButton();

    await promise.all([
      sousTribuUpdatePage.setNomFrInput('nomFr'),
      sousTribuUpdatePage.setNomLatinInput('nomLatin'),
      sousTribuUpdatePage.tribuSelectLastOption(),
      sousTribuUpdatePage.sousTribuSelectLastOption(),
    ]);

    await sousTribuUpdatePage.save();
    expect(await sousTribuUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sousTribuComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SousTribu', async () => {
    const nbButtonsBeforeDelete = await sousTribuComponentsPage.countDeleteButtons();
    await sousTribuComponentsPage.clickOnLastDeleteButton();

    sousTribuDeleteDialog = new SousTribuDeleteDialog();
    expect(await sousTribuDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Sous Tribu?');
    await sousTribuDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(sousTribuComponentsPage.title), 5000);

    expect(await sousTribuComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
