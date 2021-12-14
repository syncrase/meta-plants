import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SuperRegneComponentsPage, SuperRegneDeleteDialog, SuperRegneUpdatePage } from './super-regne.page-object';

const expect = chai.expect;

describe('SuperRegne e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let superRegneComponentsPage: SuperRegneComponentsPage;
  let superRegneUpdatePage: SuperRegneUpdatePage;
  let superRegneDeleteDialog: SuperRegneDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SuperRegnes', async () => {
    await navBarPage.goToEntity('super-regne');
    superRegneComponentsPage = new SuperRegneComponentsPage();
    await browser.wait(ec.visibilityOf(superRegneComponentsPage.title), 5000);
    expect(await superRegneComponentsPage.getTitle()).to.eq('Super Regnes');
    await browser.wait(ec.or(ec.visibilityOf(superRegneComponentsPage.entities), ec.visibilityOf(superRegneComponentsPage.noResult)), 1000);
  });

  it('should load create SuperRegne page', async () => {
    await superRegneComponentsPage.clickOnCreateButton();
    superRegneUpdatePage = new SuperRegneUpdatePage();
    expect(await superRegneUpdatePage.getPageTitle()).to.eq('Create or edit a Super Regne');
    await superRegneUpdatePage.cancel();
  });

  it('should create and save SuperRegnes', async () => {
    const nbButtonsBeforeCreate = await superRegneComponentsPage.countDeleteButtons();

    await superRegneComponentsPage.clickOnCreateButton();

    await promise.all([
      superRegneUpdatePage.setNomFrInput('nomFr'),
      superRegneUpdatePage.setNomLatinInput('nomLatin'),
      superRegneUpdatePage.superRegneSelectLastOption(),
    ]);

    await superRegneUpdatePage.save();
    expect(await superRegneUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await superRegneComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SuperRegne', async () => {
    const nbButtonsBeforeDelete = await superRegneComponentsPage.countDeleteButtons();
    await superRegneComponentsPage.clickOnLastDeleteButton();

    superRegneDeleteDialog = new SuperRegneDeleteDialog();
    expect(await superRegneDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Super Regne?');
    await superRegneDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(superRegneComponentsPage.title), 5000);

    expect(await superRegneComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
