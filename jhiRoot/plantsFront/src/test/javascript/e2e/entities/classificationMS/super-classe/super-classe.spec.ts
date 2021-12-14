import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SuperClasseComponentsPage, SuperClasseDeleteDialog, SuperClasseUpdatePage } from './super-classe.page-object';

const expect = chai.expect;

describe('SuperClasse e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let superClasseComponentsPage: SuperClasseComponentsPage;
  let superClasseUpdatePage: SuperClasseUpdatePage;
  let superClasseDeleteDialog: SuperClasseDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SuperClasses', async () => {
    await navBarPage.goToEntity('super-classe');
    superClasseComponentsPage = new SuperClasseComponentsPage();
    await browser.wait(ec.visibilityOf(superClasseComponentsPage.title), 5000);
    expect(await superClasseComponentsPage.getTitle()).to.eq('Super Classes');
    await browser.wait(
      ec.or(ec.visibilityOf(superClasseComponentsPage.entities), ec.visibilityOf(superClasseComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SuperClasse page', async () => {
    await superClasseComponentsPage.clickOnCreateButton();
    superClasseUpdatePage = new SuperClasseUpdatePage();
    expect(await superClasseUpdatePage.getPageTitle()).to.eq('Create or edit a Super Classe');
    await superClasseUpdatePage.cancel();
  });

  it('should create and save SuperClasses', async () => {
    const nbButtonsBeforeCreate = await superClasseComponentsPage.countDeleteButtons();

    await superClasseComponentsPage.clickOnCreateButton();

    await promise.all([
      superClasseUpdatePage.setNomFrInput('nomFr'),
      superClasseUpdatePage.setNomLatinInput('nomLatin'),
      superClasseUpdatePage.microEmbranchementSelectLastOption(),
      superClasseUpdatePage.superClasseSelectLastOption(),
    ]);

    await superClasseUpdatePage.save();
    expect(await superClasseUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await superClasseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SuperClasse', async () => {
    const nbButtonsBeforeDelete = await superClasseComponentsPage.countDeleteButtons();
    await superClasseComponentsPage.clickOnLastDeleteButton();

    superClasseDeleteDialog = new SuperClasseDeleteDialog();
    expect(await superClasseDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Super Classe?');
    await superClasseDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(superClasseComponentsPage.title), 5000);

    expect(await superClasseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
