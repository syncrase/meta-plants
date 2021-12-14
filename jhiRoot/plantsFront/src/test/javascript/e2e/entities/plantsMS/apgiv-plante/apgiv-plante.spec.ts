import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { APGIVPlanteComponentsPage, APGIVPlanteDeleteDialog, APGIVPlanteUpdatePage } from './apgiv-plante.page-object';

const expect = chai.expect;

describe('APGIVPlante e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let aPGIVPlanteComponentsPage: APGIVPlanteComponentsPage;
  let aPGIVPlanteUpdatePage: APGIVPlanteUpdatePage;
  let aPGIVPlanteDeleteDialog: APGIVPlanteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load APGIVPlantes', async () => {
    await navBarPage.goToEntity('apgiv-plante');
    aPGIVPlanteComponentsPage = new APGIVPlanteComponentsPage();
    await browser.wait(ec.visibilityOf(aPGIVPlanteComponentsPage.title), 5000);
    expect(await aPGIVPlanteComponentsPage.getTitle()).to.eq('APGIV Plantes');
    await browser.wait(
      ec.or(ec.visibilityOf(aPGIVPlanteComponentsPage.entities), ec.visibilityOf(aPGIVPlanteComponentsPage.noResult)),
      1000
    );
  });

  it('should load create APGIVPlante page', async () => {
    await aPGIVPlanteComponentsPage.clickOnCreateButton();
    aPGIVPlanteUpdatePage = new APGIVPlanteUpdatePage();
    expect(await aPGIVPlanteUpdatePage.getPageTitle()).to.eq('Create or edit a APGIV Plante');
    await aPGIVPlanteUpdatePage.cancel();
  });

  it('should create and save APGIVPlantes', async () => {
    const nbButtonsBeforeCreate = await aPGIVPlanteComponentsPage.countDeleteButtons();

    await aPGIVPlanteComponentsPage.clickOnCreateButton();

    await promise.all([aPGIVPlanteUpdatePage.setOrdreInput('ordre'), aPGIVPlanteUpdatePage.setFamilleInput('famille')]);

    await aPGIVPlanteUpdatePage.save();
    expect(await aPGIVPlanteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await aPGIVPlanteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last APGIVPlante', async () => {
    const nbButtonsBeforeDelete = await aPGIVPlanteComponentsPage.countDeleteButtons();
    await aPGIVPlanteComponentsPage.clickOnLastDeleteButton();

    aPGIVPlanteDeleteDialog = new APGIVPlanteDeleteDialog();
    expect(await aPGIVPlanteDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this APGIV Plante?');
    await aPGIVPlanteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(aPGIVPlanteComponentsPage.title), 5000);

    expect(await aPGIVPlanteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
