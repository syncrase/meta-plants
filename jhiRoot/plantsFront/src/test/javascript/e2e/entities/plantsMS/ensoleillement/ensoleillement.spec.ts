import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { EnsoleillementComponentsPage, EnsoleillementDeleteDialog, EnsoleillementUpdatePage } from './ensoleillement.page-object';

const expect = chai.expect;

describe('Ensoleillement e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ensoleillementComponentsPage: EnsoleillementComponentsPage;
  let ensoleillementUpdatePage: EnsoleillementUpdatePage;
  let ensoleillementDeleteDialog: EnsoleillementDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Ensoleillements', async () => {
    await navBarPage.goToEntity('ensoleillement');
    ensoleillementComponentsPage = new EnsoleillementComponentsPage();
    await browser.wait(ec.visibilityOf(ensoleillementComponentsPage.title), 5000);
    expect(await ensoleillementComponentsPage.getTitle()).to.eq('Ensoleillements');
    await browser.wait(
      ec.or(ec.visibilityOf(ensoleillementComponentsPage.entities), ec.visibilityOf(ensoleillementComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Ensoleillement page', async () => {
    await ensoleillementComponentsPage.clickOnCreateButton();
    ensoleillementUpdatePage = new EnsoleillementUpdatePage();
    expect(await ensoleillementUpdatePage.getPageTitle()).to.eq('Create or edit a Ensoleillement');
    await ensoleillementUpdatePage.cancel();
  });

  it('should create and save Ensoleillements', async () => {
    const nbButtonsBeforeCreate = await ensoleillementComponentsPage.countDeleteButtons();

    await ensoleillementComponentsPage.clickOnCreateButton();

    await promise.all([
      ensoleillementUpdatePage.setOrientationInput('orientation'),
      ensoleillementUpdatePage.setEnsoleilementInput('5'),
      ensoleillementUpdatePage.planteSelectLastOption(),
    ]);

    await ensoleillementUpdatePage.save();
    expect(await ensoleillementUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ensoleillementComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last Ensoleillement', async () => {
    const nbButtonsBeforeDelete = await ensoleillementComponentsPage.countDeleteButtons();
    await ensoleillementComponentsPage.clickOnLastDeleteButton();

    ensoleillementDeleteDialog = new EnsoleillementDeleteDialog();
    expect(await ensoleillementDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Ensoleillement?');
    await ensoleillementDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(ensoleillementComponentsPage.title), 5000);

    expect(await ensoleillementComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
