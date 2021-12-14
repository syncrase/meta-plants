import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SousClasseComponentsPage, SousClasseDeleteDialog, SousClasseUpdatePage } from './sous-classe.page-object';

const expect = chai.expect;

describe('SousClasse e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sousClasseComponentsPage: SousClasseComponentsPage;
  let sousClasseUpdatePage: SousClasseUpdatePage;
  let sousClasseDeleteDialog: SousClasseDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SousClasses', async () => {
    await navBarPage.goToEntity('sous-classe');
    sousClasseComponentsPage = new SousClasseComponentsPage();
    await browser.wait(ec.visibilityOf(sousClasseComponentsPage.title), 5000);
    expect(await sousClasseComponentsPage.getTitle()).to.eq('Sous Classes');
    await browser.wait(ec.or(ec.visibilityOf(sousClasseComponentsPage.entities), ec.visibilityOf(sousClasseComponentsPage.noResult)), 1000);
  });

  it('should load create SousClasse page', async () => {
    await sousClasseComponentsPage.clickOnCreateButton();
    sousClasseUpdatePage = new SousClasseUpdatePage();
    expect(await sousClasseUpdatePage.getPageTitle()).to.eq('Create or edit a Sous Classe');
    await sousClasseUpdatePage.cancel();
  });

  it('should create and save SousClasses', async () => {
    const nbButtonsBeforeCreate = await sousClasseComponentsPage.countDeleteButtons();

    await sousClasseComponentsPage.clickOnCreateButton();

    await promise.all([
      sousClasseUpdatePage.setNomFrInput('nomFr'),
      sousClasseUpdatePage.setNomLatinInput('nomLatin'),
      sousClasseUpdatePage.classeSelectLastOption(),
      sousClasseUpdatePage.sousClasseSelectLastOption(),
    ]);

    await sousClasseUpdatePage.save();
    expect(await sousClasseUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sousClasseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SousClasse', async () => {
    const nbButtonsBeforeDelete = await sousClasseComponentsPage.countDeleteButtons();
    await sousClasseComponentsPage.clickOnLastDeleteButton();

    sousClasseDeleteDialog = new SousClasseDeleteDialog();
    expect(await sousClasseDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Sous Classe?');
    await sousClasseDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(sousClasseComponentsPage.title), 5000);

    expect(await sousClasseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
