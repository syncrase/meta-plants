import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { AllelopathieComponentsPage, AllelopathieDeleteDialog, AllelopathieUpdatePage } from './allelopathie.page-object';

const expect = chai.expect;

describe('Allelopathie e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let allelopathieComponentsPage: AllelopathieComponentsPage;
  let allelopathieUpdatePage: AllelopathieUpdatePage;
  let allelopathieDeleteDialog: AllelopathieDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Allelopathies', async () => {
    await navBarPage.goToEntity('allelopathie');
    allelopathieComponentsPage = new AllelopathieComponentsPage();
    await browser.wait(ec.visibilityOf(allelopathieComponentsPage.title), 5000);
    expect(await allelopathieComponentsPage.getTitle()).to.eq('gatewayApp.microserviceAllelopathie.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(allelopathieComponentsPage.entities), ec.visibilityOf(allelopathieComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Allelopathie page', async () => {
    await allelopathieComponentsPage.clickOnCreateButton();
    allelopathieUpdatePage = new AllelopathieUpdatePage();
    expect(await allelopathieUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceAllelopathie.home.createOrEditLabel');
    await allelopathieUpdatePage.cancel();
  });

  it('should create and save Allelopathies', async () => {
    const nbButtonsBeforeCreate = await allelopathieComponentsPage.countDeleteButtons();

    await allelopathieComponentsPage.clickOnCreateButton();

    await promise.all([
      allelopathieUpdatePage.setTypeInput('type'),
      allelopathieUpdatePage.setDescriptionInput('description'),
      allelopathieUpdatePage.cibleSelectLastOption(),
      allelopathieUpdatePage.origineSelectLastOption(),
      allelopathieUpdatePage.planteSelectLastOption(),
    ]);

    expect(await allelopathieUpdatePage.getTypeInput()).to.eq('type', 'Expected Type value to be equals to type');
    expect(await allelopathieUpdatePage.getDescriptionInput()).to.eq(
      'description',
      'Expected Description value to be equals to description'
    );

    await allelopathieUpdatePage.save();
    expect(await allelopathieUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await allelopathieComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Allelopathie', async () => {
    const nbButtonsBeforeDelete = await allelopathieComponentsPage.countDeleteButtons();
    await allelopathieComponentsPage.clickOnLastDeleteButton();

    allelopathieDeleteDialog = new AllelopathieDeleteDialog();
    expect(await allelopathieDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceAllelopathie.delete.question');
    await allelopathieDeleteDialog.clickOnConfirmButton();

    expect(await allelopathieComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
