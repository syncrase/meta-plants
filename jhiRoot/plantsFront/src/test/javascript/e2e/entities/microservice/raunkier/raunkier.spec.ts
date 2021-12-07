import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { RaunkierComponentsPage, RaunkierDeleteDialog, RaunkierUpdatePage } from './raunkier.page-object';

const expect = chai.expect;

describe('Raunkier e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let raunkierComponentsPage: RaunkierComponentsPage;
  let raunkierUpdatePage: RaunkierUpdatePage;
  let raunkierDeleteDialog: RaunkierDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Raunkiers', async () => {
    await navBarPage.goToEntity('raunkier');
    raunkierComponentsPage = new RaunkierComponentsPage();
    await browser.wait(ec.visibilityOf(raunkierComponentsPage.title), 5000);
    expect(await raunkierComponentsPage.getTitle()).to.eq('Raunkiers');
    await browser.wait(ec.or(ec.visibilityOf(raunkierComponentsPage.entities), ec.visibilityOf(raunkierComponentsPage.noResult)), 1000);
  });

  it('should load create Raunkier page', async () => {
    await raunkierComponentsPage.clickOnCreateButton();
    raunkierUpdatePage = new RaunkierUpdatePage();
    expect(await raunkierUpdatePage.getPageTitle()).to.eq('Create or edit a Raunkier');
    await raunkierUpdatePage.cancel();
  });

  it('should create and save Raunkiers', async () => {
    const nbButtonsBeforeCreate = await raunkierComponentsPage.countDeleteButtons();

    await raunkierComponentsPage.clickOnCreateButton();

    await promise.all([raunkierUpdatePage.setTypeInput('type')]);

    await raunkierUpdatePage.save();
    expect(await raunkierUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await raunkierComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Raunkier', async () => {
    const nbButtonsBeforeDelete = await raunkierComponentsPage.countDeleteButtons();
    await raunkierComponentsPage.clickOnLastDeleteButton();

    raunkierDeleteDialog = new RaunkierDeleteDialog();
    expect(await raunkierDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Raunkier?');
    await raunkierDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(raunkierComponentsPage.title), 5000);

    expect(await raunkierComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
