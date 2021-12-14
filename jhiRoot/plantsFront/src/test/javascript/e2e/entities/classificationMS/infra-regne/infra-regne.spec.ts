import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { InfraRegneComponentsPage, InfraRegneDeleteDialog, InfraRegneUpdatePage } from './infra-regne.page-object';

const expect = chai.expect;

describe('InfraRegne e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let infraRegneComponentsPage: InfraRegneComponentsPage;
  let infraRegneUpdatePage: InfraRegneUpdatePage;
  let infraRegneDeleteDialog: InfraRegneDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load InfraRegnes', async () => {
    await navBarPage.goToEntity('infra-regne');
    infraRegneComponentsPage = new InfraRegneComponentsPage();
    await browser.wait(ec.visibilityOf(infraRegneComponentsPage.title), 5000);
    expect(await infraRegneComponentsPage.getTitle()).to.eq('Infra Regnes');
    await browser.wait(ec.or(ec.visibilityOf(infraRegneComponentsPage.entities), ec.visibilityOf(infraRegneComponentsPage.noResult)), 1000);
  });

  it('should load create InfraRegne page', async () => {
    await infraRegneComponentsPage.clickOnCreateButton();
    infraRegneUpdatePage = new InfraRegneUpdatePage();
    expect(await infraRegneUpdatePage.getPageTitle()).to.eq('Create or edit a Infra Regne');
    await infraRegneUpdatePage.cancel();
  });

  it('should create and save InfraRegnes', async () => {
    const nbButtonsBeforeCreate = await infraRegneComponentsPage.countDeleteButtons();

    await infraRegneComponentsPage.clickOnCreateButton();

    await promise.all([
      infraRegneUpdatePage.setNomFrInput('nomFr'),
      infraRegneUpdatePage.setNomLatinInput('nomLatin'),
      infraRegneUpdatePage.rameauSelectLastOption(),
      infraRegneUpdatePage.infraRegneSelectLastOption(),
    ]);

    await infraRegneUpdatePage.save();
    expect(await infraRegneUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await infraRegneComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last InfraRegne', async () => {
    const nbButtonsBeforeDelete = await infraRegneComponentsPage.countDeleteButtons();
    await infraRegneComponentsPage.clickOnLastDeleteButton();

    infraRegneDeleteDialog = new InfraRegneDeleteDialog();
    expect(await infraRegneDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Infra Regne?');
    await infraRegneDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(infraRegneComponentsPage.title), 5000);

    expect(await infraRegneComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
