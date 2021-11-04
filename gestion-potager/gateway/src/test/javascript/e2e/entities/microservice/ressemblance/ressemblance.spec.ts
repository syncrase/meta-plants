import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { RessemblanceComponentsPage, RessemblanceDeleteDialog, RessemblanceUpdatePage } from './ressemblance.page-object';

const expect = chai.expect;

describe('Ressemblance e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ressemblanceComponentsPage: RessemblanceComponentsPage;
  let ressemblanceUpdatePage: RessemblanceUpdatePage;
  let ressemblanceDeleteDialog: RessemblanceDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Ressemblances', async () => {
    await navBarPage.goToEntity('ressemblance');
    ressemblanceComponentsPage = new RessemblanceComponentsPage();
    await browser.wait(ec.visibilityOf(ressemblanceComponentsPage.title), 5000);
    expect(await ressemblanceComponentsPage.getTitle()).to.eq('gatewayApp.microserviceRessemblance.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(ressemblanceComponentsPage.entities), ec.visibilityOf(ressemblanceComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Ressemblance page', async () => {
    await ressemblanceComponentsPage.clickOnCreateButton();
    ressemblanceUpdatePage = new RessemblanceUpdatePage();
    expect(await ressemblanceUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceRessemblance.home.createOrEditLabel');
    await ressemblanceUpdatePage.cancel();
  });

  it('should create and save Ressemblances', async () => {
    const nbButtonsBeforeCreate = await ressemblanceComponentsPage.countDeleteButtons();

    await ressemblanceComponentsPage.clickOnCreateButton();

    await promise.all([ressemblanceUpdatePage.setDescriptionInput('description'), ressemblanceUpdatePage.confusionSelectLastOption()]);

    expect(await ressemblanceUpdatePage.getDescriptionInput()).to.eq(
      'description',
      'Expected Description value to be equals to description'
    );

    await ressemblanceUpdatePage.save();
    expect(await ressemblanceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ressemblanceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Ressemblance', async () => {
    const nbButtonsBeforeDelete = await ressemblanceComponentsPage.countDeleteButtons();
    await ressemblanceComponentsPage.clickOnLastDeleteButton();

    ressemblanceDeleteDialog = new RessemblanceDeleteDialog();
    expect(await ressemblanceDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceRessemblance.delete.question');
    await ressemblanceDeleteDialog.clickOnConfirmButton();

    expect(await ressemblanceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});