import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { TypeSemisComponentsPage, TypeSemisDeleteDialog, TypeSemisUpdatePage } from './type-semis.page-object';

const expect = chai.expect;

describe('TypeSemis e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let typeSemisComponentsPage: TypeSemisComponentsPage;
  let typeSemisUpdatePage: TypeSemisUpdatePage;
  let typeSemisDeleteDialog: TypeSemisDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TypeSemis', async () => {
    await navBarPage.goToEntity('type-semis');
    typeSemisComponentsPage = new TypeSemisComponentsPage();
    await browser.wait(ec.visibilityOf(typeSemisComponentsPage.title), 5000);
    expect(await typeSemisComponentsPage.getTitle()).to.eq('Type Semis');
    await browser.wait(ec.or(ec.visibilityOf(typeSemisComponentsPage.entities), ec.visibilityOf(typeSemisComponentsPage.noResult)), 1000);
  });

  it('should load create TypeSemis page', async () => {
    await typeSemisComponentsPage.clickOnCreateButton();
    typeSemisUpdatePage = new TypeSemisUpdatePage();
    expect(await typeSemisUpdatePage.getPageTitle()).to.eq('Create or edit a Type Semis');
    await typeSemisUpdatePage.cancel();
  });

  it('should create and save TypeSemis', async () => {
    const nbButtonsBeforeCreate = await typeSemisComponentsPage.countDeleteButtons();

    await typeSemisComponentsPage.clickOnCreateButton();

    await promise.all([typeSemisUpdatePage.setTypeInput('type'), typeSemisUpdatePage.setDescriptionInput('description')]);

    await typeSemisUpdatePage.save();
    expect(await typeSemisUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await typeSemisComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last TypeSemis', async () => {
    const nbButtonsBeforeDelete = await typeSemisComponentsPage.countDeleteButtons();
    await typeSemisComponentsPage.clickOnLastDeleteButton();

    typeSemisDeleteDialog = new TypeSemisDeleteDialog();
    expect(await typeSemisDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Type Semis?');
    await typeSemisDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(typeSemisComponentsPage.title), 5000);

    expect(await typeSemisComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
