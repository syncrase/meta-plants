import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SousDivisionComponentsPage, SousDivisionDeleteDialog, SousDivisionUpdatePage } from './sous-division.page-object';

const expect = chai.expect;

describe('SousDivision e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sousDivisionComponentsPage: SousDivisionComponentsPage;
  let sousDivisionUpdatePage: SousDivisionUpdatePage;
  let sousDivisionDeleteDialog: SousDivisionDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SousDivisions', async () => {
    await navBarPage.goToEntity('sous-division');
    sousDivisionComponentsPage = new SousDivisionComponentsPage();
    await browser.wait(ec.visibilityOf(sousDivisionComponentsPage.title), 5000);
    expect(await sousDivisionComponentsPage.getTitle()).to.eq('Sous Divisions');
    await browser.wait(
      ec.or(ec.visibilityOf(sousDivisionComponentsPage.entities), ec.visibilityOf(sousDivisionComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SousDivision page', async () => {
    await sousDivisionComponentsPage.clickOnCreateButton();
    sousDivisionUpdatePage = new SousDivisionUpdatePage();
    expect(await sousDivisionUpdatePage.getPageTitle()).to.eq('Create or edit a Sous Division');
    await sousDivisionUpdatePage.cancel();
  });

  it('should create and save SousDivisions', async () => {
    const nbButtonsBeforeCreate = await sousDivisionComponentsPage.countDeleteButtons();

    await sousDivisionComponentsPage.clickOnCreateButton();

    await promise.all([
      sousDivisionUpdatePage.setNomFrInput('nomFr'),
      sousDivisionUpdatePage.setNomLatinInput('nomLatin'),
      sousDivisionUpdatePage.divisionSelectLastOption(),
      sousDivisionUpdatePage.sousDivisionSelectLastOption(),
    ]);

    await sousDivisionUpdatePage.save();
    expect(await sousDivisionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sousDivisionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SousDivision', async () => {
    const nbButtonsBeforeDelete = await sousDivisionComponentsPage.countDeleteButtons();
    await sousDivisionComponentsPage.clickOnLastDeleteButton();

    sousDivisionDeleteDialog = new SousDivisionDeleteDialog();
    expect(await sousDivisionDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Sous Division?');
    await sousDivisionDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(sousDivisionComponentsPage.title), 5000);

    expect(await sousDivisionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
