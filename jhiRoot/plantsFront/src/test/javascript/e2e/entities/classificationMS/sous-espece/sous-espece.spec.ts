import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SousEspeceComponentsPage, SousEspeceDeleteDialog, SousEspeceUpdatePage } from './sous-espece.page-object';

const expect = chai.expect;

describe('SousEspece e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sousEspeceComponentsPage: SousEspeceComponentsPage;
  let sousEspeceUpdatePage: SousEspeceUpdatePage;
  let sousEspeceDeleteDialog: SousEspeceDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SousEspeces', async () => {
    await navBarPage.goToEntity('sous-espece');
    sousEspeceComponentsPage = new SousEspeceComponentsPage();
    await browser.wait(ec.visibilityOf(sousEspeceComponentsPage.title), 5000);
    expect(await sousEspeceComponentsPage.getTitle()).to.eq('Sous Especes');
    await browser.wait(ec.or(ec.visibilityOf(sousEspeceComponentsPage.entities), ec.visibilityOf(sousEspeceComponentsPage.noResult)), 1000);
  });

  it('should load create SousEspece page', async () => {
    await sousEspeceComponentsPage.clickOnCreateButton();
    sousEspeceUpdatePage = new SousEspeceUpdatePage();
    expect(await sousEspeceUpdatePage.getPageTitle()).to.eq('Create or edit a Sous Espece');
    await sousEspeceUpdatePage.cancel();
  });

  it('should create and save SousEspeces', async () => {
    const nbButtonsBeforeCreate = await sousEspeceComponentsPage.countDeleteButtons();

    await sousEspeceComponentsPage.clickOnCreateButton();

    await promise.all([
      sousEspeceUpdatePage.setNomFrInput('nomFr'),
      sousEspeceUpdatePage.setNomLatinInput('nomLatin'),
      sousEspeceUpdatePage.especeSelectLastOption(),
      sousEspeceUpdatePage.sousEspeceSelectLastOption(),
    ]);

    await sousEspeceUpdatePage.save();
    expect(await sousEspeceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sousEspeceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SousEspece', async () => {
    const nbButtonsBeforeDelete = await sousEspeceComponentsPage.countDeleteButtons();
    await sousEspeceComponentsPage.clickOnLastDeleteButton();

    sousEspeceDeleteDialog = new SousEspeceDeleteDialog();
    expect(await sousEspeceDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Sous Espece?');
    await sousEspeceDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(sousEspeceComponentsPage.title), 5000);

    expect(await sousEspeceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
