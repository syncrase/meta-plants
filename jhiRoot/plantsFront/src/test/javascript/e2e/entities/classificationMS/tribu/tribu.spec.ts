import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { TribuComponentsPage, TribuDeleteDialog, TribuUpdatePage } from './tribu.page-object';

const expect = chai.expect;

describe('Tribu e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let tribuComponentsPage: TribuComponentsPage;
  let tribuUpdatePage: TribuUpdatePage;
  let tribuDeleteDialog: TribuDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Tribus', async () => {
    await navBarPage.goToEntity('tribu');
    tribuComponentsPage = new TribuComponentsPage();
    await browser.wait(ec.visibilityOf(tribuComponentsPage.title), 5000);
    expect(await tribuComponentsPage.getTitle()).to.eq('Tribus');
    await browser.wait(ec.or(ec.visibilityOf(tribuComponentsPage.entities), ec.visibilityOf(tribuComponentsPage.noResult)), 1000);
  });

  it('should load create Tribu page', async () => {
    await tribuComponentsPage.clickOnCreateButton();
    tribuUpdatePage = new TribuUpdatePage();
    expect(await tribuUpdatePage.getPageTitle()).to.eq('Create or edit a Tribu');
    await tribuUpdatePage.cancel();
  });

  it('should create and save Tribus', async () => {
    const nbButtonsBeforeCreate = await tribuComponentsPage.countDeleteButtons();

    await tribuComponentsPage.clickOnCreateButton();

    await promise.all([
      tribuUpdatePage.setNomFrInput('nomFr'),
      tribuUpdatePage.setNomLatinInput('nomLatin'),
      tribuUpdatePage.sousFamilleSelectLastOption(),
      tribuUpdatePage.tribuSelectLastOption(),
    ]);

    await tribuUpdatePage.save();
    expect(await tribuUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await tribuComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Tribu', async () => {
    const nbButtonsBeforeDelete = await tribuComponentsPage.countDeleteButtons();
    await tribuComponentsPage.clickOnLastDeleteButton();

    tribuDeleteDialog = new TribuDeleteDialog();
    expect(await tribuDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Tribu?');
    await tribuDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(tribuComponentsPage.title), 5000);

    expect(await tribuComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
