import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SuperFamilleComponentsPage, SuperFamilleDeleteDialog, SuperFamilleUpdatePage } from './super-famille.page-object';

const expect = chai.expect;

describe('SuperFamille e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let superFamilleComponentsPage: SuperFamilleComponentsPage;
  let superFamilleUpdatePage: SuperFamilleUpdatePage;
  let superFamilleDeleteDialog: SuperFamilleDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SuperFamilles', async () => {
    await navBarPage.goToEntity('super-famille');
    superFamilleComponentsPage = new SuperFamilleComponentsPage();
    await browser.wait(ec.visibilityOf(superFamilleComponentsPage.title), 5000);
    expect(await superFamilleComponentsPage.getTitle()).to.eq('Super Familles');
    await browser.wait(
      ec.or(ec.visibilityOf(superFamilleComponentsPage.entities), ec.visibilityOf(superFamilleComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SuperFamille page', async () => {
    await superFamilleComponentsPage.clickOnCreateButton();
    superFamilleUpdatePage = new SuperFamilleUpdatePage();
    expect(await superFamilleUpdatePage.getPageTitle()).to.eq('Create or edit a Super Famille');
    await superFamilleUpdatePage.cancel();
  });

  it('should create and save SuperFamilles', async () => {
    const nbButtonsBeforeCreate = await superFamilleComponentsPage.countDeleteButtons();

    await superFamilleComponentsPage.clickOnCreateButton();

    await promise.all([
      superFamilleUpdatePage.setNomFrInput('nomFr'),
      superFamilleUpdatePage.setNomLatinInput('nomLatin'),
      superFamilleUpdatePage.microOrdreSelectLastOption(),
      superFamilleUpdatePage.superFamilleSelectLastOption(),
    ]);

    await superFamilleUpdatePage.save();
    expect(await superFamilleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await superFamilleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SuperFamille', async () => {
    const nbButtonsBeforeDelete = await superFamilleComponentsPage.countDeleteButtons();
    await superFamilleComponentsPage.clickOnLastDeleteButton();

    superFamilleDeleteDialog = new SuperFamilleDeleteDialog();
    expect(await superFamilleDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Super Famille?');
    await superFamilleDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(superFamilleComponentsPage.title), 5000);

    expect(await superFamilleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
