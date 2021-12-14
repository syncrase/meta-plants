import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SousFormeComponentsPage, SousFormeDeleteDialog, SousFormeUpdatePage } from './sous-forme.page-object';

const expect = chai.expect;

describe('SousForme e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sousFormeComponentsPage: SousFormeComponentsPage;
  let sousFormeUpdatePage: SousFormeUpdatePage;
  let sousFormeDeleteDialog: SousFormeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SousFormes', async () => {
    await navBarPage.goToEntity('sous-forme');
    sousFormeComponentsPage = new SousFormeComponentsPage();
    await browser.wait(ec.visibilityOf(sousFormeComponentsPage.title), 5000);
    expect(await sousFormeComponentsPage.getTitle()).to.eq('Sous Formes');
    await browser.wait(ec.or(ec.visibilityOf(sousFormeComponentsPage.entities), ec.visibilityOf(sousFormeComponentsPage.noResult)), 1000);
  });

  it('should load create SousForme page', async () => {
    await sousFormeComponentsPage.clickOnCreateButton();
    sousFormeUpdatePage = new SousFormeUpdatePage();
    expect(await sousFormeUpdatePage.getPageTitle()).to.eq('Create or edit a Sous Forme');
    await sousFormeUpdatePage.cancel();
  });

  it('should create and save SousFormes', async () => {
    const nbButtonsBeforeCreate = await sousFormeComponentsPage.countDeleteButtons();

    await sousFormeComponentsPage.clickOnCreateButton();

    await promise.all([
      sousFormeUpdatePage.setNomFrInput('nomFr'),
      sousFormeUpdatePage.setNomLatinInput('nomLatin'),
      sousFormeUpdatePage.formeSelectLastOption(),
      sousFormeUpdatePage.sousFormeSelectLastOption(),
    ]);

    await sousFormeUpdatePage.save();
    expect(await sousFormeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sousFormeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SousForme', async () => {
    const nbButtonsBeforeDelete = await sousFormeComponentsPage.countDeleteButtons();
    await sousFormeComponentsPage.clickOnLastDeleteButton();

    sousFormeDeleteDialog = new SousFormeDeleteDialog();
    expect(await sousFormeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Sous Forme?');
    await sousFormeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(sousFormeComponentsPage.title), 5000);

    expect(await sousFormeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
