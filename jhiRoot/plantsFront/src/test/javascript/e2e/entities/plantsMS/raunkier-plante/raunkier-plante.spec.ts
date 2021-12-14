import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { RaunkierPlanteComponentsPage, RaunkierPlanteDeleteDialog, RaunkierPlanteUpdatePage } from './raunkier-plante.page-object';

const expect = chai.expect;

describe('RaunkierPlante e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let raunkierPlanteComponentsPage: RaunkierPlanteComponentsPage;
  let raunkierPlanteUpdatePage: RaunkierPlanteUpdatePage;
  let raunkierPlanteDeleteDialog: RaunkierPlanteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load RaunkierPlantes', async () => {
    await navBarPage.goToEntity('raunkier-plante');
    raunkierPlanteComponentsPage = new RaunkierPlanteComponentsPage();
    await browser.wait(ec.visibilityOf(raunkierPlanteComponentsPage.title), 5000);
    expect(await raunkierPlanteComponentsPage.getTitle()).to.eq('Raunkier Plantes');
    await browser.wait(
      ec.or(ec.visibilityOf(raunkierPlanteComponentsPage.entities), ec.visibilityOf(raunkierPlanteComponentsPage.noResult)),
      1000
    );
  });

  it('should load create RaunkierPlante page', async () => {
    await raunkierPlanteComponentsPage.clickOnCreateButton();
    raunkierPlanteUpdatePage = new RaunkierPlanteUpdatePage();
    expect(await raunkierPlanteUpdatePage.getPageTitle()).to.eq('Create or edit a Raunkier Plante');
    await raunkierPlanteUpdatePage.cancel();
  });

  it('should create and save RaunkierPlantes', async () => {
    const nbButtonsBeforeCreate = await raunkierPlanteComponentsPage.countDeleteButtons();

    await raunkierPlanteComponentsPage.clickOnCreateButton();

    await promise.all([raunkierPlanteUpdatePage.setTypeInput('type')]);

    await raunkierPlanteUpdatePage.save();
    expect(await raunkierPlanteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await raunkierPlanteComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last RaunkierPlante', async () => {
    const nbButtonsBeforeDelete = await raunkierPlanteComponentsPage.countDeleteButtons();
    await raunkierPlanteComponentsPage.clickOnLastDeleteButton();

    raunkierPlanteDeleteDialog = new RaunkierPlanteDeleteDialog();
    expect(await raunkierPlanteDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Raunkier Plante?');
    await raunkierPlanteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(raunkierPlanteComponentsPage.title), 5000);

    expect(await raunkierPlanteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
