import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { APGIIIPlanteComponentsPage, APGIIIPlanteDeleteDialog, APGIIIPlanteUpdatePage } from './apgiii-plante.page-object';

const expect = chai.expect;

describe('APGIIIPlante e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let aPGIIIPlanteComponentsPage: APGIIIPlanteComponentsPage;
  let aPGIIIPlanteUpdatePage: APGIIIPlanteUpdatePage;
  let aPGIIIPlanteDeleteDialog: APGIIIPlanteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load APGIIIPlantes', async () => {
    await navBarPage.goToEntity('apgiii-plante');
    aPGIIIPlanteComponentsPage = new APGIIIPlanteComponentsPage();
    await browser.wait(ec.visibilityOf(aPGIIIPlanteComponentsPage.title), 5000);
    expect(await aPGIIIPlanteComponentsPage.getTitle()).to.eq('APGIII Plantes');
    await browser.wait(
      ec.or(ec.visibilityOf(aPGIIIPlanteComponentsPage.entities), ec.visibilityOf(aPGIIIPlanteComponentsPage.noResult)),
      1000
    );
  });

  it('should load create APGIIIPlante page', async () => {
    await aPGIIIPlanteComponentsPage.clickOnCreateButton();
    aPGIIIPlanteUpdatePage = new APGIIIPlanteUpdatePage();
    expect(await aPGIIIPlanteUpdatePage.getPageTitle()).to.eq('Create or edit a APGIII Plante');
    await aPGIIIPlanteUpdatePage.cancel();
  });

  it('should create and save APGIIIPlantes', async () => {
    const nbButtonsBeforeCreate = await aPGIIIPlanteComponentsPage.countDeleteButtons();

    await aPGIIIPlanteComponentsPage.clickOnCreateButton();

    await promise.all([
      aPGIIIPlanteUpdatePage.setOrdreInput('ordre'),
      aPGIIIPlanteUpdatePage.setFamilleInput('famille'),
      aPGIIIPlanteUpdatePage.setSousFamilleInput('sousFamille'),
      aPGIIIPlanteUpdatePage.setTribuInput('tribu'),
      aPGIIIPlanteUpdatePage.setSousTribuInput('sousTribu'),
      aPGIIIPlanteUpdatePage.setGenreInput('genre'),
      // aPGIIIPlanteUpdatePage.cladesSelectLastOption(),
    ]);

    await aPGIIIPlanteUpdatePage.save();
    expect(await aPGIIIPlanteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await aPGIIIPlanteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last APGIIIPlante', async () => {
    const nbButtonsBeforeDelete = await aPGIIIPlanteComponentsPage.countDeleteButtons();
    await aPGIIIPlanteComponentsPage.clickOnLastDeleteButton();

    aPGIIIPlanteDeleteDialog = new APGIIIPlanteDeleteDialog();
    expect(await aPGIIIPlanteDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this APGIII Plante?');
    await aPGIIIPlanteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(aPGIIIPlanteComponentsPage.title), 5000);

    expect(await aPGIIIPlanteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
