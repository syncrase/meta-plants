import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  MicroEmbranchementComponentsPage,
  MicroEmbranchementDeleteDialog,
  MicroEmbranchementUpdatePage,
} from './micro-embranchement.page-object';

const expect = chai.expect;

describe('MicroEmbranchement e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let microEmbranchementComponentsPage: MicroEmbranchementComponentsPage;
  let microEmbranchementUpdatePage: MicroEmbranchementUpdatePage;
  let microEmbranchementDeleteDialog: MicroEmbranchementDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load MicroEmbranchements', async () => {
    await navBarPage.goToEntity('micro-embranchement');
    microEmbranchementComponentsPage = new MicroEmbranchementComponentsPage();
    await browser.wait(ec.visibilityOf(microEmbranchementComponentsPage.title), 5000);
    expect(await microEmbranchementComponentsPage.getTitle()).to.eq('Micro Embranchements');
    await browser.wait(
      ec.or(ec.visibilityOf(microEmbranchementComponentsPage.entities), ec.visibilityOf(microEmbranchementComponentsPage.noResult)),
      1000
    );
  });

  it('should load create MicroEmbranchement page', async () => {
    await microEmbranchementComponentsPage.clickOnCreateButton();
    microEmbranchementUpdatePage = new MicroEmbranchementUpdatePage();
    expect(await microEmbranchementUpdatePage.getPageTitle()).to.eq('Create or edit a Micro Embranchement');
    await microEmbranchementUpdatePage.cancel();
  });

  it('should create and save MicroEmbranchements', async () => {
    const nbButtonsBeforeCreate = await microEmbranchementComponentsPage.countDeleteButtons();

    await microEmbranchementComponentsPage.clickOnCreateButton();

    await promise.all([
      microEmbranchementUpdatePage.setNomFrInput('nomFr'),
      microEmbranchementUpdatePage.setNomLatinInput('nomLatin'),
      microEmbranchementUpdatePage.infraEmbranchementSelectLastOption(),
      microEmbranchementUpdatePage.microEmbranchementSelectLastOption(),
    ]);

    await microEmbranchementUpdatePage.save();
    expect(await microEmbranchementUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await microEmbranchementComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last MicroEmbranchement', async () => {
    const nbButtonsBeforeDelete = await microEmbranchementComponentsPage.countDeleteButtons();
    await microEmbranchementComponentsPage.clickOnLastDeleteButton();

    microEmbranchementDeleteDialog = new MicroEmbranchementDeleteDialog();
    expect(await microEmbranchementDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Micro Embranchement?');
    await microEmbranchementDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(microEmbranchementComponentsPage.title), 5000);

    expect(await microEmbranchementComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
