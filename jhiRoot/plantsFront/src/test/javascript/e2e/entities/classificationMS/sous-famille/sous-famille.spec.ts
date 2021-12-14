import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SousFamilleComponentsPage, SousFamilleDeleteDialog, SousFamilleUpdatePage } from './sous-famille.page-object';

const expect = chai.expect;

describe('SousFamille e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sousFamilleComponentsPage: SousFamilleComponentsPage;
  let sousFamilleUpdatePage: SousFamilleUpdatePage;
  let sousFamilleDeleteDialog: SousFamilleDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SousFamilles', async () => {
    await navBarPage.goToEntity('sous-famille');
    sousFamilleComponentsPage = new SousFamilleComponentsPage();
    await browser.wait(ec.visibilityOf(sousFamilleComponentsPage.title), 5000);
    expect(await sousFamilleComponentsPage.getTitle()).to.eq('Sous Familles');
    await browser.wait(
      ec.or(ec.visibilityOf(sousFamilleComponentsPage.entities), ec.visibilityOf(sousFamilleComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SousFamille page', async () => {
    await sousFamilleComponentsPage.clickOnCreateButton();
    sousFamilleUpdatePage = new SousFamilleUpdatePage();
    expect(await sousFamilleUpdatePage.getPageTitle()).to.eq('Create or edit a Sous Famille');
    await sousFamilleUpdatePage.cancel();
  });

  it('should create and save SousFamilles', async () => {
    const nbButtonsBeforeCreate = await sousFamilleComponentsPage.countDeleteButtons();

    await sousFamilleComponentsPage.clickOnCreateButton();

    await promise.all([
      sousFamilleUpdatePage.setNomFrInput('nomFr'),
      sousFamilleUpdatePage.setNomLatinInput('nomLatin'),
      sousFamilleUpdatePage.familleSelectLastOption(),
      sousFamilleUpdatePage.sousFamilleSelectLastOption(),
    ]);

    await sousFamilleUpdatePage.save();
    expect(await sousFamilleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sousFamilleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SousFamille', async () => {
    const nbButtonsBeforeDelete = await sousFamilleComponentsPage.countDeleteButtons();
    await sousFamilleComponentsPage.clickOnLastDeleteButton();

    sousFamilleDeleteDialog = new SousFamilleDeleteDialog();
    expect(await sousFamilleDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Sous Famille?');
    await sousFamilleDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(sousFamilleComponentsPage.title), 5000);

    expect(await sousFamilleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
