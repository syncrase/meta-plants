import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { NomVernaculaireComponentsPage, NomVernaculaireDeleteDialog, NomVernaculaireUpdatePage } from './nom-vernaculaire.page-object';

const expect = chai.expect;

describe('NomVernaculaire e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let nomVernaculaireComponentsPage: NomVernaculaireComponentsPage;
  let nomVernaculaireUpdatePage: NomVernaculaireUpdatePage;
  let nomVernaculaireDeleteDialog: NomVernaculaireDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load NomVernaculaires', async () => {
    await navBarPage.goToEntity('nom-vernaculaire');
    nomVernaculaireComponentsPage = new NomVernaculaireComponentsPage();
    await browser.wait(ec.visibilityOf(nomVernaculaireComponentsPage.title), 5000);
    expect(await nomVernaculaireComponentsPage.getTitle()).to.eq('gatewayApp.microserviceNomVernaculaire.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(nomVernaculaireComponentsPage.entities), ec.visibilityOf(nomVernaculaireComponentsPage.noResult)),
      1000
    );
  });

  it('should load create NomVernaculaire page', async () => {
    await nomVernaculaireComponentsPage.clickOnCreateButton();
    nomVernaculaireUpdatePage = new NomVernaculaireUpdatePage();
    expect(await nomVernaculaireUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceNomVernaculaire.home.createOrEditLabel');
    await nomVernaculaireUpdatePage.cancel();
  });

  it('should create and save NomVernaculaires', async () => {
    const nbButtonsBeforeCreate = await nomVernaculaireComponentsPage.countDeleteButtons();

    await nomVernaculaireComponentsPage.clickOnCreateButton();

    await promise.all([nomVernaculaireUpdatePage.setNomInput('nom'), nomVernaculaireUpdatePage.setDescriptionInput('description')]);

    expect(await nomVernaculaireUpdatePage.getNomInput()).to.eq('nom', 'Expected Nom value to be equals to nom');
    expect(await nomVernaculaireUpdatePage.getDescriptionInput()).to.eq(
      'description',
      'Expected Description value to be equals to description'
    );

    await nomVernaculaireUpdatePage.save();
    expect(await nomVernaculaireUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await nomVernaculaireComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last NomVernaculaire', async () => {
    const nbButtonsBeforeDelete = await nomVernaculaireComponentsPage.countDeleteButtons();
    await nomVernaculaireComponentsPage.clickOnLastDeleteButton();

    nomVernaculaireDeleteDialog = new NomVernaculaireDeleteDialog();
    expect(await nomVernaculaireDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceNomVernaculaire.delete.question');
    await nomVernaculaireDeleteDialog.clickOnConfirmButton();

    expect(await nomVernaculaireComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
