import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { ReproductionComponentsPage, ReproductionDeleteDialog, ReproductionUpdatePage } from './reproduction.page-object';

const expect = chai.expect;

describe('Reproduction e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let reproductionComponentsPage: ReproductionComponentsPage;
  let reproductionUpdatePage: ReproductionUpdatePage;
  let reproductionDeleteDialog: ReproductionDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Reproductions', async () => {
    await navBarPage.goToEntity('reproduction');
    reproductionComponentsPage = new ReproductionComponentsPage();
    await browser.wait(ec.visibilityOf(reproductionComponentsPage.title), 5000);
    expect(await reproductionComponentsPage.getTitle()).to.eq('Reproductions');
    await browser.wait(
      ec.or(ec.visibilityOf(reproductionComponentsPage.entities), ec.visibilityOf(reproductionComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Reproduction page', async () => {
    await reproductionComponentsPage.clickOnCreateButton();
    reproductionUpdatePage = new ReproductionUpdatePage();
    expect(await reproductionUpdatePage.getPageTitle()).to.eq('Create or edit a Reproduction');
    await reproductionUpdatePage.cancel();
  });

  it('should create and save Reproductions', async () => {
    const nbButtonsBeforeCreate = await reproductionComponentsPage.countDeleteButtons();

    await reproductionComponentsPage.clickOnCreateButton();

    await promise.all([reproductionUpdatePage.setVitesseInput('vitesse'), reproductionUpdatePage.setTypeInput('type')]);

    await reproductionUpdatePage.save();
    expect(await reproductionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await reproductionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Reproduction', async () => {
    const nbButtonsBeforeDelete = await reproductionComponentsPage.countDeleteButtons();
    await reproductionComponentsPage.clickOnLastDeleteButton();

    reproductionDeleteDialog = new ReproductionDeleteDialog();
    expect(await reproductionDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Reproduction?');
    await reproductionDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(reproductionComponentsPage.title), 5000);

    expect(await reproductionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
