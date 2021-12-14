import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SemisComponentsPage, SemisDeleteDialog, SemisUpdatePage } from './semis.page-object';

const expect = chai.expect;

describe('Semis e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let semisComponentsPage: SemisComponentsPage;
  let semisUpdatePage: SemisUpdatePage;
  let semisDeleteDialog: SemisDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Semis', async () => {
    await navBarPage.goToEntity('semis');
    semisComponentsPage = new SemisComponentsPage();
    await browser.wait(ec.visibilityOf(semisComponentsPage.title), 5000);
    expect(await semisComponentsPage.getTitle()).to.eq('Semis');
    await browser.wait(ec.or(ec.visibilityOf(semisComponentsPage.entities), ec.visibilityOf(semisComponentsPage.noResult)), 1000);
  });

  it('should load create Semis page', async () => {
    await semisComponentsPage.clickOnCreateButton();
    semisUpdatePage = new SemisUpdatePage();
    expect(await semisUpdatePage.getPageTitle()).to.eq('Create or edit a Semis');
    await semisUpdatePage.cancel();
  });

  it('should create and save Semis', async () => {
    const nbButtonsBeforeCreate = await semisComponentsPage.countDeleteButtons();

    await semisComponentsPage.clickOnCreateButton();

    await promise.all([
      semisUpdatePage.semisPleineTerreSelectLastOption(),
      semisUpdatePage.semisSousAbrisSelectLastOption(),
      semisUpdatePage.typeSemisSelectLastOption(),
      semisUpdatePage.germinationSelectLastOption(),
    ]);

    await semisUpdatePage.save();
    expect(await semisUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await semisComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Semis', async () => {
    const nbButtonsBeforeDelete = await semisComponentsPage.countDeleteButtons();
    await semisComponentsPage.clickOnLastDeleteButton();

    semisDeleteDialog = new SemisDeleteDialog();
    expect(await semisDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Semis?');
    await semisDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(semisComponentsPage.title), 5000);

    expect(await semisComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
