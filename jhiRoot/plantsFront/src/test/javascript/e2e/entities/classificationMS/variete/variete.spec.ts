import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { VarieteComponentsPage, VarieteDeleteDialog, VarieteUpdatePage } from './variete.page-object';

const expect = chai.expect;

describe('Variete e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let varieteComponentsPage: VarieteComponentsPage;
  let varieteUpdatePage: VarieteUpdatePage;
  let varieteDeleteDialog: VarieteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Varietes', async () => {
    await navBarPage.goToEntity('variete');
    varieteComponentsPage = new VarieteComponentsPage();
    await browser.wait(ec.visibilityOf(varieteComponentsPage.title), 5000);
    expect(await varieteComponentsPage.getTitle()).to.eq('Varietes');
    await browser.wait(ec.or(ec.visibilityOf(varieteComponentsPage.entities), ec.visibilityOf(varieteComponentsPage.noResult)), 1000);
  });

  it('should load create Variete page', async () => {
    await varieteComponentsPage.clickOnCreateButton();
    varieteUpdatePage = new VarieteUpdatePage();
    expect(await varieteUpdatePage.getPageTitle()).to.eq('Create or edit a Variete');
    await varieteUpdatePage.cancel();
  });

  it('should create and save Varietes', async () => {
    const nbButtonsBeforeCreate = await varieteComponentsPage.countDeleteButtons();

    await varieteComponentsPage.clickOnCreateButton();

    await promise.all([
      varieteUpdatePage.setNomFrInput('nomFr'),
      varieteUpdatePage.setNomLatinInput('nomLatin'),
      varieteUpdatePage.sousEspeceSelectLastOption(),
      varieteUpdatePage.varieteSelectLastOption(),
    ]);

    await varieteUpdatePage.save();
    expect(await varieteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await varieteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Variete', async () => {
    const nbButtonsBeforeDelete = await varieteComponentsPage.countDeleteButtons();
    await varieteComponentsPage.clickOnLastDeleteButton();

    varieteDeleteDialog = new VarieteDeleteDialog();
    expect(await varieteDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Variete?');
    await varieteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(varieteComponentsPage.title), 5000);

    expect(await varieteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
