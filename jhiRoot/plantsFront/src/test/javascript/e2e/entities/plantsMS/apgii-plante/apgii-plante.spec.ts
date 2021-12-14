import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { APGIIPlanteComponentsPage, APGIIPlanteDeleteDialog, APGIIPlanteUpdatePage } from './apgii-plante.page-object';

const expect = chai.expect;

describe('APGIIPlante e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let aPGIIPlanteComponentsPage: APGIIPlanteComponentsPage;
  let aPGIIPlanteUpdatePage: APGIIPlanteUpdatePage;
  let aPGIIPlanteDeleteDialog: APGIIPlanteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load APGIIPlantes', async () => {
    await navBarPage.goToEntity('apgii-plante');
    aPGIIPlanteComponentsPage = new APGIIPlanteComponentsPage();
    await browser.wait(ec.visibilityOf(aPGIIPlanteComponentsPage.title), 5000);
    expect(await aPGIIPlanteComponentsPage.getTitle()).to.eq('APGII Plantes');
    await browser.wait(
      ec.or(ec.visibilityOf(aPGIIPlanteComponentsPage.entities), ec.visibilityOf(aPGIIPlanteComponentsPage.noResult)),
      1000
    );
  });

  it('should load create APGIIPlante page', async () => {
    await aPGIIPlanteComponentsPage.clickOnCreateButton();
    aPGIIPlanteUpdatePage = new APGIIPlanteUpdatePage();
    expect(await aPGIIPlanteUpdatePage.getPageTitle()).to.eq('Create or edit a APGII Plante');
    await aPGIIPlanteUpdatePage.cancel();
  });

  it('should create and save APGIIPlantes', async () => {
    const nbButtonsBeforeCreate = await aPGIIPlanteComponentsPage.countDeleteButtons();

    await aPGIIPlanteComponentsPage.clickOnCreateButton();

    await promise.all([aPGIIPlanteUpdatePage.setOrdreInput('ordre'), aPGIIPlanteUpdatePage.setFamilleInput('famille')]);

    await aPGIIPlanteUpdatePage.save();
    expect(await aPGIIPlanteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await aPGIIPlanteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last APGIIPlante', async () => {
    const nbButtonsBeforeDelete = await aPGIIPlanteComponentsPage.countDeleteButtons();
    await aPGIIPlanteComponentsPage.clickOnLastDeleteButton();

    aPGIIPlanteDeleteDialog = new APGIIPlanteDeleteDialog();
    expect(await aPGIIPlanteDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this APGII Plante?');
    await aPGIIPlanteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(aPGIIPlanteComponentsPage.title), 5000);

    expect(await aPGIIPlanteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
