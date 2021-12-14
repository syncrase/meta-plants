import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { InfraClasseComponentsPage, InfraClasseDeleteDialog, InfraClasseUpdatePage } from './infra-classe.page-object';

const expect = chai.expect;

describe('InfraClasse e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let infraClasseComponentsPage: InfraClasseComponentsPage;
  let infraClasseUpdatePage: InfraClasseUpdatePage;
  let infraClasseDeleteDialog: InfraClasseDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load InfraClasses', async () => {
    await navBarPage.goToEntity('infra-classe');
    infraClasseComponentsPage = new InfraClasseComponentsPage();
    await browser.wait(ec.visibilityOf(infraClasseComponentsPage.title), 5000);
    expect(await infraClasseComponentsPage.getTitle()).to.eq('Infra Classes');
    await browser.wait(
      ec.or(ec.visibilityOf(infraClasseComponentsPage.entities), ec.visibilityOf(infraClasseComponentsPage.noResult)),
      1000
    );
  });

  it('should load create InfraClasse page', async () => {
    await infraClasseComponentsPage.clickOnCreateButton();
    infraClasseUpdatePage = new InfraClasseUpdatePage();
    expect(await infraClasseUpdatePage.getPageTitle()).to.eq('Create or edit a Infra Classe');
    await infraClasseUpdatePage.cancel();
  });

  it('should create and save InfraClasses', async () => {
    const nbButtonsBeforeCreate = await infraClasseComponentsPage.countDeleteButtons();

    await infraClasseComponentsPage.clickOnCreateButton();

    await promise.all([
      infraClasseUpdatePage.setNomFrInput('nomFr'),
      infraClasseUpdatePage.setNomLatinInput('nomLatin'),
      infraClasseUpdatePage.sousClasseSelectLastOption(),
      infraClasseUpdatePage.infraClasseSelectLastOption(),
    ]);

    await infraClasseUpdatePage.save();
    expect(await infraClasseUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await infraClasseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last InfraClasse', async () => {
    const nbButtonsBeforeDelete = await infraClasseComponentsPage.countDeleteButtons();
    await infraClasseComponentsPage.clickOnLastDeleteButton();

    infraClasseDeleteDialog = new InfraClasseDeleteDialog();
    expect(await infraClasseDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Infra Classe?');
    await infraClasseDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(infraClasseComponentsPage.title), 5000);

    expect(await infraClasseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
