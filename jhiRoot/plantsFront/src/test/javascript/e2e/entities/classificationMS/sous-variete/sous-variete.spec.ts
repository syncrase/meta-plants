import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SousVarieteComponentsPage, SousVarieteDeleteDialog, SousVarieteUpdatePage } from './sous-variete.page-object';

const expect = chai.expect;

describe('SousVariete e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sousVarieteComponentsPage: SousVarieteComponentsPage;
  let sousVarieteUpdatePage: SousVarieteUpdatePage;
  let sousVarieteDeleteDialog: SousVarieteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SousVarietes', async () => {
    await navBarPage.goToEntity('sous-variete');
    sousVarieteComponentsPage = new SousVarieteComponentsPage();
    await browser.wait(ec.visibilityOf(sousVarieteComponentsPage.title), 5000);
    expect(await sousVarieteComponentsPage.getTitle()).to.eq('Sous Varietes');
    await browser.wait(
      ec.or(ec.visibilityOf(sousVarieteComponentsPage.entities), ec.visibilityOf(sousVarieteComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SousVariete page', async () => {
    await sousVarieteComponentsPage.clickOnCreateButton();
    sousVarieteUpdatePage = new SousVarieteUpdatePage();
    expect(await sousVarieteUpdatePage.getPageTitle()).to.eq('Create or edit a Sous Variete');
    await sousVarieteUpdatePage.cancel();
  });

  it('should create and save SousVarietes', async () => {
    const nbButtonsBeforeCreate = await sousVarieteComponentsPage.countDeleteButtons();

    await sousVarieteComponentsPage.clickOnCreateButton();

    await promise.all([
      sousVarieteUpdatePage.setNomFrInput('nomFr'),
      sousVarieteUpdatePage.setNomLatinInput('nomLatin'),
      sousVarieteUpdatePage.varieteSelectLastOption(),
      sousVarieteUpdatePage.sousVarieteSelectLastOption(),
    ]);

    await sousVarieteUpdatePage.save();
    expect(await sousVarieteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sousVarieteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SousVariete', async () => {
    const nbButtonsBeforeDelete = await sousVarieteComponentsPage.countDeleteButtons();
    await sousVarieteComponentsPage.clickOnLastDeleteButton();

    sousVarieteDeleteDialog = new SousVarieteDeleteDialog();
    expect(await sousVarieteDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Sous Variete?');
    await sousVarieteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(sousVarieteComponentsPage.title), 5000);

    expect(await sousVarieteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
