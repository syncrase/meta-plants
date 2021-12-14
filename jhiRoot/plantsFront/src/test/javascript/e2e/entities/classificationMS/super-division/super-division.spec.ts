import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SuperDivisionComponentsPage, SuperDivisionDeleteDialog, SuperDivisionUpdatePage } from './super-division.page-object';

const expect = chai.expect;

describe('SuperDivision e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let superDivisionComponentsPage: SuperDivisionComponentsPage;
  let superDivisionUpdatePage: SuperDivisionUpdatePage;
  let superDivisionDeleteDialog: SuperDivisionDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SuperDivisions', async () => {
    await navBarPage.goToEntity('super-division');
    superDivisionComponentsPage = new SuperDivisionComponentsPage();
    await browser.wait(ec.visibilityOf(superDivisionComponentsPage.title), 5000);
    expect(await superDivisionComponentsPage.getTitle()).to.eq('Super Divisions');
    await browser.wait(
      ec.or(ec.visibilityOf(superDivisionComponentsPage.entities), ec.visibilityOf(superDivisionComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SuperDivision page', async () => {
    await superDivisionComponentsPage.clickOnCreateButton();
    superDivisionUpdatePage = new SuperDivisionUpdatePage();
    expect(await superDivisionUpdatePage.getPageTitle()).to.eq('Create or edit a Super Division');
    await superDivisionUpdatePage.cancel();
  });

  it('should create and save SuperDivisions', async () => {
    const nbButtonsBeforeCreate = await superDivisionComponentsPage.countDeleteButtons();

    await superDivisionComponentsPage.clickOnCreateButton();

    await promise.all([
      superDivisionUpdatePage.setNomFrInput('nomFr'),
      superDivisionUpdatePage.setNomLatinInput('nomLatin'),
      superDivisionUpdatePage.infraRegneSelectLastOption(),
      superDivisionUpdatePage.superDivisionSelectLastOption(),
    ]);

    await superDivisionUpdatePage.save();
    expect(await superDivisionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await superDivisionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SuperDivision', async () => {
    const nbButtonsBeforeDelete = await superDivisionComponentsPage.countDeleteButtons();
    await superDivisionComponentsPage.clickOnLastDeleteButton();

    superDivisionDeleteDialog = new SuperDivisionDeleteDialog();
    expect(await superDivisionDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Super Division?');
    await superDivisionDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(superDivisionComponentsPage.title), 5000);

    expect(await superDivisionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
