import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { StrateComponentsPage, StrateDeleteDialog, StrateUpdatePage } from './strate.page-object';

const expect = chai.expect;

describe('Strate e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let strateComponentsPage: StrateComponentsPage;
  let strateUpdatePage: StrateUpdatePage;
  let strateDeleteDialog: StrateDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Strates', async () => {
    await navBarPage.goToEntity('strate');
    strateComponentsPage = new StrateComponentsPage();
    await browser.wait(ec.visibilityOf(strateComponentsPage.title), 5000);
    expect(await strateComponentsPage.getTitle()).to.eq('gatewayApp.microserviceStrate.home.title');
    await browser.wait(ec.or(ec.visibilityOf(strateComponentsPage.entities), ec.visibilityOf(strateComponentsPage.noResult)), 1000);
  });

  it('should load create Strate page', async () => {
    await strateComponentsPage.clickOnCreateButton();
    strateUpdatePage = new StrateUpdatePage();
    expect(await strateUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceStrate.home.createOrEditLabel');
    await strateUpdatePage.cancel();
  });

  it('should create and save Strates', async () => {
    const nbButtonsBeforeCreate = await strateComponentsPage.countDeleteButtons();

    await strateComponentsPage.clickOnCreateButton();

    await promise.all([strateUpdatePage.setTypeInput('type')]);

    await strateUpdatePage.save();
    expect(await strateUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await strateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Strate', async () => {
    const nbButtonsBeforeDelete = await strateComponentsPage.countDeleteButtons();
    await strateComponentsPage.clickOnLastDeleteButton();

    strateDeleteDialog = new StrateDeleteDialog();
    expect(await strateDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceStrate.delete.question');
    await strateDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(strateComponentsPage.title), 5000);

    expect(await strateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
