import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { RegneComponentsPage, RegneDeleteDialog, RegneUpdatePage } from './regne.page-object';

const expect = chai.expect;

describe('Regne e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let regneComponentsPage: RegneComponentsPage;
  let regneUpdatePage: RegneUpdatePage;
  let regneDeleteDialog: RegneDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Regnes', async () => {
    await navBarPage.goToEntity('regne');
    regneComponentsPage = new RegneComponentsPage();
    await browser.wait(ec.visibilityOf(regneComponentsPage.title), 5000);
    expect(await regneComponentsPage.getTitle()).to.eq('Regnes');
    await browser.wait(ec.or(ec.visibilityOf(regneComponentsPage.entities), ec.visibilityOf(regneComponentsPage.noResult)), 1000);
  });

  it('should load create Regne page', async () => {
    await regneComponentsPage.clickOnCreateButton();
    regneUpdatePage = new RegneUpdatePage();
    expect(await regneUpdatePage.getPageTitle()).to.eq('Create or edit a Regne');
    await regneUpdatePage.cancel();
  });

  it('should create and save Regnes', async () => {
    const nbButtonsBeforeCreate = await regneComponentsPage.countDeleteButtons();

    await regneComponentsPage.clickOnCreateButton();

    await promise.all([
      regneUpdatePage.setNomFrInput('nomFr'),
      regneUpdatePage.setNomLatinInput('nomLatin'),
      regneUpdatePage.superRegneSelectLastOption(),
      regneUpdatePage.regneSelectLastOption(),
    ]);

    await regneUpdatePage.save();
    expect(await regneUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await regneComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Regne', async () => {
    const nbButtonsBeforeDelete = await regneComponentsPage.countDeleteButtons();
    await regneComponentsPage.clickOnLastDeleteButton();

    regneDeleteDialog = new RegneDeleteDialog();
    expect(await regneDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Regne?');
    await regneDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(regneComponentsPage.title), 5000);

    expect(await regneComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
