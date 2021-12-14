import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { ClasseComponentsPage, ClasseDeleteDialog, ClasseUpdatePage } from './classe.page-object';

const expect = chai.expect;

describe('Classe e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let classeComponentsPage: ClasseComponentsPage;
  let classeUpdatePage: ClasseUpdatePage;
  let classeDeleteDialog: ClasseDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Classes', async () => {
    await navBarPage.goToEntity('classe');
    classeComponentsPage = new ClasseComponentsPage();
    await browser.wait(ec.visibilityOf(classeComponentsPage.title), 5000);
    expect(await classeComponentsPage.getTitle()).to.eq('Classes');
    await browser.wait(ec.or(ec.visibilityOf(classeComponentsPage.entities), ec.visibilityOf(classeComponentsPage.noResult)), 1000);
  });

  it('should load create Classe page', async () => {
    await classeComponentsPage.clickOnCreateButton();
    classeUpdatePage = new ClasseUpdatePage();
    expect(await classeUpdatePage.getPageTitle()).to.eq('Create or edit a Classe');
    await classeUpdatePage.cancel();
  });

  it('should create and save Classes', async () => {
    const nbButtonsBeforeCreate = await classeComponentsPage.countDeleteButtons();

    await classeComponentsPage.clickOnCreateButton();

    await promise.all([
      classeUpdatePage.setNomFrInput('nomFr'),
      classeUpdatePage.setNomLatinInput('nomLatin'),
      classeUpdatePage.superClasseSelectLastOption(),
      classeUpdatePage.classeSelectLastOption(),
    ]);

    await classeUpdatePage.save();
    expect(await classeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await classeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Classe', async () => {
    const nbButtonsBeforeDelete = await classeComponentsPage.countDeleteButtons();
    await classeComponentsPage.clickOnLastDeleteButton();

    classeDeleteDialog = new ClasseDeleteDialog();
    expect(await classeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Classe?');
    await classeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(classeComponentsPage.title), 5000);

    expect(await classeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
