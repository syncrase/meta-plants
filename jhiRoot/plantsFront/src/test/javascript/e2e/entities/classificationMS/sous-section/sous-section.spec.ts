import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SousSectionComponentsPage, SousSectionDeleteDialog, SousSectionUpdatePage } from './sous-section.page-object';

const expect = chai.expect;

describe('SousSection e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sousSectionComponentsPage: SousSectionComponentsPage;
  let sousSectionUpdatePage: SousSectionUpdatePage;
  let sousSectionDeleteDialog: SousSectionDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SousSections', async () => {
    await navBarPage.goToEntity('sous-section');
    sousSectionComponentsPage = new SousSectionComponentsPage();
    await browser.wait(ec.visibilityOf(sousSectionComponentsPage.title), 5000);
    expect(await sousSectionComponentsPage.getTitle()).to.eq('Sous Sections');
    await browser.wait(
      ec.or(ec.visibilityOf(sousSectionComponentsPage.entities), ec.visibilityOf(sousSectionComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SousSection page', async () => {
    await sousSectionComponentsPage.clickOnCreateButton();
    sousSectionUpdatePage = new SousSectionUpdatePage();
    expect(await sousSectionUpdatePage.getPageTitle()).to.eq('Create or edit a Sous Section');
    await sousSectionUpdatePage.cancel();
  });

  it('should create and save SousSections', async () => {
    const nbButtonsBeforeCreate = await sousSectionComponentsPage.countDeleteButtons();

    await sousSectionComponentsPage.clickOnCreateButton();

    await promise.all([
      sousSectionUpdatePage.setNomFrInput('nomFr'),
      sousSectionUpdatePage.setNomLatinInput('nomLatin'),
      sousSectionUpdatePage.sectionSelectLastOption(),
      sousSectionUpdatePage.sousSectionSelectLastOption(),
    ]);

    await sousSectionUpdatePage.save();
    expect(await sousSectionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sousSectionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SousSection', async () => {
    const nbButtonsBeforeDelete = await sousSectionComponentsPage.countDeleteButtons();
    await sousSectionComponentsPage.clickOnLastDeleteButton();

    sousSectionDeleteDialog = new SousSectionDeleteDialog();
    expect(await sousSectionDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Sous Section?');
    await sousSectionDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(sousSectionComponentsPage.title), 5000);

    expect(await sousSectionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
