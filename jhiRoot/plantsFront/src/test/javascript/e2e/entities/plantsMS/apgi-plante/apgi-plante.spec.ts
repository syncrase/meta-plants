import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { APGIPlanteComponentsPage, APGIPlanteDeleteDialog, APGIPlanteUpdatePage } from './apgi-plante.page-object';

const expect = chai.expect;

describe('APGIPlante e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let aPGIPlanteComponentsPage: APGIPlanteComponentsPage;
  let aPGIPlanteUpdatePage: APGIPlanteUpdatePage;
  let aPGIPlanteDeleteDialog: APGIPlanteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load APGIPlantes', async () => {
    await navBarPage.goToEntity('apgi-plante');
    aPGIPlanteComponentsPage = new APGIPlanteComponentsPage();
    await browser.wait(ec.visibilityOf(aPGIPlanteComponentsPage.title), 5000);
    expect(await aPGIPlanteComponentsPage.getTitle()).to.eq('APGI Plantes');
    await browser.wait(ec.or(ec.visibilityOf(aPGIPlanteComponentsPage.entities), ec.visibilityOf(aPGIPlanteComponentsPage.noResult)), 1000);
  });

  it('should load create APGIPlante page', async () => {
    await aPGIPlanteComponentsPage.clickOnCreateButton();
    aPGIPlanteUpdatePage = new APGIPlanteUpdatePage();
    expect(await aPGIPlanteUpdatePage.getPageTitle()).to.eq('Create or edit a APGI Plante');
    await aPGIPlanteUpdatePage.cancel();
  });

  it('should create and save APGIPlantes', async () => {
    const nbButtonsBeforeCreate = await aPGIPlanteComponentsPage.countDeleteButtons();

    await aPGIPlanteComponentsPage.clickOnCreateButton();

    await promise.all([aPGIPlanteUpdatePage.setOrdreInput('ordre'), aPGIPlanteUpdatePage.setFamilleInput('famille')]);

    await aPGIPlanteUpdatePage.save();
    expect(await aPGIPlanteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await aPGIPlanteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last APGIPlante', async () => {
    const nbButtonsBeforeDelete = await aPGIPlanteComponentsPage.countDeleteButtons();
    await aPGIPlanteComponentsPage.clickOnLastDeleteButton();

    aPGIPlanteDeleteDialog = new APGIPlanteDeleteDialog();
    expect(await aPGIPlanteDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this APGI Plante?');
    await aPGIPlanteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(aPGIPlanteComponentsPage.title), 5000);

    expect(await aPGIPlanteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
