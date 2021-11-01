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

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Raunkiers', async () => {
    await navBarPage.goToEntity('raunkier');
    raunkierComponentsPage = new RaunkierComponentsPage();
    await browser.wait(ec.visibilityOf(raunkierComponentsPage.title), 5000);
    expect(await raunkierComponentsPage.getTitle()).to.eq('gatewayApp.microserviceRaunkier.home.title');
    await browser.wait(ec.or(ec.visibilityOf(raunkierComponentsPage.entities), ec.visibilityOf(raunkierComponentsPage.noResult)), 1000);
  });

  it('should load create Raunkier page', async () => {
    await raunkierComponentsPage.clickOnCreateButton();
    raunkierUpdatePage = new RaunkierUpdatePage();
    expect(await raunkierUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceRaunkier.home.createOrEditLabel');
    await raunkierUpdatePage.cancel();
  });

  it('should create and save Raunkiers', async () => {
    const nbButtonsBeforeCreate = await raunkierComponentsPage.countDeleteButtons();

    await raunkierComponentsPage.clickOnCreateButton();

    await promise.all([raunkierUpdatePage.setTypeInput('type')]);

    expect(await raunkierUpdatePage.getTypeInput()).to.eq('type', 'Expected Type value to be equals to type');

    await raunkierUpdatePage.save();
    expect(await raunkierUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await raunkierComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Raunkier', async () => {
    const nbButtonsBeforeDelete = await raunkierComponentsPage.countDeleteButtons();
    await raunkierComponentsPage.clickOnLastDeleteButton();

    raunkierDeleteDialog = new RaunkierDeleteDialog();
    expect(await raunkierDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceRaunkier.delete.question');
    await raunkierDeleteDialog.clickOnConfirmButton();

    expect(await raunkierComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
