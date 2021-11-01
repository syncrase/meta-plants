import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SolComponentsPage, SolDeleteDialog, SolUpdatePage } from './sol.page-object';

const expect = chai.expect;

describe('Sol e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let solComponentsPage: SolComponentsPage;
  let solUpdatePage: SolUpdatePage;
  let solDeleteDialog: SolDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Sols', async () => {
    await navBarPage.goToEntity('sol');
    solComponentsPage = new SolComponentsPage();
    await browser.wait(ec.visibilityOf(solComponentsPage.title), 5000);
    expect(await solComponentsPage.getTitle()).to.eq('gatewayApp.microserviceSol.home.title');
    await browser.wait(ec.or(ec.visibilityOf(solComponentsPage.entities), ec.visibilityOf(solComponentsPage.noResult)), 1000);
  });

  it('should load create Sol page', async () => {
    await solComponentsPage.clickOnCreateButton();
    solUpdatePage = new SolUpdatePage();
    expect(await solUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceSol.home.createOrEditLabel');
    await solUpdatePage.cancel();
  });

  it('should create and save Sols', async () => {
    const nbButtonsBeforeCreate = await solComponentsPage.countDeleteButtons();

    await solComponentsPage.clickOnCreateButton();

    await promise.all([solUpdatePage.setAciditeInput('5'), solUpdatePage.setTypeInput('type')]);

    expect(await solUpdatePage.getAciditeInput()).to.eq('5', 'Expected acidite value to be equals to 5');
    expect(await solUpdatePage.getTypeInput()).to.eq('type', 'Expected Type value to be equals to type');

    await solUpdatePage.save();
    expect(await solUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await solComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Sol', async () => {
    const nbButtonsBeforeDelete = await solComponentsPage.countDeleteButtons();
    await solComponentsPage.clickOnLastDeleteButton();

    solDeleteDialog = new SolDeleteDialog();
    expect(await solDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceSol.delete.question');
    await solDeleteDialog.clickOnConfirmButton();

    expect(await solComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
