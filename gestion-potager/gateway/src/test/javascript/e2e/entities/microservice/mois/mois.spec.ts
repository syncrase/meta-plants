import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { MoisComponentsPage, MoisDeleteDialog, MoisUpdatePage } from './mois.page-object';

const expect = chai.expect;

describe('Mois e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let moisComponentsPage: MoisComponentsPage;
  let moisUpdatePage: MoisUpdatePage;
  let moisDeleteDialog: MoisDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Mois', async () => {
    await navBarPage.goToEntity('mois');
    moisComponentsPage = new MoisComponentsPage();
    await browser.wait(ec.visibilityOf(moisComponentsPage.title), 5000);
    expect(await moisComponentsPage.getTitle()).to.eq('gatewayApp.microserviceMois.home.title');
    await browser.wait(ec.or(ec.visibilityOf(moisComponentsPage.entities), ec.visibilityOf(moisComponentsPage.noResult)), 1000);
  });

  it('should load create Mois page', async () => {
    await moisComponentsPage.clickOnCreateButton();
    moisUpdatePage = new MoisUpdatePage();
    expect(await moisUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceMois.home.createOrEditLabel');
    await moisUpdatePage.cancel();
  });

  it('should create and save Mois', async () => {
    const nbButtonsBeforeCreate = await moisComponentsPage.countDeleteButtons();

    await moisComponentsPage.clickOnCreateButton();

    await promise.all([moisUpdatePage.setNumeroInput('5'), moisUpdatePage.setNomInput('nom')]);

    expect(await moisUpdatePage.getNumeroInput()).to.eq('5', 'Expected numero value to be equals to 5');
    expect(await moisUpdatePage.getNomInput()).to.eq('nom', 'Expected Nom value to be equals to nom');

    await moisUpdatePage.save();
    expect(await moisUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await moisComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Mois', async () => {
    const nbButtonsBeforeDelete = await moisComponentsPage.countDeleteButtons();
    await moisComponentsPage.clickOnLastDeleteButton();

    moisDeleteDialog = new MoisDeleteDialog();
    expect(await moisDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceMois.delete.question');
    await moisDeleteDialog.clickOnConfirmButton();

    expect(await moisComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
