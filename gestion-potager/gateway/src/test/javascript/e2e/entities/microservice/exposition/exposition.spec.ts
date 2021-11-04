import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { ExpositionComponentsPage, ExpositionDeleteDialog, ExpositionUpdatePage } from './exposition.page-object';

const expect = chai.expect;

describe('Exposition e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let expositionComponentsPage: ExpositionComponentsPage;
  let expositionUpdatePage: ExpositionUpdatePage;
  let expositionDeleteDialog: ExpositionDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Expositions', async () => {
    await navBarPage.goToEntity('exposition');
    expositionComponentsPage = new ExpositionComponentsPage();
    await browser.wait(ec.visibilityOf(expositionComponentsPage.title), 5000);
    expect(await expositionComponentsPage.getTitle()).to.eq('gatewayApp.microserviceExposition.home.title');
    await browser.wait(ec.or(ec.visibilityOf(expositionComponentsPage.entities), ec.visibilityOf(expositionComponentsPage.noResult)), 1000);
  });

  it('should load create Exposition page', async () => {
    await expositionComponentsPage.clickOnCreateButton();
    expositionUpdatePage = new ExpositionUpdatePage();
    expect(await expositionUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceExposition.home.createOrEditLabel');
    await expositionUpdatePage.cancel();
  });

  it('should create and save Expositions', async () => {
    const nbButtonsBeforeCreate = await expositionComponentsPage.countDeleteButtons();

    await expositionComponentsPage.clickOnCreateButton();

    await promise.all([
      expositionUpdatePage.setValeurInput('valeur'),
      expositionUpdatePage.setEnsoleilementInput('5'),
      expositionUpdatePage.planteSelectLastOption(),
    ]);

    await expositionUpdatePage.save();
    expect(await expositionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await expositionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Exposition', async () => {
    const nbButtonsBeforeDelete = await expositionComponentsPage.countDeleteButtons();
    await expositionComponentsPage.clickOnLastDeleteButton();

    expositionDeleteDialog = new ExpositionDeleteDialog();
    expect(await expositionDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceExposition.delete.question');
    await expositionDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(expositionComponentsPage.title), 5000);

    expect(await expositionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
