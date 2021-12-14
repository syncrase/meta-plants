import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SousRegneComponentsPage, SousRegneDeleteDialog, SousRegneUpdatePage } from './sous-regne.page-object';

const expect = chai.expect;

describe('SousRegne e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sousRegneComponentsPage: SousRegneComponentsPage;
  let sousRegneUpdatePage: SousRegneUpdatePage;
  let sousRegneDeleteDialog: SousRegneDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SousRegnes', async () => {
    await navBarPage.goToEntity('sous-regne');
    sousRegneComponentsPage = new SousRegneComponentsPage();
    await browser.wait(ec.visibilityOf(sousRegneComponentsPage.title), 5000);
    expect(await sousRegneComponentsPage.getTitle()).to.eq('Sous Regnes');
    await browser.wait(ec.or(ec.visibilityOf(sousRegneComponentsPage.entities), ec.visibilityOf(sousRegneComponentsPage.noResult)), 1000);
  });

  it('should load create SousRegne page', async () => {
    await sousRegneComponentsPage.clickOnCreateButton();
    sousRegneUpdatePage = new SousRegneUpdatePage();
    expect(await sousRegneUpdatePage.getPageTitle()).to.eq('Create or edit a Sous Regne');
    await sousRegneUpdatePage.cancel();
  });

  it('should create and save SousRegnes', async () => {
    const nbButtonsBeforeCreate = await sousRegneComponentsPage.countDeleteButtons();

    await sousRegneComponentsPage.clickOnCreateButton();

    await promise.all([
      sousRegneUpdatePage.setNomFrInput('nomFr'),
      sousRegneUpdatePage.setNomLatinInput('nomLatin'),
      sousRegneUpdatePage.regneSelectLastOption(),
      sousRegneUpdatePage.sousRegneSelectLastOption(),
    ]);

    await sousRegneUpdatePage.save();
    expect(await sousRegneUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sousRegneComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SousRegne', async () => {
    const nbButtonsBeforeDelete = await sousRegneComponentsPage.countDeleteButtons();
    await sousRegneComponentsPage.clickOnLastDeleteButton();

    sousRegneDeleteDialog = new SousRegneDeleteDialog();
    expect(await sousRegneDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Sous Regne?');
    await sousRegneDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(sousRegneComponentsPage.title), 5000);

    expect(await sousRegneComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
