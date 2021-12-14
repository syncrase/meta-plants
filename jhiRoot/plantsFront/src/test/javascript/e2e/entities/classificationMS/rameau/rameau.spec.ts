import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { RameauComponentsPage, RameauDeleteDialog, RameauUpdatePage } from './rameau.page-object';

const expect = chai.expect;

describe('Rameau e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rameauComponentsPage: RameauComponentsPage;
  let rameauUpdatePage: RameauUpdatePage;
  let rameauDeleteDialog: RameauDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Rameaus', async () => {
    await navBarPage.goToEntity('rameau');
    rameauComponentsPage = new RameauComponentsPage();
    await browser.wait(ec.visibilityOf(rameauComponentsPage.title), 5000);
    expect(await rameauComponentsPage.getTitle()).to.eq('Rameaus');
    await browser.wait(ec.or(ec.visibilityOf(rameauComponentsPage.entities), ec.visibilityOf(rameauComponentsPage.noResult)), 1000);
  });

  it('should load create Rameau page', async () => {
    await rameauComponentsPage.clickOnCreateButton();
    rameauUpdatePage = new RameauUpdatePage();
    expect(await rameauUpdatePage.getPageTitle()).to.eq('Create or edit a Rameau');
    await rameauUpdatePage.cancel();
  });

  it('should create and save Rameaus', async () => {
    const nbButtonsBeforeCreate = await rameauComponentsPage.countDeleteButtons();

    await rameauComponentsPage.clickOnCreateButton();

    await promise.all([
      rameauUpdatePage.setNomFrInput('nomFr'),
      rameauUpdatePage.setNomLatinInput('nomLatin'),
      rameauUpdatePage.sousRegneSelectLastOption(),
      rameauUpdatePage.rameauSelectLastOption(),
    ]);

    await rameauUpdatePage.save();
    expect(await rameauUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await rameauComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Rameau', async () => {
    const nbButtonsBeforeDelete = await rameauComponentsPage.countDeleteButtons();
    await rameauComponentsPage.clickOnLastDeleteButton();

    rameauDeleteDialog = new RameauDeleteDialog();
    expect(await rameauDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Rameau?');
    await rameauDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(rameauComponentsPage.title), 5000);

    expect(await rameauComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
