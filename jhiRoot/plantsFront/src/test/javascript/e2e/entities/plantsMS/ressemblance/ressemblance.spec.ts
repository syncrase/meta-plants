import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { RessemblanceComponentsPage, RessemblanceDeleteDialog, RessemblanceUpdatePage } from './ressemblance.page-object';

const expect = chai.expect;

describe('Ressemblance e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ressemblanceComponentsPage: RessemblanceComponentsPage;
  let ressemblanceUpdatePage: RessemblanceUpdatePage;
  let ressemblanceDeleteDialog: RessemblanceDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Ressemblances', async () => {
    await navBarPage.goToEntity('ressemblance');
    ressemblanceComponentsPage = new RessemblanceComponentsPage();
    await browser.wait(ec.visibilityOf(ressemblanceComponentsPage.title), 5000);
    expect(await ressemblanceComponentsPage.getTitle()).to.eq('Ressemblances');
    await browser.wait(
      ec.or(ec.visibilityOf(ressemblanceComponentsPage.entities), ec.visibilityOf(ressemblanceComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Ressemblance page', async () => {
    await ressemblanceComponentsPage.clickOnCreateButton();
    ressemblanceUpdatePage = new RessemblanceUpdatePage();
    expect(await ressemblanceUpdatePage.getPageTitle()).to.eq('Create or edit a Ressemblance');
    await ressemblanceUpdatePage.cancel();
  });

  it('should create and save Ressemblances', async () => {
    const nbButtonsBeforeCreate = await ressemblanceComponentsPage.countDeleteButtons();

    await ressemblanceComponentsPage.clickOnCreateButton();

    await promise.all([
      ressemblanceUpdatePage.setDescriptionInput('description'),
      ressemblanceUpdatePage.planteRessemblantSelectLastOption(),
    ]);

    await ressemblanceUpdatePage.save();
    expect(await ressemblanceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ressemblanceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Ressemblance', async () => {
    const nbButtonsBeforeDelete = await ressemblanceComponentsPage.countDeleteButtons();
    await ressemblanceComponentsPage.clickOnLastDeleteButton();

    ressemblanceDeleteDialog = new RessemblanceDeleteDialog();
    expect(await ressemblanceDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Ressemblance?');
    await ressemblanceDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(ressemblanceComponentsPage.title), 5000);

    expect(await ressemblanceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
