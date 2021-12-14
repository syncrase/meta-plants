import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { FamilleComponentsPage, FamilleDeleteDialog, FamilleUpdatePage } from './famille.page-object';

const expect = chai.expect;

describe('Famille e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let familleComponentsPage: FamilleComponentsPage;
  let familleUpdatePage: FamilleUpdatePage;
  let familleDeleteDialog: FamilleDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Familles', async () => {
    await navBarPage.goToEntity('famille');
    familleComponentsPage = new FamilleComponentsPage();
    await browser.wait(ec.visibilityOf(familleComponentsPage.title), 5000);
    expect(await familleComponentsPage.getTitle()).to.eq('Familles');
    await browser.wait(ec.or(ec.visibilityOf(familleComponentsPage.entities), ec.visibilityOf(familleComponentsPage.noResult)), 1000);
  });

  it('should load create Famille page', async () => {
    await familleComponentsPage.clickOnCreateButton();
    familleUpdatePage = new FamilleUpdatePage();
    expect(await familleUpdatePage.getPageTitle()).to.eq('Create or edit a Famille');
    await familleUpdatePage.cancel();
  });

  it('should create and save Familles', async () => {
    const nbButtonsBeforeCreate = await familleComponentsPage.countDeleteButtons();

    await familleComponentsPage.clickOnCreateButton();

    await promise.all([
      familleUpdatePage.setNomFrInput('nomFr'),
      familleUpdatePage.setNomLatinInput('nomLatin'),
      familleUpdatePage.superFamilleSelectLastOption(),
      familleUpdatePage.familleSelectLastOption(),
    ]);

    await familleUpdatePage.save();
    expect(await familleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await familleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Famille', async () => {
    const nbButtonsBeforeDelete = await familleComponentsPage.countDeleteButtons();
    await familleComponentsPage.clickOnLastDeleteButton();

    familleDeleteDialog = new FamilleDeleteDialog();
    expect(await familleDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Famille?');
    await familleDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(familleComponentsPage.title), 5000);

    expect(await familleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
