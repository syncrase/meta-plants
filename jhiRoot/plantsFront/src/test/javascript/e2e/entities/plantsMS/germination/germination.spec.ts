import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { GerminationComponentsPage, GerminationDeleteDialog, GerminationUpdatePage } from './germination.page-object';

const expect = chai.expect;

describe('Germination e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let germinationComponentsPage: GerminationComponentsPage;
  let germinationUpdatePage: GerminationUpdatePage;
  let germinationDeleteDialog: GerminationDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Germinations', async () => {
    await navBarPage.goToEntity('germination');
    germinationComponentsPage = new GerminationComponentsPage();
    await browser.wait(ec.visibilityOf(germinationComponentsPage.title), 5000);
    expect(await germinationComponentsPage.getTitle()).to.eq('Germinations');
    await browser.wait(
      ec.or(ec.visibilityOf(germinationComponentsPage.entities), ec.visibilityOf(germinationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Germination page', async () => {
    await germinationComponentsPage.clickOnCreateButton();
    germinationUpdatePage = new GerminationUpdatePage();
    expect(await germinationUpdatePage.getPageTitle()).to.eq('Create or edit a Germination');
    await germinationUpdatePage.cancel();
  });

  it('should create and save Germinations', async () => {
    const nbButtonsBeforeCreate = await germinationComponentsPage.countDeleteButtons();

    await germinationComponentsPage.clickOnCreateButton();

    await promise.all([
      germinationUpdatePage.setTempsDeGerminationInput('tempsDeGermination'),
      germinationUpdatePage.setConditionDeGerminationInput('conditionDeGermination'),
    ]);

    await germinationUpdatePage.save();
    expect(await germinationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await germinationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Germination', async () => {
    const nbButtonsBeforeDelete = await germinationComponentsPage.countDeleteButtons();
    await germinationComponentsPage.clickOnLastDeleteButton();

    germinationDeleteDialog = new GerminationDeleteDialog();
    expect(await germinationDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Germination?');
    await germinationDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(germinationComponentsPage.title), 5000);

    expect(await germinationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
