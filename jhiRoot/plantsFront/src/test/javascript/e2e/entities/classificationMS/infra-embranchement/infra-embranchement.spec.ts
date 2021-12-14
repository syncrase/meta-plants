import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  InfraEmbranchementComponentsPage,
  InfraEmbranchementDeleteDialog,
  InfraEmbranchementUpdatePage,
} from './infra-embranchement.page-object';

const expect = chai.expect;

describe('InfraEmbranchement e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let infraEmbranchementComponentsPage: InfraEmbranchementComponentsPage;
  let infraEmbranchementUpdatePage: InfraEmbranchementUpdatePage;
  let infraEmbranchementDeleteDialog: InfraEmbranchementDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load InfraEmbranchements', async () => {
    await navBarPage.goToEntity('infra-embranchement');
    infraEmbranchementComponentsPage = new InfraEmbranchementComponentsPage();
    await browser.wait(ec.visibilityOf(infraEmbranchementComponentsPage.title), 5000);
    expect(await infraEmbranchementComponentsPage.getTitle()).to.eq('Infra Embranchements');
    await browser.wait(
      ec.or(ec.visibilityOf(infraEmbranchementComponentsPage.entities), ec.visibilityOf(infraEmbranchementComponentsPage.noResult)),
      1000
    );
  });

  it('should load create InfraEmbranchement page', async () => {
    await infraEmbranchementComponentsPage.clickOnCreateButton();
    infraEmbranchementUpdatePage = new InfraEmbranchementUpdatePage();
    expect(await infraEmbranchementUpdatePage.getPageTitle()).to.eq('Create or edit a Infra Embranchement');
    await infraEmbranchementUpdatePage.cancel();
  });

  it('should create and save InfraEmbranchements', async () => {
    const nbButtonsBeforeCreate = await infraEmbranchementComponentsPage.countDeleteButtons();

    await infraEmbranchementComponentsPage.clickOnCreateButton();

    await promise.all([
      infraEmbranchementUpdatePage.setNomFrInput('nomFr'),
      infraEmbranchementUpdatePage.setNomLatinInput('nomLatin'),
      infraEmbranchementUpdatePage.sousDivisionSelectLastOption(),
      infraEmbranchementUpdatePage.infraEmbranchementSelectLastOption(),
    ]);

    await infraEmbranchementUpdatePage.save();
    expect(await infraEmbranchementUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await infraEmbranchementComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last InfraEmbranchement', async () => {
    const nbButtonsBeforeDelete = await infraEmbranchementComponentsPage.countDeleteButtons();
    await infraEmbranchementComponentsPage.clickOnLastDeleteButton();

    infraEmbranchementDeleteDialog = new InfraEmbranchementDeleteDialog();
    expect(await infraEmbranchementDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Infra Embranchement?');
    await infraEmbranchementDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(infraEmbranchementComponentsPage.title), 5000);

    expect(await infraEmbranchementComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
